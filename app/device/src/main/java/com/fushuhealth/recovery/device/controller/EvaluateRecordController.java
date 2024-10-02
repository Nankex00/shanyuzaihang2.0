package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordEditRequest;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.service.IEvaluateRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@RestController
@RequestMapping("/evaluate")
public class EvaluateRecordController extends BaseController{
    @Autowired
    private IEvaluateRecordService iEvaluateRecordService;

    @ApiOperation("新增儿童评估记录数据")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated EvaluateRecordRequest request){
        return AjaxResult.success(iEvaluateRecordService.add(request));
    }

    @ApiOperation(value = "儿童评估记录列表")
    @GetMapping("/child/list")
    public AjaxResult searchListByChildId(@NotNull(message = "儿童id不能为空") Long id){
        return AjaxResult.success(iEvaluateRecordService.searchListByChildId(id));
    }

    @ApiOperation(value = "评估记录详情信息")
    @GetMapping("/detail")
    public AjaxResult searchDetail(@NotNull(message = "记录id不能为空")Long id){
        return AjaxResult.success(iEvaluateRecordService.searchDetail(id));
    }

    @ApiOperation(value = "编辑评估记录")
    @PutMapping("/edit")
    public AjaxResult editDetail(@RequestBody @Validated EvaluateRecordEditRequest request){
        return toAjax(iEvaluateRecordService.editDetail(request));
    }

    @ApiOperation(value = "删除评估记录",notes = "在24小时内可以进行标记删除")
    @DeleteMapping("/delete")
    public AjaxResult delete(@NotNull(message = "记录id不能为空") Long id){
        return toAjax(iEvaluateRecordService.delete(id));
    }
}
