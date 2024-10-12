package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.dal.vo.TrainingResultVo;
import com.fushuhealth.recovery.device.service.TrainingRecordResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainingRecordResult")
public class TrainingRecordResultController {

    @Autowired
    private TrainingRecordResultService trainingRecordResultService;

//    @Autowired
//    private TrainingRecordService trainingRecordService;
//
//    @GetMapping("/plan/get")
//    public BaseResponse getResultByPlanId(@RequestParam Long planId,
//                                          @RequestParam Long day) {
//        TrainingRecordResultPlanVo vo = trainingRecordResultService.getResultByPlanId(planId, day);
//        return BaseResponse.success(vo);
//    }
//
//    @GetMapping("/list")
//    public BaseResponse getResultByTrainingRecordId(@RequestParam Long recordId) {
//        TrainingRecordResultPlanVo vo = trainingRecordResultService.getResultByRecordId(recordId);
//        return BaseResponse.success(vo);
//    }

//    @GetMapping("/get")
//    public BaseResponse getResultById(@RequestParam Long id) {
//        TrainingResultVo result = null;
//        ActionVo actionVo = trainingRecordResultService.getTrainingAction(id);
//        if (actionVo != null && actionVo.getResultPage().equals("4") && actionVo.getActionName().contains("GMs")) {
//            result = trainingRecordResultService.getMockResult(id);
//        } else {
//            result = trainingRecordResultService.getResult(id);
//        }
//        return BaseResponse.success(result);
//    }

    @GetMapping("/get")
    public OldBaseResponse getResultById(@RequestParam Long id) {
        TrainingResultVo result = trainingRecordResultService.getScaleRecordResult(id);
//        ActionVo actionVo = trainingRecordResultService.getTrainingAction(id);
//        if (actionVo != null && actionVo.getResultPage().equals("4") && actionVo.getActionName().contains("GMs")) {
//            result = trainingRecordResultService.getMockResult(id);
//        } else {
//            result = trainingRecordResultService.getResult(id);
//        }
        return OldBaseResponse.success(result);
    }
    //
//    @PostMapping("/notify")
//    public BaseResponse handleResult(@RequestBody GaitAnalysisResultNotifyRequest resultNotifyRequest) {
//        trainingRecordResultService.resolveScaleResult(resultNotifyRequest);
//        return BaseResponse.success();
//    }
//    //
//    @Deprecated
//    @PostMapping("/update")
//    public BaseResponse updateResult(@RequestBody UpdateRecordResultRequest request) {
//        trainingRecordResultService.updateResult(request);
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/picture/get")
//    public BaseResponse getResultPicture(@RequestParam Long id,
//                                         @RequestParam(defaultValue = "0") Integer index) {
//        RecordResultPictureVo resultPicture = trainingRecordResultService.getResultPicture(id, index);
//        return BaseResponse.success(resultPicture);
//    }
//
//    @PostMapping("/picture/update")
//    public BaseResponse updatePictureResult(@RequestBody UpdateRecordResultPictureRequest recordResultPictureRequest) {
//        RecordResultPictureVo recordResultPictureVo = trainingRecordResultService.updatePictureKeyPoints(recordResultPictureRequest);
//        return BaseResponse.success(recordResultPictureVo);
//    }
//
//    @GetMapping("/share")
//    public BaseResponse shareTrainingRecordResult(long recordId) {
//        ShareRecordResultVo shareRecordResultVo = trainingRecordService.shareRecordResult(recordId);
//        return BaseResponse.success(shareRecordResultVo);
//    }
}
