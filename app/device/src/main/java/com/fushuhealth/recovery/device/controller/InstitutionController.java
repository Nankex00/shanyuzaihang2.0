package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.model.request.MyDeptRequest;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private ISysUserService iSysUserService;

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('institution:subordinate:list')")
    public AjaxResult list(@Validated InstitutionRequest request){
        return AjaxResult.success(iSysDeptService.list(request));
    }

    @PostMapping("/add")
//    @PreAuthorize("@ss.hasPermi('institution:subordinate:add')")
    public AjaxResult add(@Validated @RequestBody SysDeptBo bo){
        return toAjax(iSysDeptService.createDept(bo));
    }

    @DeleteMapping("/delete")
//    @PreAuthorize("@ss.hasPermi('institution:subordinate:delete')")
    public AjaxResult delete(Long userId){
        return toAjax(iSysDeptService.deleteDept(userId));
    }

    @PutMapping("/edit")
//    @PreAuthorize("@ss.hasPermi('institution:subordinate:edit')")
    public AjaxResult update(@Validated @RequestBody SysDeptBo bo){
        return toAjax(iSysDeptService.updateDept(bo));
    }

    @GetMapping("/searchDetail")
//    @PreAuthorize("@ss.hasPermi('institution:subordinate:detail')")
    public AjaxResult searchDetail(Long id){
        return AjaxResult.success(iSysDeptService.searchDetail(id));
    }

    @GetMapping("/searchMyDetail")
//    @PreAuthorize("@ss.hasPermi('institution:info:detail')")
    public AjaxResult searchMyDetail(){
        return AjaxResult.success(iSysDeptService.searchDetail(SecurityUtils.getUserId()));
    }


    @GetMapping("/isUserExist")
    public AjaxResult isUserExist(String userName){
        return iSysUserService.isUserExist(userName)?AjaxResult.success("用户名可用"):AjaxResult.error("用户名已存在");
    }

    @GetMapping("/searchAncestorsDept")
    public AjaxResult searchAncestorsDept(Long deptId){
        return AjaxResult.success(iSysDeptService.searchAncestorsDeptByDeptId(deptId));
    }

    /**
     * 修改本机构信息
     * @param bo
     * @return
     */
    @PutMapping("/editMyInstitution")
    public AjaxResult editMyInstitution(@Validated @RequestBody MyDeptRequest bo){
        return toAjax(iSysDeptService.editMyInstitution(bo));
    }
}
