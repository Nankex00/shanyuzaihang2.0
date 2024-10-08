package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.DangerLevelType;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.EvaluateRecordMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.EvaluateRecord;
import com.fushuhealth.recovery.dal.entity.RepeatFiltrateRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.dto.EvaluateRecordListDto;
import com.fushuhealth.recovery.device.model.dto.RepeatFiltrateListDto;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordEditRequest;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordListRequest;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.response.*;
import com.fushuhealth.recovery.device.service.IEvaluateRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Service
public class EvaluateRecordServiceImpl implements IEvaluateRecordService {
    @Autowired
    private EvaluateRecordMapper evaluateRecordMapper;
    @Autowired
    private ChildrenMapper childrenMapper;
    @Override
    public int add(EvaluateRecordRequest request) {
        EvaluateRecord evaluateRecord = BeanUtil.copyProperties(request, EvaluateRecord.class);
        evaluateRecord.setSubmitTime(new Date());
        evaluateRecord.setOperatedId(SecurityUtils.getUserId());
        return evaluateRecordMapper.insert(evaluateRecord);
    }

    @Override
    public BaseResponse<List<EvaluateRecordResponse>> searchListByChildId(Long childId) {
        MPJLambdaWrapper<EvaluateRecord> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper.selectAll(EvaluateRecord.class)
                .selectAs(SysDept::getDeptName, EvaluateRecordResponse::getOperateDept)
                .eq(EvaluateRecord::getChildId,childId)
                .leftJoin(SysUser.class,SysUser::getUserId,EvaluateRecord::getOperatedId)
                .leftJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
        List<EvaluateRecordResponse> evaluateRecordResponses = evaluateRecordMapper.selectJoinList(EvaluateRecordResponse.class, lambdaQueryWrapper);
        evaluateRecordResponses.forEach((response)->{
            response.setMonthAge(MonthType.findMonthByType(Byte.parseByte(response.getMonthAge())));
        });
        return new BaseResponse<List<EvaluateRecordResponse>>(evaluateRecordResponses, (long) evaluateRecordResponses.size());

    }

    @Override
    public EvaluateRecordDetail searchDetail(Long id) {
        EvaluateRecord evaluateRecord = evaluateRecordMapper.selectById(id);
        return BeanUtil.copyProperties(evaluateRecord, EvaluateRecordDetail.class);
    }

    @Override
    public int editDetail(EvaluateRecordEditRequest request) {
        EvaluateRecord evaluateRecord = BeanUtil.copyProperties(request, EvaluateRecord.class);
        evaluateRecord.setSubmitTime(new Date());
        evaluateRecord.setOperatedId(SecurityUtils.getUserId());
        return evaluateRecordMapper.updateById(evaluateRecord);
    }

    @Override
    public int delete(Long id) {
        EvaluateRecord evaluateRecord = evaluateRecordMapper.selectById(id);
        //判断数据创建是否在一小时内
        Date submitTime = evaluateRecord.getSubmitTime();
        Date currentTime = new Date();
        Calendar submitCalendar = Calendar.getInstance();
        submitCalendar.setTime(submitTime);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentTime);
        currentCalendar.add(Calendar.HOUR, 1);
        boolean flag = currentCalendar.after(submitCalendar);
        //判断数据是否为本机构账号创建
        boolean equals = evaluateRecord.getOperatedId().equals(SecurityUtils.getUserId());
        if (!flag){
            throw new ServiceException("数据生成超过一小时，无法删除");
        }
        if (!equals){
            throw new ServiceException("数据不是本机构创建，无删除权限");
        }
        LambdaUpdateWrapper<EvaluateRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(EvaluateRecord::getId,id)
                .set(EvaluateRecord::getDelFlag,1);
        return evaluateRecordMapper.update(new EvaluateRecord(),lambdaUpdateWrapper);
    }

    @Override
    public BaseResponse<List<EvaluateRecordListResponse>> searchDeptList(EvaluateRecordListRequest request) {
        Page<EvaluateRecordListDto> page = new Page<>(request.getPageNum(),request.getPageSize());
        MPJLambdaWrapper<Children> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(EvaluateRecord::getMonthAge, EvaluateRecordListDto::getMonthAge)
                .selectAs(EvaluateRecord::getSubmitTime,EvaluateRecordListDto::getSubmitTime)
                .selectAs(SysDept::getDeptName,EvaluateRecordListDto::getDeptName)
                .innerJoin(EvaluateRecord.class,EvaluateRecord::getChildId,Children::getId)
                .innerJoin(SysUser.class,SysUser::getUserId,EvaluateRecord::getOperatedId)
                .innerJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
        if (request.getQuery()!=null&& !request.getQuery().isEmpty()){
            lambdaWrapper.like(Children::getId,request.getQuery())
                    .or()
                    .like(Children::getName,request.getQuery());
        }
        lambdaWrapper.orderByDesc(EvaluateRecord::getSubmitTime);
        childrenMapper.selectJoinPage(page,EvaluateRecordListDto.class, lambdaWrapper);
        List<EvaluateRecordListResponse> responses = new ArrayList<>();
        page.getRecords().forEach(evaluateRecordListDto -> {
            EvaluateRecordListResponse response = BeanUtil.copyProperties(evaluateRecordListDto,EvaluateRecordListResponse.class);
            response.setMonthAge(MonthType.findMonthByType(evaluateRecordListDto.getMonthAge()));
//            response.setDangerLevel(DangerLevelType.findDangerLevelByType(evaluateRecordListDto.getDangerLevel()));
            responses.add(response);
        });
        return new BaseResponse<List<EvaluateRecordListResponse>>(responses, page.getTotal());
    }
}
