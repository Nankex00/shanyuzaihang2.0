package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.device.model.request.*;
import com.fushuhealth.recovery.device.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@RestController
@RequestMapping("/children")
public class ChildrenController extends BaseController {
    @Autowired
    private IChildrenService iChildrenService;
    @Autowired
    private IDangerLevelChangeRecordService iDangerLevelChangeDetailService;
    @Autowired
    private ITransferRecordService iTransferRecordService;
    @Autowired
    private IDiagnoseService iDiagnoseService;
    @Autowired
    private ISettleRecordService iSettleRecordService;
    @Autowired
    private IDiagnoseRecordService iDiagnoseRecordService;


    @GetMapping("/list")
    public AjaxResult list(@Validated ChildrenRequest request){
        return AjaxResult.success(iChildrenService.searchList(request));
    }

    @GetMapping("/searchDetail")
    public AjaxResult searchDetail(@NotNull(message = "id不能为空") Long id){
        return AjaxResult.success(iChildrenService.searchDetail(id));
    }

    /**
     * 更改高危等级
     * @param request
     * @return AjaxResult
     */
    @Operation(summary = "更改高危等级")
    @PostMapping("/changeLevel")
    public AjaxResult changeLevel(@RequestBody DangerLevelRequest request){
        return toAjax(iDangerLevelChangeDetailService.changeLevel(request));
    }

    /**
     * 高危等级更改记录列表
     * @param childId
     * @return
     */
    @Operation(summary = "高危等级更改记录")
    @GetMapping("/searchChangeLevelList")
    public AjaxResult searchChangeLevelList(@NotNull(message = "id不能为空") Long childId){
        return AjaxResult.success(iDangerLevelChangeDetailService.searchChangeLevelList(childId));
    }

    /**
     * 儿童转诊
     * @param request
     * @return
     */
    @Operation(summary = "儿童转诊操作")
    @PostMapping("/transferInstitution")
    public AjaxResult transferInstitution(@RequestBody TransferRequest request){
        return toAjax(iTransferRecordService.transferInstitution(request));
    }

    /**
     * 转诊记录列表
     * @return
     */
    @Operation(summary = "转诊记录列表")
    @GetMapping("/searchTransferRecord")
    public AjaxResult searchTransferRecord(@NotNull(message = "儿童id不能为空") Long childId){
        return AjaxResult.success(iTransferRecordService.searchTransferRecord(childId));
    }

    /**
     * 诊断类型列表
     * @return
     */
    @Operation(summary = "诊断类型类表")
    @GetMapping("/diagnoseList")
    public AjaxResult diagnoseList(){
        return AjaxResult.success(iDiagnoseService.list());
    }

    /**
     * 完成结案
     * @return AjaxResult
     */
    @Operation(summary = "完成结案")
    @PostMapping("/settleComplete")
    private AjaxResult settleDiagnose(@RequestBody SettleRequest request){
        return toAjax(iDiagnoseService.settleDiagnose(request));
    }

    /**
     * 结案记录列表
     */
    @Operation(summary = "结案记录列表")
    @GetMapping("/settleRecordList")
    private AjaxResult settleRecordList(@NotNull(message = "儿童id不能为空") @Parameter(name = "儿童id",required = true,example = "1") Long childId){
        return AjaxResult.success(iSettleRecordService.list(childId));
    }

    @Operation(summary = "点击诊断按钮获取最新诊断数据")
    @GetMapping("/searchDiagnoseRecord")
    private AjaxResult searchDiagnoseRecord(@NotNull(message = "儿童id不能为空")Long childId){
        return AjaxResult.success(iDiagnoseService.searchDiagnoseByChildId(childId));
    }

    @Operation(summary = "新增诊断数据")
    @PostMapping("/addDiagnoseRecord")
    private AjaxResult addDiagnoseRecord(@RequestBody @Validated DiagnoseRequest request){
        return toAjax(iDiagnoseService.addDiagnoseRecord(request));
    }

    @Operation(summary = "儿童诊断数据列表")
    @GetMapping("/diagnose/list")
    private AjaxResult searchDiagnoseRecordList(@NotNull Long childId){
        return AjaxResult.success(iDiagnoseRecordService.searchListByChildId(childId));
    }

    @Operation(summary = "高危儿管理列表")
    @GetMapping("/highRisk/list")
    private AjaxResult searchListHighRisk(@Validated HighRiskChildrenRequest request){
        return AjaxResult.success(iChildrenService.searchListHighRisk(request));
    }

}
