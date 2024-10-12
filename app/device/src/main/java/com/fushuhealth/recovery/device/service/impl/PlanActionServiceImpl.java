package com.fushuhealth.recovery.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.dal.dao.PlanActionDao;
import com.fushuhealth.recovery.dal.dto.PositionFile;
import com.fushuhealth.recovery.dal.entity.ActionVideo;
import com.fushuhealth.recovery.dal.entity.PlanAction;
import com.fushuhealth.recovery.dal.vo.ActionVo;
import com.fushuhealth.recovery.dal.vo.PlanActionListVo;
import com.fushuhealth.recovery.dal.vo.PositionFileVo;
import com.fushuhealth.recovery.device.service.ActionService;
import com.fushuhealth.recovery.device.service.ActionVideoService;
import com.fushuhealth.recovery.device.service.FileService;
import com.fushuhealth.recovery.device.service.PlanActionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanActionServiceImpl implements PlanActionService {

    @Autowired
    private PlanActionDao planActionDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private ActionVideoService actionVideoService;

    @Override
    public List<PlanActionListVo> listPlanActionByPlanId(long planId) {
        QueryWrapper<PlanAction> wrapper = new QueryWrapper<>();
        wrapper.eq("plan_id", planId);
        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
        wrapper.orderByAsc("id");
        List<PlanAction> planActions = planActionDao.selectList(wrapper);
        List<PlanActionListVo> list = planActions.stream().map(
                planAction -> convertToPlanActionListVo(planAction)).collect(Collectors.toList());
        return list;
    }

//    @Override
//    public void savePlanAction(PlanAction planAction) {
//        planActionDao.insert(planAction);
//    }
//
//    @Override
//    public void deletePlanAction(long planId) {
//        UpdateWrapper<PlanAction> wrapper = new UpdateWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus()).eq("plan_id", planId);
//        PlanAction planAction = new PlanAction();
//        planAction.setStatus(BaseStatus.DELETE.getStatus());
//        planActionDao.update(planAction, wrapper);
//    }

    @Override
    public PlanAction getPlanActionByPlanIdAndActionId(long planId, long actionId) {
        QueryWrapper<PlanAction> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
        wrapper.eq("plan_id", planId);
        wrapper.eq("action_id", actionId);
        return planActionDao.selectOne(wrapper);
    }

//    @Override
//    public List<SimplePlanActionListVo> getPlanActions(long planId) {
//        QueryWrapper<PlanAction> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("plan_id", planId);
//        List<PlanAction> planActions = planActionDao.selectList(wrapper);
//        List<SimplePlanActionListVo> collect = planActions.stream()
//                .map(planAction -> convertToSimplePlanActionListVo(planAction)).collect(Collectors.toList());
//        return collect;
//    }
//
    private PlanActionListVo convertToPlanActionListVo(PlanAction planAction) {
        PlanActionListVo vo = new PlanActionListVo();
        ActionVo actionVo = actionService.getActionVo(planAction.getActionId());
        ActionVideo actionVideo = actionVideoService.getById(planAction.getActionVideoId());
        vo.setActionCoverUrl(fileService.getFileUrl(planAction.getActionCoverFileId(), false));
        vo.setActionName(planAction.getActionName());
        vo.setVideoDuration(planAction.getVideoDuration());

        List<PositionFile> positionFiles = JSON.parseArray(actionVideo.getVideos(), PositionFile.class);
//        PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//
//        Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);

        if (CollectionUtils.isNotEmpty(positionFiles) && positionFiles.size() > 1) {
            PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
            if (positionFile != null) {
                Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);
                vo.setActionVideoUrl(fileService.getFileUrl(positionFile.getFileId(), false));
            } else {
                vo.setActionVideoUrl("");
            }
        }
        List<PositionFileVo> collect = positionFiles.stream().map(pf -> convertToPositionFileVo(pf)).collect(Collectors.toList());
        vo.setVideos(collect);
        vo.setCycles(planAction.getCycles());
        vo.setEveryDuration(planAction.getEveryDuration());
        vo.setId(planAction.getId());
        vo.setActionId(planAction.getActionId());
        vo.setCheck(planAction.getCheckEnable() == 0 ? false : true);
        //TODO FIX
        vo.setOrder(0);
        vo.setRestTime(planAction.getRestTime());
        return vo;
    }

    private PositionFileVo convertToPositionFileVo(PositionFile positionFile) {
        PositionFileVo vo = new PositionFileVo();
        vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
        vo.setPosition(positionFile.getPosition());
        return vo;
    }
//
//    private SimplePlanActionListVo convertToSimplePlanActionListVo(PlanAction planAction) {
//        SimplePlanActionListVo vo = new SimplePlanActionListVo();
//        ActionVo actionVo = actionService.getActionVo(planAction.getActionId());
//        vo.setActionId(actionVo.getId());
//        vo.setActionName(actionVo.getActionName());
//        return vo;
//    }
}
