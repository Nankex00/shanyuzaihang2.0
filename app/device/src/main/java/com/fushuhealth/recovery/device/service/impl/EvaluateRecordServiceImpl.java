package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.EvaluateRecordMapper;
import com.fushuhealth.recovery.dal.entity.EvaluateRecord;
import com.fushuhealth.recovery.dal.entity.RepeatFiltrateRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordEditRequest;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.response.EvaluateRecordDetail;
import com.fushuhealth.recovery.device.model.response.EvaluateRecordResponse;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordDetail;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordResponse;
import com.fushuhealth.recovery.device.service.IEvaluateRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
