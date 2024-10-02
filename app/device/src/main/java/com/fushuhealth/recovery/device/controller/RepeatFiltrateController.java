package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.dal.dao.RepeatFiltrateRecordMapper;
import com.fushuhealth.recovery.dal.entity.RepeatFiltrateRecord;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateRecordRequest;
import com.fushuhealth.recovery.device.service.IRepeatFiltrateRecordService;
import io.swagger.annotations.Api;
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
@RequestMapping("/repeatFiltrate")
public class RepeatFiltrateController extends BaseController {
    @Autowired
    private IRepeatFiltrateRecordService iRepeatFiltrateRecordService;


    @ApiOperation(value = "儿童复筛记录列表")
    @GetMapping("/list")
    public AjaxResult list(@NotNull(message = "儿童id不能为空") Long id){
        return AjaxResult.success(iRepeatFiltrateRecordService.searchListByChildId(id));
    }

    @ApiOperation(value = "新增复筛记录")
    @PostMapping("/addRepeatFiltrateRecord")
    public AjaxResult addRepeatFiltrateRecord(@RequestBody @Validated RepeatFiltrateRecordRequest request){
        return toAjax(iRepeatFiltrateRecordService.addRepeatFiltrateRecord(request));
    }

    @ApiOperation(value = "复筛记录详情信息")
    @GetMapping("/detail")
    public AjaxResult searchDetail(@NotNull(message = "记录id不能为空")Long id){
        return AjaxResult.success(iRepeatFiltrateRecordService.searchDetail(id));
    }

    @ApiOperation(value = "编辑复筛记录")
    @PutMapping("/edit")
    public AjaxResult editDetail(@RequestBody @Validated RepeatFiltrateEditRequest request){
        return toAjax(iRepeatFiltrateRecordService.editDetail(request));
    }

    @ApiOperation(value = "删除复筛记录",notes = "在24小时内可以进行标记删除")
    @DeleteMapping("/delete")
    public AjaxResult delete(@NotNull(message = "记录id不能为空") Long id){
        return toAjax(iRepeatFiltrateRecordService.delete(id));
    }
}
