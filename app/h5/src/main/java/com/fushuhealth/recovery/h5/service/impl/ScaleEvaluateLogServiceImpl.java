package com.fushuhealth.recovery.h5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.h5.service.ScaleEvaluateLogService;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.constant.ScaleStatus;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.dal.dao.ScaleEvaluateLogDao;
import com.fushuhealth.recovery.dal.entity.ScaleEvaluateLog;
import com.fushuhealth.recovery.dal.vo.h5.ScaleEvaluateLogListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScaleEvaluateLogServiceImpl implements ScaleEvaluateLogService {

    @Autowired
    private ScaleEvaluateLogDao scaleEvaluateLogDao;

//    @Autowired
//    private DoctorService doctorService;

    @Override
    public List<ScaleEvaluateLogListVo> listScaleEvaluateLogByScaleRecordId(long recordId) {
        QueryWrapper<ScaleEvaluateLog> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ScaleEvaluateLog::getScaleRecordId, recordId)
                .eq(ScaleEvaluateLog::getStatus, BaseStatus.NORMAL.getStatus());
        List<ScaleEvaluateLog> scaleEvaluateLogs = scaleEvaluateLogDao.selectList(wrapper);
        return scaleEvaluateLogs.stream().map(log -> convert2ScaleEvaluateLogListVo(log)).collect(Collectors.toList());
    }

    private ScaleEvaluateLogListVo convert2ScaleEvaluateLogListVo(ScaleEvaluateLog log) {
        if (log == null) {
            return null;
        }
        //todo:医生签名
//        DoctorVo doctorVo = doctorService.getDoctorVo(log.getUserId());
        ScaleEvaluateLogListVo vo = new ScaleEvaluateLogListVo();
        vo.setCreated(DateUtil.getYMDHMSDate(log.getCreated()));
//        vo.setUserName(doctorVo == null ? "" : doctorVo.getName());
        vo.setUserId(log.getUserId());
        vo.setStatusByte(log.getScaleStatus());
        vo.setStatus(ScaleStatus.getStatus(log.getScaleStatus()).getDesc());
        vo.setId(log.getId());
        vo.setRemark(log.getRemark());
        return vo;
    }
}
