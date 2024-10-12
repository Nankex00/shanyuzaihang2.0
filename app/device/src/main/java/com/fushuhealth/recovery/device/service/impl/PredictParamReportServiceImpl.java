package com.fushuhealth.recovery.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.constant.WarnResultType;
import com.fushuhealth.recovery.dal.dao.PredictParamReportMapper;
import com.fushuhealth.recovery.dal.dao.PredictParamScaleMapper;
import com.fushuhealth.recovery.dal.dao.PredictWarnMapper;
import com.fushuhealth.recovery.dal.dao.ScaleEvaluationRecordDao;
import com.fushuhealth.recovery.dal.dto.AnswerWithRemarkDto;
import com.fushuhealth.recovery.dal.dto.AttachmentDto;
import com.fushuhealth.recovery.dal.entity.*;
import com.fushuhealth.recovery.dal.vo.AttachmentVo;
import com.fushuhealth.recovery.device.model.response.PredictParamReportResponse;
import com.fushuhealth.recovery.device.model.vo.PredictParamReportVo;
import com.fushuhealth.recovery.device.service.FileService;
import com.fushuhealth.recovery.device.service.IPredictParamReportService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private ScaleEvaluationRecordDao scaleEvaluationRecordDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private PredictParamScaleMapper predictParamScaleMapper;

    @Override
    public PredictParamReportResponse searchDetail(Long predictWarnId) {
        //todo:修改结构
        MPJLambdaWrapper<PredictParamReport> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(PredictParamReport.class)
                .selectAs(PredictWarnQuantification::getScreeningItem, PredictParamReportVo::getPredictWarnQuestion)
                .eq(PredictParamReport::getId,predictWarnId)
                .leftJoin(PredictWarnQuantification.class,PredictWarnQuantification::getId,PredictParamReport::getQuantificationId);
        List<PredictParamReportVo> predictParamReportVos = predictParamReportMapper.selectJoinList(PredictParamReportVo.class, lambdaWrapper);
        if (CollectionUtils.isNotEmpty(predictParamReportVos)){
            predictParamReportVos.forEach(predictParamReportVo ->{
                MPJLambdaWrapper<PredictParamScale> mpjLambdaWrapper = new MPJLambdaWrapper<>();
                mpjLambdaWrapper.selectAll(ScaleEvaluationRecord.class)
                        .eq(PredictParamScale::getParamId,predictParamReportVo.getId())
                        .leftJoin(ScaleEvaluationRecord.class,ScaleEvaluationRecord::getId,PredictParamScale::getScaleId);
                List<ScaleEvaluationRecord> scaleEvaluationRecords = predictParamScaleMapper.selectJoinList(ScaleEvaluationRecord.class, mpjLambdaWrapper);
                ArrayList<AttachmentVo> videos = new ArrayList<>();
                scaleEvaluationRecords.forEach(scaleEvaluationRecord -> {
                    String answerWithRemark = scaleEvaluationRecord.getAnswerWithRemark();

                    if (StringUtils.isNotBlank(answerWithRemark)) {
                        List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
                        if (CollectionUtils.isNotEmpty(answerWithRemarkDtos)) {
                            AnswerWithRemarkDto answerWithRemarkDto = answerWithRemarkDtos.get(0);
                            List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
                            if (CollectionUtils.isNotEmpty(attachmentDtos)) {
                                for (AttachmentDto attachmentDto : attachmentDtos) {
                                    if (attachmentDto != null) {
                                        AttachmentVo attachmentVo = new AttachmentVo();
                                        attachmentVo.setCoverUrl(attachmentDto.getCoverFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getCoverFileId(), false));
                                        attachmentVo.setUrl(attachmentDto.getFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getFileId(), false));
                                        videos.add(attachmentVo);
                                    }
                                }
                            }
                        }
                    }

                });
                predictParamReportVo.setVideoVos(videos);
            });
        }

        PredictWarn predictWarn = predictWarnMapper.selectById(predictWarnId);
        PredictParamReportResponse predictParamReportResponse = new PredictParamReportResponse();
        predictParamReportResponse.setVoList(predictParamReportVos);
        predictParamReportResponse.setWarnStatus(WarnResultType.findWarnResultByType(predictWarn.getWarnResult()));
        predictParamReportResponse.setMonthAge(MonthType.findMonthByType(predictWarn.getMonthAge()));
        return predictParamReportResponse;
    }
}
