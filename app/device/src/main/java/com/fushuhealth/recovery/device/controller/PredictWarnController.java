package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.device.service.IPredictWarnService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@RestController
@RequestMapping("/predict")
public class PredictWarnController extends BaseController {
    @Autowired
    private IPredictWarnService iPredictWarnService;

    @GetMapping("/child/{id}")
    @ApiOperation("儿童详情-预警记录列表")
    public AjaxResult searchPredictByChildId(@PathVariable Long id){
        return AjaxResult.success(iPredictWarnService.searchPredictByChildId(id));
    }

    @GetMapping("/child/report")
    @ApiOperation("儿童详情-预警报告")
    public AjaxResult searchPredictReport(){
        return AjaxResult.success();
    }

}
