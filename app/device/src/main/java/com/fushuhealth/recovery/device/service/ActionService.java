package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.dto.ActionQuery;
import com.fushuhealth.recovery.dal.vo.ActionAttributeVo;
import com.fushuhealth.recovery.dal.vo.ActionVo;
import com.fushuhealth.recovery.device.model.request.SaveActionRequest;
import com.fushuhealth.recovery.device.model.request.UpdateActionRequest;
import com.fushuhealth.recovery.device.model.response.ListActionResponse;
import com.fushuhealth.recovery.device.model.vo.LoginVo;

import java.util.List;

public interface ActionService {

    ListActionResponse queryActions(ActionQuery actionQuery, LoginVo loginVo);

    ActionVo getActionVo(long id);

    void saveAction(SaveActionRequest request, LoginVo vo);
//
//    List<Long> getIdsLikeName(String name);

    void updateAction(UpdateActionRequest request, LoginVo vo);

    List<ActionAttributeVo> getActionAttribute();

//    List<Long> getActions();
}
