package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.dal.vo.ActionVideoVo;
import com.fushuhealth.recovery.device.model.request.SaveActionVideoRequest;
import com.fushuhealth.recovery.device.model.request.UpdateActionVideoRequest;
import com.fushuhealth.recovery.device.model.response.ListActionVideoResponse;
import com.fushuhealth.recovery.device.model.vo.LoginVo;
import com.fushuhealth.recovery.device.service.ActionVideoService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actionVideo")
public class ActionVideoController {

    @Autowired
    private ActionVideoService actionVideoService;
    @Autowired
    private ISysUserService userService;

    @GetMapping("/list")
    public OldBaseResponse listByActionId(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                       @RequestParam(value = "actionId", defaultValue = "0")Long actionId,
                                       @RequestParam(required = false, defaultValue = "") String actionName,
                                       @RequestParam(required = false, defaultValue = "0") Byte isPublic) {
        LoginVo user = userService.getLoginVo();
        ListActionVideoResponse list = actionVideoService.listActionVideo(pageNo, pageSize, actionId, actionName, isPublic, user);
        return OldBaseResponse.success(list);
    }

    @GetMapping("/get")
    public OldBaseResponse getActionVideo(@RequestParam long id) {
        ActionVideoVo actionVideoVo = actionVideoService.getActionVideoVo(id);
        return OldBaseResponse.success(actionVideoVo);
    }

    @GetMapping("/changePublic")
    public OldBaseResponse changeActionVideoPublic(@RequestParam long id,
                                                @RequestParam byte isPublic) {
        actionVideoService.changeActionVideoPublic(id, isPublic);
        return OldBaseResponse.success();
    }

    @PostMapping("/save")
    public OldBaseResponse saveActionVideo(@RequestBody SaveActionVideoRequest request) {
        LoginVo user = userService.getLoginVo();
        actionVideoService.saveActionVideo(request, user);
        return OldBaseResponse.success();
    }

    @PostMapping("/update")
    public OldBaseResponse updateActionVideo(@RequestBody UpdateActionVideoRequest request) {
        LoginVo user = userService.getLoginVo();
        actionVideoService.updateActionVideo(request, user);
        return OldBaseResponse.success();
    }

    @GetMapping("/delete")
    public OldBaseResponse deleteActionVideo(@RequestParam long id) {
        actionVideoService.deleteActionVideo(id);
        return OldBaseResponse.success();
    }
}
