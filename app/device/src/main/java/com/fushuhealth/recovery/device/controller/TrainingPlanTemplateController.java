package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.device.model.response.ListTrainingPlanTemplateResponse;
import com.fushuhealth.recovery.device.service.TrainingPlanTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainingPlanTemplate")
public class TrainingPlanTemplateController {

    @Autowired
    private TrainingPlanTemplateService trainingPlanTemplateService;

    @GetMapping("/list")
    public OldBaseResponse listTrainingPlanTemplate(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                                 @RequestParam(required = false) String name) {
        Long userId = SecurityUtils.getUserId();
        ListTrainingPlanTemplateResponse response = trainingPlanTemplateService.listTemplate(pageNo, pageSize, userId, name);
        return OldBaseResponse.success(response);
    }

//    @PostMapping("/save")
//    public BaseResponse saveTrainingPlanTemplate(@RequestBody SavePlanTemplateRequest request)  {
//        Long userId = AuthContext.getUserId();
//        trainingPlanTemplateService.saveTrainingPlanTemplate(userId, request);
//        return BaseResponse.success();
//    }
//
//    @PostMapping("/update")
//    public BaseResponse saveTrainingPlanTemplate(@RequestBody UpdateTrainingPlanTemplateRequest request)  {
//        trainingPlanTemplateService.updateTrainingPlanTemplate(request);
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/delete")
//    public BaseResponse deleteTrainingPlanTemplate(@RequestParam Long id)  {
//        trainingPlanTemplateService.deleteTrainingPlanTemplate(id);
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/actions")
//    public BaseResponse listTrainingPlanTemplate(@RequestParam Long id) {
//        TrainingPlanTemplateVo trainingPlanTemplate = trainingPlanTemplateService.getTrainingPlanTemplateVo(id);
//        return BaseResponse.success(trainingPlanTemplate);
//    }
}
