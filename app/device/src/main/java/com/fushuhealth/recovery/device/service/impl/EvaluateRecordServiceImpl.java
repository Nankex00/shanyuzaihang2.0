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
import com.fushuhealth.recovery.dal.dto.FileDetailDto;
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
import com.fushuhealth.recovery.device.service.FileService;
import com.fushuhealth.recovery.device.service.IEvaluateRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    private FileService fileService;
    @Override
    public int add(EvaluateRecordRequest request) {
        List<String> geSells = fileService.operateFile(null, request.getGeSellUrls());
        List<String> ss = fileService.operateFile(null, request.getSSUrls());
        List<String> other = fileService.operateFile(null, request.getOtherUrls());
        EvaluateRecord evaluateRecord = BeanUtil.copyProperties(request, EvaluateRecord.class);
        evaluateRecord.setGeSellUrls(String.valueOf(geSells));
        evaluateRecord.setSSUrls(String.valueOf(ss));
        evaluateRecord.setOtherUrls(String.valueOf(other));
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
                .eq(EvaluateRecord::getDelFlag,0)
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
        EvaluateRecordDetail evaluateRecordDetail = BeanUtil.copyProperties(evaluateRecord, EvaluateRecordDetail.class);
        String geSellUrls = evaluateRecord.getGeSellUrls();
        if (geSellUrls!=null){
            geSellUrls =geSellUrls.substring(1, geSellUrls.length() - 1);
            List<FileDetailDto> geSell = new ArrayList<>();
            Arrays.stream(geSellUrls.split(", ")).forEach(ids->{
                String filePath = fileService.getFilePath(Long.parseLong(ids));
                geSell.add(new FileDetailDto(Long.parseLong(ids),fileService.getFileUrl(Long.parseLong(ids),true)));
            });
            evaluateRecordDetail.setGeSellUrls(geSell.toString());
        }

        String ssUrls = evaluateRecord.getSSUrls();
        if (ssUrls!=null){
            ssUrls =ssUrls.substring(1, ssUrls.length() - 1);
            List<FileDetailDto> ss = new ArrayList<>();
            Arrays.stream(ssUrls.split(", ")).forEach(ids->{
                String filePath = fileService.getFilePath(Long.parseLong(ids));
                ss.add(new FileDetailDto(Long.parseLong(ids),fileService.getFileUrl(Long.parseLong(ids),true)));
            });
            evaluateRecordDetail.setSSUrls(ss.toString());
        }

        String otherUrls = evaluateRecord.getOtherUrls();
        if (otherUrls!=null){
            otherUrls =otherUrls.substring(1, otherUrls.length() - 1);
            List<FileDetailDto> others = new ArrayList<>();
            Arrays.stream(otherUrls.split(", ")).forEach(ids->{
                String filePath = fileService.getFilePath(Long.parseLong(ids));
                others.add(new FileDetailDto(Long.parseLong(ids),fileService.getFileUrl(Long.parseLong(ids),true)));
            });
            evaluateRecordDetail.setOtherUrls(others.toString());
        }
        return evaluateRecordDetail;
    }

    @Override
    public int editDetail(EvaluateRecordEditRequest request) {
        EvaluateRecord evaluateRecord1 = Optional.ofNullable(evaluateRecordMapper.selectById(request.getId())).orElseThrow(() -> new ServiceException("数据异常，不存在对应记录"));
        List<String> geSell = fileService.operateFile(evaluateRecord1.getGeSellResult(), request.getGeSellUrls());
        List<String> ss = fileService.operateFile(evaluateRecord1.getSSUrls(), request.getSSUrls());
        List<String> others = fileService.operateFile(evaluateRecord1.getOtherUrls(), request.getOtherUrls());
        EvaluateRecord evaluateRecord = BeanUtil.copyProperties(request, EvaluateRecord.class);
        evaluateRecord.setSubmitTime(new Date());
        evaluateRecord.setOperatedId(SecurityUtils.getUserId());
        if(CollectionUtils.isNotEmpty(geSell)){
            evaluateRecord.setGeSellUrls(String.valueOf(geSell));
        }else {
            evaluateRecord.setGeSellUrls("[]");
        }
        if(CollectionUtils.isNotEmpty(ss)){
            evaluateRecord.setGeSellUrls(String.valueOf(ss));
        }else {
            evaluateRecord.setSSUrls("[]");
        }
        if(CollectionUtils.isNotEmpty(others)){
            evaluateRecord.setOtherUrls(String.valueOf(others));
        }else {
            evaluateRecord.setOtherUrls("[]");
        }

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
