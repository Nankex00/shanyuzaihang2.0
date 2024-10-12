package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.vo.ActionVo;

import java.util.List;

public interface ActionService {

//    ListActionResponse queryActions(ActionQuery actionQuery, LoginVo loginVo);

    ActionVo getActionVo(long id);

//    void saveAction(SaveActionRequest request, LoginVo vo);
//
//    List<Long> getIdsLikeName(String name);
//
//    void updateAction(UpdateActionRequest request, LoginVo vo);
//
//    List<ActionAttributeVo> getActionAttribute();
}
