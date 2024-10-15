package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.dal.dto.ActionQuery;
import com.fushuhealth.recovery.dal.vo.ActionVo;
import com.fushuhealth.recovery.device.model.request.SaveActionRequest;
import com.fushuhealth.recovery.device.model.request.UpdateActionRequest;
import com.fushuhealth.recovery.device.model.response.ListActionResponse;
import com.fushuhealth.recovery.device.model.vo.LoginVo;
import com.fushuhealth.recovery.device.service.ActionService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/action")
public class ActionController {

    @Autowired
    private ActionService actionService;
    @Autowired
    private ISysUserService userService;

    @GetMapping("/list")
    public OldBaseResponse listActions(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                       @RequestParam(required = false) String word,
                                       @RequestParam(required = false, defaultValue = "0") Long tagId) {
        LoginVo loginVo = userService.getLoginVo();
        ActionQuery actionQuery = ActionQuery.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .word(word)
                .tagId(tagId).build();
        ListActionResponse listActionResponse = actionService.queryActions(actionQuery, loginVo);
        return OldBaseResponse.success(listActionResponse);
    }

    @GetMapping("/get")
    public OldBaseResponse getAction(@RequestParam Long id) {
        ActionVo actionVo = actionService.getActionVo(id);
        return OldBaseResponse.success(actionVo);
    }

    @PostMapping("/save")
    public OldBaseResponse saveAction(@RequestBody SaveActionRequest request) {
        LoginVo user = userService.getLoginVo();
        actionService.saveAction(request, user);
        return OldBaseResponse.success();
    }

    @PostMapping("/update")
    public OldBaseResponse updateAction(@RequestBody UpdateActionRequest request) {
        LoginVo user = userService.getLoginVo();
        actionService.updateAction(request, user);
        return OldBaseResponse.success();
    }

    @GetMapping("/attribute")
    public OldBaseResponse getActionAttribute() {
        return OldBaseResponse.success(actionService.getActionAttribute());
    }
}
