package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.DangerLevelType;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.constant.WarnResultType;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.RepeatFiltrateRecordMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.PredictWarn;
import com.fushuhealth.recovery.dal.entity.RepeatFiltrateRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.dto.RepeatFiltrateListDto;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateListRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateRecordRequest;
import com.fushuhealth.recovery.device.model.response.PredictWarnListResponse;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateListResponse;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordDetail;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordResponse;
import com.fushuhealth.recovery.device.model.vo.PredictWarnListVo;
import com.fushuhealth.recovery.device.service.IRepeatFiltrateRecordService;
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
public class RepeatFiltrateRecordServiceImpl implements IRepeatFiltrateRecordService {
    @Autowired
    private RepeatFiltrateRecordMapper repeatFiltrateRecordMapper;
    @Autowired
    private ChildrenMapper childrenMapper;
    @Override
    public int addRepeatFiltrateRecord(RepeatFiltrateRecordRequest request) {
        RepeatFiltrateRecord repeatFiltrateRecord = BeanUtil.copyProperties(request,RepeatFiltrateRecord.class);
        repeatFiltrateRecord.setOperatedId(SecurityUtils.getUserId());
        repeatFiltrateRecord.setSubmitTime(new Date());
        return repeatFiltrateRecordMapper.insert(repeatFiltrateRecord);
    }

    @Override
    public BaseResponse<List<RepeatFiltrateRecordResponse>> searchListByChildId(Long childId) {
        MPJLambdaWrapper<RepeatFiltrateRecord> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper.selectAll(RepeatFiltrateRecord.class)
                .selectAs(SysDept::getDeptName,RepeatFiltrateRecordResponse::getOperateDept)
                .eq(RepeatFiltrateRecord::getChildId,childId)
                .leftJoin(SysUser.class,SysUser::getUserId,RepeatFiltrateRecord::getOperatedId)
                .leftJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
        List<RepeatFiltrateRecordResponse> repeatFiltrateRecordResponses = repeatFiltrateRecordMapper.selectJoinList(RepeatFiltrateRecordResponse.class, lambdaQueryWrapper);
        repeatFiltrateRecordResponses.forEach((response)->{
            response.setMonthAge(MonthType.findMonthByType(Byte.parseByte(response.getMonthAge())));
        });
        return new BaseResponse<List<RepeatFiltrateRecordResponse>>(repeatFiltrateRecordResponses, (long) repeatFiltrateRecordResponses.size());
    }

    @Override
    public RepeatFiltrateRecordDetail searchDetail(Long id) {
        RepeatFiltrateRecord repeatFiltrateRecord = repeatFiltrateRecordMapper.selectById(id);
        return BeanUtil.copyProperties(repeatFiltrateRecord, RepeatFiltrateRecordDetail.class);
    }

    @Override
    public int editDetail(RepeatFiltrateEditRequest request) {
        RepeatFiltrateRecord repeatFiltrateRecord = BeanUtil.copyProperties(request, RepeatFiltrateRecord.class);
        repeatFiltrateRecord.setSubmitTime(new Date());
        repeatFiltrateRecord.setOperatedId(SecurityUtils.getUserId());
        return repeatFiltrateRecordMapper.updateById(repeatFiltrateRecord);
    }

    @Override
    public int delete(Long id) {
        RepeatFiltrateRecord repeatFiltrateRecord = repeatFiltrateRecordMapper.selectById(id);
        //判断数据创建是否在一小时内
        Date submitTime = repeatFiltrateRecord.getSubmitTime();
        Date currentTime = new Date();
        Calendar submitCalendar = Calendar.getInstance();
        submitCalendar.setTime(submitTime);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentTime);
        currentCalendar.add(Calendar.HOUR, 1);
        boolean flag = currentCalendar.after(submitCalendar);
        //判断数据是否为本机构账号创建
        boolean equals = repeatFiltrateRecord.getOperatedId().equals(SecurityUtils.getUserId());
        if (!flag){
            throw new ServiceException("数据生成超过一小时，无法删除");
        }
        if (!equals){
            throw new ServiceException("数据不是本机构创建，无删除权限");
        }
        LambdaUpdateWrapper<RepeatFiltrateRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(RepeatFiltrateRecord::getId,id)
                .set(RepeatFiltrateRecord::getDelFlag,1);
        return repeatFiltrateRecordMapper.update(new RepeatFiltrateRecord(),lambdaUpdateWrapper);
    }

    @Override
    public BaseResponse<List<RepeatFiltrateListResponse>> searchDeptList(RepeatFiltrateListRequest request) {
        Page<RepeatFiltrateListDto> page = new Page<>(request.getPageNum(),request.getPageSize());
        MPJLambdaWrapper<Children> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(RepeatFiltrateRecord::getMonthAge, RepeatFiltrateListDto::getMonthAge)
                .selectAs(RepeatFiltrateRecord::getSubmitTime,RepeatFiltrateListDto::getSubmitTime)
                .selectAs(SysDept::getDeptName,RepeatFiltrateListDto::getDeptName)
                .innerJoin(RepeatFiltrateRecord.class,RepeatFiltrateRecord::getChildId,Children::getId)
                .innerJoin(SysUser.class,SysUser::getUserId,RepeatFiltrateRecord::getOperatedId)
                .innerJoin(SysDept.class,SysDept::getDeptId,SysUser::getUserId);
        Byte type = request.getType() != null ? request.getType() : 0; // 默认值为 0，根据需要修改
        lambdaWrapper.eq(type != 0, Children::getDangerLevel, type);
        if (request.getQuery()!=null&& !request.getQuery().isEmpty()){
            lambdaWrapper.like(Children::getId,request.getQuery())
                    .or()
                    .like(Children::getName,request.getQuery());
        }
        lambdaWrapper.orderByDesc(RepeatFiltrateRecord::getSubmitTime);
        childrenMapper.selectJoinPage(page,RepeatFiltrateListDto.class, lambdaWrapper);
        List<RepeatFiltrateListResponse> responses = new ArrayList<>();
        page.getRecords().forEach(repeatFiltrateListDto -> {
            RepeatFiltrateListResponse response = BeanUtil.copyProperties(repeatFiltrateListDto,RepeatFiltrateListResponse.class);
            response.setMonthAge(MonthType.findMonthByType(repeatFiltrateListDto.getMonthAge()));
            response.setDangerLevel(DangerLevelType.findDangerLevelByType(repeatFiltrateListDto.getDangerLevel()));
            responses.add(response);
        });
        return new BaseResponse<List<RepeatFiltrateListResponse>>(responses, page.getTotal());
    }
}
