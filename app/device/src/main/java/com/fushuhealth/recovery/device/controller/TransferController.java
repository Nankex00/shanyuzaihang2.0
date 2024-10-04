package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateListRequest;
import com.fushuhealth.recovery.device.model.request.TransferRecordListRequest;
import com.fushuhealth.recovery.device.service.ITransferRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@RestController
public class TransferController extends BaseController {
    @Autowired
    private ITransferRecordService iTransferRecordService;
    @Operation(summary = "机构转诊记录列表")
    @GetMapping("/dept/transferRecordList")
    public AjaxResult searchDeptList(@Validated @Parameter TransferRecordListRequest request){
        return AjaxResult.success(iTransferRecordService.searchDeptList(request));
    }
}
