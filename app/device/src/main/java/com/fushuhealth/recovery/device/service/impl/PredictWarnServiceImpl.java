package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.constant.StatusType;
import com.fushuhealth.recovery.common.constant.WarnResultType;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.PredictWarnMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.PredictWarn;
import com.fushuhealth.recovery.device.model.request.PredictWarnRequest;
import com.fushuhealth.recovery.device.model.response.PredictWarnListResponse;
import com.fushuhealth.recovery.device.model.response.PredictWarnResponse;
import com.fushuhealth.recovery.device.model.vo.PredictWarnListVo;
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
    @Autowired
    private ChildrenMapper childrenMapper;
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

    @Override
    public BaseResponse<List<PredictWarnListResponse>> searchList(PredictWarnRequest request) {
        MPJLambdaWrapper<Children> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(PredictWarn::getMonthAge, PredictWarnListVo::getMonthAge)
                .selectAs(PredictWarn::getWarnResult,PredictWarnListVo::getWarnResult)
                .selectAs(PredictWarn::getSubmitTime,PredictWarnListVo::getSubmitTime)
                .leftJoin(PredictWarn.class,PredictWarn::getChildId,Children::getId);
        List<PredictWarnListVo> predictWarnListVos = childrenMapper.selectJoinList(PredictWarnListVo.class, lambdaWrapper);
        List<PredictWarnListResponse> responses = new ArrayList<>();
        predictWarnListVos.forEach(predictWarnListVo -> {
            PredictWarnListResponse response = BeanUtil.copyProperties(predictWarnListVo,PredictWarnListResponse.class);
            response.setMonthAge(MonthType.findMonthByType(predictWarnListVo.getMonthAge()));
            response.setWarnResult(MonthType.findMonthByType(predictWarnListVo.getWarnResult()));
            responses.add(response);
        });
        return new BaseResponse<List<PredictWarnListResponse>>(responses, (long) responses.size());
    }
}
