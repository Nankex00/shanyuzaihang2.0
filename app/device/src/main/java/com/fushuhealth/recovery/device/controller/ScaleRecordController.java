package com.fushuhealth.recovery.device.controller;

import com.alibaba.fastjson.JSON;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.device.model.vo.ScaleTableVo;
import com.fushuhealth.recovery.device.service.ScaleTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/scale")
public class ScaleRecordController {

//    @Autowired
//    private ScaleRecordService scaleRecordService;
//
//    @Autowired
//    private FileService fileService;
//
    @Autowired
    private ScaleTableService scaleTableService;
//
//    @Autowired
//    private ScaleEvaluateLogService scaleEvaluateLogService;
//
//    /*
//    获取用户量表评测列表
//     */
//    @GetMapping("/record/list")
//    public BaseResponse listScaleRecord(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
//                                        @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
//                                        @RequestParam(required = false) String word,
//                                        @RequestParam(required = false, defaultValue = "0") Byte code,
//                                        @RequestParam(required = false, defaultValue = "0") Long start,
//                                        @RequestParam(required = false, defaultValue = "0") Long end,
//                                        @RequestParam(required = false, defaultValue = "0") Long userId,
//                                        @RequestParam(required = false, defaultValue = "0") Byte status) {
//        LoginVo user = AuthContext.getUser();
//        ScaleRecordQuery query = ScaleRecordQuery.builder()
//                .end(end)
//                .keyword(word)
//                .scaleCode(code)
//                .start(start)
//                .status(status)
//                .userId(userId).build();
//        ListScaleRecordResponse response = scaleRecordService.queryScaleRecord(pageNo, pageSize, user, query);
//        return BaseResponse.success(response);
//    }
//
//    /*
//    获取智能评估线下支付方式的审核列表
//     */
//    @GetMapping("/review/list")
//    public BaseResponse listWaitReviewScaleRecord(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
//                                                  @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize) {
//        ListScaleOrderResponse response = scaleRecordService.listReviewScaleOrder(pageNo, pageSize);
//        return BaseResponse.success(response);
//    }
//
//    /**
//     * 审核
//     */
//    @PostMapping("/review/update")
//    public BaseResponse updateReviewScaleRecord(@RequestBody ReviewScaleOrderRequest request) {
//        scaleRecordService.reviewScaleOrder(request);
//        return BaseResponse.success();
//    }
//
//    /*
//    获取用户评测详情，包含附件
//     */
//    @GetMapping("/record/get")
//    public BaseResponse getScaleRecord(@RequestParam Long id,
//                                       @RequestParam(required = false, defaultValue = "1") byte type,
//                                       @RequestParam(required = false, defaultValue = "0")Long fileId) {
//        ScaleRecordVo vo = scaleRecordService.getScaleRecordVo(id, type, fileId);
//        return BaseResponse.success(vo);
//    }
//
//    /*
//    医生更新用户评测的单个题目得分
//     */
//    @PostMapping("/record/updateScore")
//    public BaseResponse updateScore(@RequestBody UpdateScaleRecordAnswerScoreRequest request) {
//        LoginVo user = AuthContext.getUser();
//        scaleRecordService.updateScaleAnswerScore(user, request);
//        return BaseResponse.success();
//    }
//
//    /*
//    获取报告 pdf
//     */
//    @GetMapping("/record/report/preview")
//    public BaseResponse previewReport(Long id) {
//        String url = scaleRecordService.previewReport(id);
//        return BaseResponse.success(new SingletonMap("url", url));
//    }
//
//    @GetMapping("/report/picture")
//    public BaseResponse getScaleEvaluationRecordReportPic(@RequestParam Long id) {
//
//        //先生成 pdf
//        String url = scaleRecordService.previewReport(id);
//
//        //再获取图片
//        List<String> list = scaleRecordService.getReportPic(id);
////        List<String> list = new ArrayList<>();
////        list.add(fileDomain + "stq11.jpg");
////        list.add(fileDomain + "stq12.jpg");
////        list.add(fileDomain + "stq13.jpg");
////        list.add(fileDomain + "stq14.jpg");
//        return BaseResponse.success(list);
//    }
//
//    /*
//    医生更新用户评测的结论，并推送微信消息
//     */
//    @PostMapping("/record/updateRemark")
//    public BaseResponse updateRemark(@RequestBody Object request) {
//        LoginVo user = AuthContext.getUser();
//        scaleRecordService.updateScaleRemark(user, JSON.toJSONString(request));
//        return BaseResponse.success();
//    }
//
//    @PostMapping("/record/review")
//    public BaseResponse reviewScaleReport(@RequestBody ReviewScaleOrderRequest request) {
//        LoginVo user = AuthContext.getUser();
//        scaleRecordService.updateScaleRemark(user, JSON.toJSONString(request));
//        return BaseResponse.success();
//    }
//
//    /*
//    获取量表列表
//     */
//    @GetMapping("/scaleTable")
//    public BaseResponse listScaleTable() {
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ArrayList<ScaleTableListVo> list = new ArrayList<>(scaleTableMap.size());
//        for (Map.Entry<Byte, ScaleTable> entry : scaleTableMap.entrySet()) {
//            ScaleTableListVo vo = new ScaleTableListVo(entry.getValue().getName(), entry.getKey());
//            list.add(vo);
//        }
//        return BaseResponse.success(list);
//    }
//
//    @GetMapping("/question")
//    public BaseResponse getScaleTableQuestion(@RequestParam Byte code) {
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(code);
//        List<QuestionDto> questions = scaleTable.getQuestions();
//        List<String> list = questions.stream().sorted(Comparator.comparing(QuestionDto::getSn)).collect(Collectors.toList()).stream().map(questionDto -> {
//            Byte subject = questionDto.getSubject();
//            QuestionSubjectEnum questionSubject = QuestionSubjectEnum.getQuestionSubject(subject);
//            String name = questionDto.getName();
//            return name;
//        }).collect(Collectors.toList());
//        return BaseResponse.success(list);
//    }
//
//    @GetMapping("/scaleTable/question")
//    public BaseResponse getScaleTableQuestions(@RequestParam Byte code) {
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(code);
//        List<QuestionDto> questions = scaleTable.getQuestions();
//        List<QuestionWithOptionVo> list = questions.stream().map(questionDto -> {
//            return new QuestionWithOptionVo(questionDto.getSn(), questionDto.getName(), questionDto.getOptions());
//        }).collect(Collectors.toList());
//        return BaseResponse.success(list);
//    }
//
//    @GetMapping("/record/video")
//    public BaseResponse getScaleRecordVideo(@RequestParam Long id,
//                                            @RequestParam(required = false, defaultValue = "1") byte type) {
//        ScaleRecordVo vo = scaleRecordService.getScaleRecordVo(id, type, 0);
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleRecordService.getScaleEvaluationRecord(id);
//        ArrayList<String> list = new ArrayList<>();
//        if (scaleEvaluationRecord != null) {
//            String answerWithRemark = scaleEvaluationRecord.getAnswerWithRemark();
//            if (StringUtils.isNotBlank(answerWithRemark)) {
//                List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//                for (AnswerWithRemarkDto answer : answers) {
//                    List<AttachmentDto> attachmentDtos = answer.getAttachmentDtos();
//                    if (CollectionUtils.isNotEmpty(attachmentDtos)) {
//                        for (AttachmentDto attachment : attachmentDtos) {
//                            if (attachment != null) {
//                                Files file = fileService.getFile(attachment.getFileId());
//                                if (FileType.VIDEO.getCode() == file.getFileType()) {
//                                    list.add(fileService.getFileUrl(file.getId(), true));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return BaseResponse.success(list);
//    }
//
//    /**
//     * 取消门诊量表评估
//     * @param id
//     * @return
//     */
//    @GetMapping("/record/cancel")
//    public BaseResponse cancelClinicScaleTableEvaluation(@RequestParam Long id) {
//        scaleRecordService.cancelClinicScaleTableEvaluation(id);
//        return BaseResponse.success();
//    }
//
//    /**
//     * 更新门诊评估预约时间
//     * @param id
//     * @param time
//     * @return
//     */
//    @GetMapping("/record/reserve-time/update")
//    public BaseResponse updateReserveTime(@RequestParam Long id,
//                                          @RequestParam Long time) {
//        scaleRecordService.updateEvaluationReserveTime(id, time);
//        return BaseResponse.success();
//    }
//
    /**
     * 获取量表详情
     * @param code
     * @return
     */
    @GetMapping("/scaleTable/get")
    public AjaxResult getScaleTable(@RequestParam(defaultValue = "0") Byte code) {
        ScaleTableVo scaleTableVo = scaleTableService.getScaleTableVo(code);
        return AjaxResult.success(scaleTableVo);
    }
//
//    /**
//     * 更新记分册
//     * @return
//     */
//    @PostMapping("/scoring-book/update")
//    public BaseResponse updateScoringBook(@RequestBody UpdateScoringBookRequest request) {
//        scaleRecordService.updateScoringBook(request.getId(), request.getAnswers());
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/scoring-book/get")
//    public BaseResponse getScoringBook(@RequestParam long id) {
//        List<AnswerRequest> scoringBook = scaleRecordService.getScoringBook(id);
//        return BaseResponse.success(scoringBook);
//    }
//
//    @GetMapping("/operation/log")
//    public BaseResponse listOperationLogs(@RequestParam long id) {
//        return BaseResponse.success(scaleEvaluateLogService.listScaleEvaluateLogByScaleRecordId(id));
//    }
//
//    @PostMapping("/report/review")
//    public BaseResponse reviewScaleReport(@RequestBody ReviewScaleReportRequest request) {
//        scaleRecordService.reviewScaleReport(request);
//        return BaseResponse.success();
//    }
//
//    @PostMapping("/report/batch/review")
//    public BaseResponse batchReviewScaleReport(@RequestBody BatchReviewScaleReportRequest request) {
//        List<Long> ids = request.getIds();
//        if (CollectionUtils.isEmpty(ids)) return BaseResponse.success();
//
//        for (Long id : ids) {
//            try {
//                ReviewScaleReportRequest reportRequest = new ReviewScaleReportRequest();
//                reportRequest.setId(id);
//                reportRequest.setResult(request.isResult());
//                reportRequest.setRemark(request.getRemark());
//                scaleRecordService.reviewScaleReport(reportRequest);
//            } catch (Exception e) {
//                log.error("review report error", e);
//            }
//
//        }
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/report/send")
//    public BaseResponse sendScaleReport(@RequestParam long id) {
//        scaleRecordService.sendReport(id);
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/report/batch/send")
//    public BaseResponse batchSendScaleReport(@RequestParam List<Long> ids) {
//        if (CollectionUtils.isEmpty(ids)) return BaseResponse.success();
//        for (Long id : ids) {
//            try {
//                scaleRecordService.sendReport(id);
//            } catch (Exception e) {
//                log.error("send report error", e);
//            }
//        }
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/report/submitReview")
//    public BaseResponse submitReview(@RequestParam long id) {
//        scaleRecordService.submitReview(id);
//        return BaseResponse.success();
//    }
}
