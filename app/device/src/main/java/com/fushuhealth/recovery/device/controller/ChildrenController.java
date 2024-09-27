package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.device.model.request.ChildrenRequest;
import com.fushuhealth.recovery.device.service.IChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@RestController
@RequestMapping("/children")
public class ChildrenController extends BaseController {
    @Autowired
    private IChildrenService iChildrenService;

    @GetMapping("/list")
    public AjaxResult list(ChildrenRequest request){
        return AjaxResult.success(iChildrenService.searchList(request));
    }
}
