package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.constant.StatusType;
import com.fushuhealth.recovery.common.constant.WarnResultType;
import com.fushuhealth.recovery.dal.dao.PredictWarnMapper;
import com.fushuhealth.recovery.dal.entity.PredictWarn;
import com.fushuhealth.recovery.device.model.response.PredictWarnResponse;
import com.fushuhealth.recovery.device.service.IPredictWarnService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Service
public class PredictWarnServiceImpl implements IPredictWarnService {
    @Autowired
    private PredictWarnMapper predictWarnMapper;
    @Override
    public BaseResponse<List<PredictWarnResponse>> searchPredictByChildId(Long id) {
        MPJLambdaWrapper<PredictWarn> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper.eq(PredictWarn::getChildId,id);
        List<PredictWarn> predictWarns = predictWarnMapper.selectList(lambdaQueryWrapper);
        List<PredictWarnResponse> predictWarnResponses = new ArrayList<>();
        predictWarns.forEach(predictWarn -> {
            PredictWarnResponse predictWarnResponse = BeanUtil.copyProperties(predictWarn, PredictWarnResponse.class);
            predictWarnResponse.setMonthAge(MonthType.findMonthByType(predictWarn.getMonthAge()));
            predictWarnResponse.setWarnStatus(StatusType.findStatusByType(predictWarn.getWarnStatus()));
            predictWarnResponse.setWarnResult(WarnResultType.findWarnResultByType(predictWarn.getWarnResult()));
            predictWarnResponses.add(predictWarnResponse);
        });
        return new BaseResponse<List<PredictWarnResponse>>(predictWarnResponses, (long) predictWarnResponses.size());
    }
}
