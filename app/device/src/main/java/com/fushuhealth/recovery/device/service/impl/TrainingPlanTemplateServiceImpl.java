package com.fushuhealth.recovery.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.dal.dao.TrainingPlanTemplateDao;
import com.fushuhealth.recovery.dal.dto.PositionFile;
import com.fushuhealth.recovery.dal.entity.ActionVideo;
import com.fushuhealth.recovery.dal.entity.TrainingPlanTemplate;
import com.fushuhealth.recovery.dal.vo.*;
import com.fushuhealth.recovery.device.model.request.SavePlanActionRequest;
import com.fushuhealth.recovery.device.model.request.SavePlanTemplateRequest;
import com.fushuhealth.recovery.device.model.request.UpdateTrainingPlanTemplateRequest;
import com.fushuhealth.recovery.device.model.response.ListTrainingPlanTemplateResponse;
import com.fushuhealth.recovery.device.model.vo.PageVo;
import com.fushuhealth.recovery.device.service.ActionService;
import com.fushuhealth.recovery.device.service.ActionVideoService;
import com.fushuhealth.recovery.device.service.FileService;
import com.fushuhealth.recovery.device.service.TrainingPlanTemplateService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingPlanTemplateServiceImpl implements TrainingPlanTemplateService {

    @Autowired
    private TrainingPlanTemplateDao trainingPlanTemplateDao;

    @Autowired
    private ActionService actionService;

    @Autowired
    private ActionVideoService actionVideoService;

    @Autowired
    private FileService fileService;

    @Override
    public ListTrainingPlanTemplateResponse listTemplate(int pageNo, int pageSize, long userId, String name) {
        Page<TrainingPlanTemplate> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<TrainingPlanTemplate> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus()).eq("user_id", userId);
//        if (!StringUtils.isBlank(name)) {
//            wrapper.like("name", name);
//        }
//        wrapper.orderByDesc("id");
        MPJLambdaWrapper<TrainingPlanTemplate> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(TrainingPlanTemplate.class)
                .eq(TrainingPlanTemplate::getStatus,BaseStatus.NORMAL.getStatus())
                .eq(TrainingPlanTemplate::getUserId,userId);
        if (!StringUtils.isBlank(name)&& !name.isEmpty()) {
            lambdaQueryWrapper.like(TrainingPlanTemplate::getName, name);
        }
        lambdaQueryWrapper.orderByDesc(TrainingPlanTemplate::getId);
        page = trainingPlanTemplateDao.selectJoinPage(page,TrainingPlanTemplate.class,lambdaQueryWrapper);
        List<TrainingPlanTemplate> records = page.getRecords();
        List<TrainingPlanTemplateListVo> list = records.stream()
                .map(template -> convertTrainingPlanTemplateListVo(template)).collect(Collectors.toList());
        PageVo pageVo = PageVo.builder()
                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
        return new ListTrainingPlanTemplateResponse(pageVo, list);
    }

    @Override
    public void saveTrainingPlanTemplate(long userId, SavePlanTemplateRequest request) {
        TrainingPlanTemplate template = new TrainingPlanTemplate();
        List<SavePlanActionRequest> actions = request.getActions();
        template.setActionCount(actions.size());
        template.setActions(JSON.toJSONString(actions));
        template.setCreated(DateUtil.getCurrentTimeStamp());
        template.setName(request.getName());
        template.setStatus(BaseStatus.NORMAL.getStatus());
        template.setUpdated(DateUtil.getCurrentTimeStamp());
        template.setUserId(userId);
        trainingPlanTemplateDao.insert(template);
    }

    @Override
    public void deleteTrainingPlanTemplate(long id) {
        TrainingPlanTemplate template = trainingPlanTemplateDao.selectById(id);
        template.setStatus(BaseStatus.DELETE.getStatus());
        trainingPlanTemplateDao.updateById(template);
    }

    @Override
    public TrainingPlanTemplateVo getTrainingPlanTemplateVo(long id) {
        TrainingPlanTemplate template = trainingPlanTemplateDao.selectById(id);
        return convertTrainingPlanTemplateVo(template);
    }

    @Override
    public void updateTrainingPlanTemplate(UpdateTrainingPlanTemplateRequest request) {
        TrainingPlanTemplate template = trainingPlanTemplateDao.selectById(request.getId());
        List<SavePlanActionRequest> actions = request.getActions();
        template.setName(request.getName());
        template.setActionCount(actions.size());
        template.setActions(JSON.toJSONString(actions));
        trainingPlanTemplateDao.updateById(template);
    }

    @Override
    public TrainingPlanTemplate getTrainingPlanTemplate(long id) {
        return trainingPlanTemplateDao.selectById(id);
    }

    private TrainingPlanTemplateListVo convertTrainingPlanTemplateListVo(TrainingPlanTemplate trainingPlanTemplate) {
        TrainingPlanTemplateListVo vo = new TrainingPlanTemplateListVo();
        List<SavePlanActionRequest> array = JSON.parseArray(trainingPlanTemplate.getActions(), SavePlanActionRequest.class);
        if (CollectionUtils.isEmpty(array)) {
            vo.setActionCount(0);
        } else {
            vo.setActionCount(array.size());
//            try {
//                String name = array.stream().map(request -> actionService.getActionVo(request.getActionId()).getActionName())
//                        .collect(Collectors.joining(","));
//                vo.setActions(name);
//            } catch (Exception e) {
//                vo.setActions("");
//            }
        }
        vo.setCreated(DateUtil.getYMDHMSDate(trainingPlanTemplate.getCreated()));
        vo.setId(trainingPlanTemplate.getId());
        vo.setName(trainingPlanTemplate.getName());

//        List<SavePlanActionRequest> collect = array.stream().sorted(Comparator.comparing(SavePlanActionRequest::getOrder)).collect(Collectors.toList());
        List<TrainingPlanTemplateActionVo> collect = array.stream().map(request -> convertTrainingPlanTemplateActionVo(request)).collect(Collectors.toList());
        vo.setActionList(collect);
        vo.setCoverUrl(collect.get(0).getCoverUrl());
        return vo;
    }

    private TrainingPlanTemplateVo convertTrainingPlanTemplateVo(TrainingPlanTemplate template) {
        TrainingPlanTemplateVo vo = new TrainingPlanTemplateVo();
        List<SavePlanActionRequest> array = JSON.parseArray(template.getActions(), SavePlanActionRequest.class);
        vo.setName(template.getName());
        vo.setActions(array.stream().map(request -> convertTrainingPlanTemplateActionVo(request)).collect(Collectors.toList()));
        vo.setId(template.getId());
        return vo;
    }

    private TrainingPlanTemplateActionVo convertTrainingPlanTemplateActionVo(SavePlanActionRequest request) {
        TrainingPlanTemplateActionVo vo = new TrainingPlanTemplateActionVo();
        ActionVo actionVo = actionService.getActionVo(request.getActionId());
        ActionVideo actionVideo = actionVideoService.getById(request.getActionVideoId());

        String videos = actionVideo.getVideos();
        List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
//        PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//        Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);

        if (CollectionUtils.isNotEmpty(positionFiles)) {
            if (positionFiles.size() > 1) {
                PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
                if (positionFile != null) {
                    vo.setVideoUrl(fileService.getFileUrl(positionFile.getFileId(), false));
                    Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);
                }
            }
        } else {
            PositionFile positionFile = positionFiles.get(0);
            vo.setVideoUrl(fileService.getFileUrl(positionFile.getFileId(), false));
        }

        List<PositionFileVo> collect = positionFiles.stream().map(pf -> convertToPositionFileVo(pf)).collect(Collectors.toList());
        vo.setVideos(collect);
        vo.setActionId(request.getActionId());
        vo.setActionVideoId(request.getActionVideoId());
        vo.setCoverUrl(fileService.getFileUrl(actionVideo.getCoverFileId(), false));
//        vo.setCheck(request.getCheck());
        vo.setCycles(request.getCycles());
        vo.setEveryDuration(request.getEveryDuration());
//        if (request.getSort() == null) {
//            vo.setSort(0);
//        } else {
//            vo.setSort(request.getSort());
//        }
        if (request.getRestTime() == null) {
            vo.setRestTime(30);
        } else {
            vo.setRestTime(request.getRestTime());
        }
        vo.setActionName(actionVo.getActionName());
        return vo;
    }
//
    private PositionFileVo convertToPositionFileVo(PositionFile positionFile) {
        PositionFileVo vo = new PositionFileVo();
        vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
        vo.setPosition(positionFile.getPosition());
        return vo;
    }
}
