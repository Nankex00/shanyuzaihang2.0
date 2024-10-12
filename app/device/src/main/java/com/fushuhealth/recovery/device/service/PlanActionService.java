package com.fushuhealth.recovery.device.service;


import com.fushuhealth.recovery.dal.entity.PlanAction;
import com.fushuhealth.recovery.dal.vo.PlanActionListVo;

import java.util.List;

public interface PlanActionService {

    List<PlanActionListVo> listPlanActionByPlanId(long planId);
//
//    void savePlanAction(PlanAction planAction);
//
//    void deletePlanAction(long planId);

    PlanAction getPlanActionByPlanIdAndActionId(long planId, long actionId);

//    List<SimplePlanActionListVo> getPlanActions(long planId);
}
