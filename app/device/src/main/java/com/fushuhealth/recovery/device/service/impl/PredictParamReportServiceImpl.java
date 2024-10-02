package com.fushuhealth.recovery.device.service.impl;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.constant.WarnResultType;
import com.fushuhealth.recovery.dal.dao.PredictParamReportMapper;
import com.fushuhealth.recovery.dal.dao.PredictWarnMapper;
import com.fushuhealth.recovery.dal.entity.PredictParamReport;
import com.fushuhealth.recovery.dal.entity.PredictWarn;
import com.fushuhealth.recovery.dal.entity.PredictWarnQuantification;
import com.fushuhealth.recovery.device.model.response.PredictParamReportResponse;
import com.fushuhealth.recovery.device.model.vo.PredictParamReportVo;
import com.fushuhealth.recovery.device.service.IPredictParamReportService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Service
public class PredictParamReportServiceImpl implements IPredictParamReportService {
    @Autowired
    private PredictParamReportMapper predictParamReportMapper;
    @Autowired
    private PredictWarnMapper predictWarnMapper;

    @Override
    public BaseResponse<PredictParamReportResponse> searchDetail(Long predictWarnId) {
        MPJLambdaWrapper<PredictParamReport> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(PredictParamReport.class)
                .selectAs(PredictWarnQuantification::getScreeningItem, PredictParamReportVo::getPredictWarnQuestion)
                .eq(PredictParamReport::getId,predictWarnId)
                .leftJoin(PredictWarnQuantification.class,PredictWarnQuantification::getId,PredictParamReport::getQuantificationId);
        List<PredictParamReportVo> predictParamReportVos = predictParamReportMapper.selectJoinList(PredictParamReportVo.class, lambdaWrapper);
        PredictWarn predictWarn = predictWarnMapper.selectById(predictWarnId);
        PredictParamReportResponse predictParamReportResponse = new PredictParamReportResponse();
        predictParamReportResponse.setVoList(predictParamReportVos);
        predictParamReportResponse.setWarnStatus(WarnResultType.findWarnResultByType(predictWarn.getWarnResult()));
        predictParamReportResponse.setMonthAge(MonthType.findMonthByType(predictWarn.getMonthAge()));
        return new BaseResponse<PredictParamReportResponse>(predictParamReportResponse, (long) predictParamReportVos.size());
    }
}
