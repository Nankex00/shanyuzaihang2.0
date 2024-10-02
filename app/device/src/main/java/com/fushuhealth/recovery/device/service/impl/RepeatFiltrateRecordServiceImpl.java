package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.RepeatFiltrateRecordMapper;
import com.fushuhealth.recovery.dal.entity.RepeatFiltrateRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateRecordRequest;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordDetail;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordResponse;
import com.fushuhealth.recovery.device.service.IRepeatFiltrateRecordService;
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
public class RepeatFiltrateRecordServiceImpl implements IRepeatFiltrateRecordService {
    @Autowired
    private RepeatFiltrateRecordMapper repeatFiltrateRecordMapper;
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
}
