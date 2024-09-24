package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@RestController
@RequestMapping("/sysDept")
public class InstitutionController extends BaseController {
    @Autowired
    private ISysDeptService iSysDeptService;

    @GetMapping("/list")
    public AjaxResult list(@Validated InstitutionRequest request){
        return AjaxResult.success(iSysDeptService.list(request));
    }

    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDeptBo bo){
        return toAjax(iSysDeptService.createDept(bo));
    }

    @DeleteMapping("/delete")
    public AjaxResult delete(Long id,Long userId){
        return toAjax(iSysDeptService.deleteDept(id,userId));
    }

    @PutMapping("/edit")
    public AjaxResult update(@Validated @RequestBody SysDeptBo bo){
        return toAjax(iSysDeptService.updateDept(bo));
    }

    @GetMapping("/searchDetail")
    public AjaxResult searchDetail(Long id){
        //todo:对于是否返回密码原文存疑
        return AjaxResult.success(iSysDeptService.searchDetail(id));
    }
}
