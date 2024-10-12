package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.dal.dao.TrainingActionRecordDao;
import com.fushuhealth.recovery.dal.entity.TrainingActionRecord;
import com.fushuhealth.recovery.device.service.TrainingActionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//package com.fushuhealth.recovery.device.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.fushuhealth.recovery.common.api.ResultCode;
//import com.fushuhealth.recovery.common.constant.BaseStatus;
//import com.fushuhealth.recovery.common.constant.TrainingStatus;
//import com.fushuhealth.recovery.common.constant.TrainingType;
//import com.fushuhealth.recovery.common.entity.PlanAction;
//import com.fushuhealth.recovery.common.entity.TrainingActionRecord;
//import com.fushuhealth.recovery.common.entity.TrainingRecord;
//import com.fushuhealth.recovery.common.entity.TrainingRecordResult;
//import com.fushuhealth.recovery.common.entity.dto.PositionFile;
//import com.fushuhealth.recovery.common.entity.vo.PositionFileVo;
//import com.fushuhealth.recovery.common.error.ServiceException;
//import com.fushuhealth.recovery.common.util.DateUtil;
//import com.fushuhealth.recovery.web.dao.TrainingActionRecordDao;
//import com.fushuhealth.recovery.web.dao.TrainingVideoRecordDao;
//import com.fushuhealth.recovery.web.model.response.ListTrainingActionRecordResponse;
//import com.fushuhealth.recovery.web.model.vo.ActionVo;
//import com.fushuhealth.recovery.web.model.vo.PageVo;
//import com.fushuhealth.recovery.web.model.vo.PlanActionListVo;
//import com.fushuhealth.recovery.web.model.vo.TrainingActionRecordListVo;
//import com.fushuhealth.recovery.web.model.vo.TrainingActionRecordListVoV2;
//import com.fushuhealth.recovery.web.model.vo.TrainingRecordVideoVo;
//import com.fushuhealth.recovery.web.model.vo.TrainingRecordVo;
//import com.fushuhealth.recovery.web.service.ActionService;
//import com.fushuhealth.recovery.web.service.FileService;
//import com.fushuhealth.recovery.web.service.PlanActionService;
//import com.fushuhealth.recovery.web.service.TrainingActionRecordService;
//import com.fushuhealth.recovery.web.service.TrainingRecordResultService;
//import com.fushuhealth.recovery.web.service.TrainingRecordService;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
@Service
public class TrainingActionRecordServiceImpl implements TrainingActionRecordService {
//
    @Autowired
    private TrainingActionRecordDao trainingActionRecordDao;
//
//    @Autowired
//    private TrainingVideoRecordDao trainingVideoRecordDao;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private ActionService actionService;
//
//    @Autowired
//    private ActionVideoServiceImpl actionVideoService;
//
//    @Autowired
//    private TrainingRecordService trainingRecordService;
//
//    @Autowired
//    private TrainingRecordResultService trainingRecordResultService;
//
//    @Autowired
//    private PlanActionService planActionService;
//
//
//    @Override
//    public ListTrainingActionRecordResponse listTrainingActionRecord(long userId, String word, int pageNo, int pageSize) {
//
//        Page<TrainingActionRecord> page = new Page<>(pageNo, pageSize);
//
//        QueryWrapper<TrainingActionRecord> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userId).eq("status", BaseStatus.NORMAL.getStatus());
//        if (!StringUtils.isBlank(word)) {
//            List<Long> list = actionService.getIdsLikeName(word);
//            queryWrapper.in("action_id", list);
//        }
//        queryWrapper.orderByDesc("start_time");
//
//        page = trainingActionRecordDao.selectPage(page, queryWrapper);
//
//        List<TrainingActionRecord> records = page.getRecords();
//        if (CollectionUtils.isEmpty(records)) {
//            return null;
//        }
//
//        List<TrainingActionRecordListVo> trainingRecordListVoList = records.stream().map(
//                trainingRecord -> convertToTrainingRecordListVo(trainingRecord)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent())
//                .totalPage(page.getPages())
//                .totalRecord(page.getTotal())
//                .build();
//        return new ListTrainingActionRecordResponse(pageVo, trainingRecordListVoList);
//    }
//
//    @Override
//    public List<TrainingRecordVideoVo> listTrainingRecordVideoVo(long recordId) {
//        TrainingActionRecord trainingActionRecord = trainingActionRecordDao.selectById(recordId);
//        if (trainingActionRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        String videos = trainingActionRecord.getVideos();
//        List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
//        List<TrainingRecordVideoVo> videoVos = new ArrayList<>();
//        for (PositionFile positionFile : positionFiles) {
//            TrainingRecordVideoVo vo = new TrainingRecordVideoVo();
//            vo.setPosition(positionFile.getPosition());
//            vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
//            videoVos.add(vo);
//        }
//        return videoVos;
//    }
//
    @Override
    public List<TrainingActionRecord> listTrainingActionRecordByRecordId(long recordId) {
        QueryWrapper<TrainingActionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
        wrapper.eq("record_id", recordId);
        wrapper.orderByDesc("id");
        return trainingActionRecordDao.selectList(wrapper);
    }
//
//    @Override
//    public TrainingActionRecord getById(long id) {
//        return trainingActionRecordDao.selectById(id);
//    }
//
//    @Override
//    public void update(TrainingActionRecord trainingActionRecord) {
//        trainingActionRecordDao.updateById(trainingActionRecord);
//    }
//
//    @Override
//    public void saveTrainingAction(TrainingActionRecord trainingActionRecord) {
//        trainingActionRecordDao.insert(trainingActionRecord);
//    }
//
//    @Override
//    public List<TrainingActionRecordListVoV2> listByPlanIdAndDay(long planId, long patientId, long day) {
//        List<TrainingRecord> trainingRecords = trainingRecordService.listByPlanIdAndDay(planId, patientId, day);
//        if (CollectionUtils.isEmpty(trainingRecords)) {
//            return new ArrayList<TrainingActionRecordListVoV2>();
//        }
//        List<Long> trainingRecordIds = trainingRecords.stream().map(TrainingRecord::getId).distinct().collect(Collectors.toList());
//
//        QueryWrapper<TrainingActionRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.in("record_id", trainingRecordIds);
//        wrapper.orderByDesc("id");
//        List<TrainingActionRecord> trainingActionRecords = trainingActionRecordDao.selectList(wrapper);
//        List<TrainingActionRecordListVoV2> collect = trainingActionRecords.stream().map(record -> convertToTrainingActionRecordListVoV2(record)).collect(Collectors.toList());
//        return collect;
//    }
//
//    @Override
//    public List<TrainingActionRecordListVoV2> listByRecordId(TrainingRecord record) {
//
//        List<TrainingActionRecord> trainingActionRecords = listTrainingActionRecordByRecordId(record.getId());
//        List<PlanActionListVo> planActionListVos = planActionService.listPlanActionByPlanId(record.getPlanId());
//        ArrayList<TrainingActionRecordListVoV2> list = new ArrayList<>();
//        for (PlanActionListVo planActionListVo : planActionListVos) {
//            TrainingActionRecordListVoV2 vo = new TrainingActionRecordListVoV2();
//            ActionVo actionVo = actionService.getActionVo(planActionListVo.getActionId());
//            TrainingActionRecord trainingActionRecord = getTrainingActionRecord(trainingActionRecords, planActionListVo);
//            vo.setActionName(actionVo.getActionName());
//            vo.setActionId(actionVo.getId());
//            vo.setResultPage(actionVo.getResultPage());
//            vo.setPlanCount(planActionListVo.getCycles());
//
//            if (trainingActionRecord != null) {
//                Long cycle = (trainingActionRecord.getDuration() + 1) / Math.max(planActionListVo.getVideoDuration(), planActionListVo.getEveryDuration());
//                vo.setFinishCount(cycle.intValue());
//                vo.setId(trainingActionRecord.getId());
//
//                String videos = trainingActionRecord.getVideos();
//                List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
//                if (CollectionUtils.isNotEmpty(positionFiles) && positionFiles.size() > 1) {
//                    PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//                    if (positionFile != null) {
//                        Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);
//                    }
//                }
//                List<PositionFileVo> collect = positionFiles.stream().map(pf -> convertToPositionFileVo(pf)).collect(Collectors.toList());
//                vo.setVideos(collect);
//                if (trainingActionRecord.getCoverFileId() == 0) {
//                    vo.setCoverUrl(actionVo.getCoverUrl());
//                } else {
//                    vo.setCoverUrl(fileService.getFileUrl(trainingActionRecord.getCoverFileId(), false));
//                }
//
//            } else {
//                vo.setFinishCount(0);
//                vo.setVideos(new ArrayList<>());
//                vo.setId(0l);
//                vo.setCoverUrl(actionVo.getCoverUrl());
//            }
//            list.add(vo);
//        }
//        return list;
//    }
//
//    private TrainingActionRecord getTrainingActionRecord(List<TrainingActionRecord> trainingActionRecords, PlanActionListVo planActionListVo) {
//        for (TrainingActionRecord trainingActionRecord : trainingActionRecords) {
//            if (trainingActionRecord.getActionId().equals(planActionListVo.getActionId())) {
//                return trainingActionRecord;
//            }
//        }
//        return null;
//    }
//
////    @Override
////    public TrainingRecordVo getTrainingRecord(long id) {
////        TrainingActionRecord trainingActionRecord = trainingRecordDao.selectById(id);
////        if (trainingActionRecord == null) {
////            throw new ServiceException(ResultCode.PARAM_ERROR);
////        }
////        return convertToTrainingRecordVo(trainingActionRecord);
////    }
//
//    private TrainingActionRecordListVo convertToTrainingRecordListVo(TrainingActionRecord tr) {
//        ActionVo actionVo = actionService.getActionVo(tr.getActionId());
//        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(tr.getRecordId());
//        return TrainingActionRecordListVo.builder()
//                .startTime(DateUtil.getYMDHMSDate(tr.getStartTime()))
//                .duration(DateUtil.secondToHMS(tr.getDuration()))
//                .id(tr.getId())
//                .actionName(actionVo.getActionName())
//                .actionId(actionVo.getId())
//                .trainingStatus(TrainingStatus.getStatus(tr.getTrainingStatus()).getDesc())
//                .resultPage(actionVo.getResultPage())
//                .planName(trainingRecordVo.getPlanName())
//                .trainingType(TrainingType.getType(trainingRecordVo.getTrainingType()).getDesc())
//                .build();
//    }
//
//    private TrainingActionRecordListVoV2 convertToTrainingActionRecordListVoV2(TrainingActionRecord trainingActionRecord) {
//        TrainingActionRecordListVoV2 vo = new TrainingActionRecordListVoV2();
//        ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//        String videos = trainingActionRecord.getVideos();
//        List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
////        PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
////
////        Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);
//        if (CollectionUtils.isNotEmpty(positionFiles) && positionFiles.size() > 1) {
//            PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//            if (positionFile != null) {
//                Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);
//            }
//        }
//        List<PositionFileVo> collect = positionFiles.stream().map(pf -> convertToPositionFileVo(pf)).collect(Collectors.toList());
////        List<TrainingRecordVideoVo> videoVos = new ArrayList<>();
////        for (PositionFile positionFile : positionFiles) {
////            TrainingRecordVideoVo trainingRecordVideoVo = new TrainingRecordVideoVo();
////            trainingRecordVideoVo.setPosition(positionFile.getPosition());
////            trainingRecordVideoVo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
////            videoVos.add(trainingRecordVideoVo);
////        }
//
//        vo.setActionId(actionVo.getId());
//        vo.setActionName(actionVo.getActionName());
//        vo.setResultPage(actionVo.getResultPage());
//        String coverUrl = "";
//        if (trainingActionRecord.getCoverFileId() == 0) {
//            coverUrl = actionVo.getCoverUrl();
//        } else {
//            coverUrl = fileService.getFileUrl(trainingActionRecord.getCoverFileId(), false);
//        }
//        vo.setCoverUrl(coverUrl);
//        //TODO FIX FINISH COUNT
////        List<TrainingRecordResult> trainingRecordResults = trainingRecordResultService.listResultByActionRecordId(trainingActionRecord.getId());
////        if (CollectionUtils.isEmpty(trainingRecordResults)) {
////            vo.setFinishCount(0);
////        } else {
////            TrainingRecordResult result = trainingRecordResults.get(0);
////            vo.setFinishCount(result.getFinishCount());
////        }
//        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(trainingActionRecord.getRecordId());
//        PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(trainingRecordVo.getPlanId(), trainingActionRecord.getActionId());
//        if (planAction != null) {
//            vo.setPlanCount(planAction.getCycles());
//            Long cycle = (trainingActionRecord.getDuration() + 1) / Math.max(planAction.getVideoDuration(), planAction.getEveryDuration());
//            vo.setFinishCount(cycle.intValue());
//        } else {
//            vo.setPlanCount(0);
//            vo.setFinishCount(0);
//        }
//        vo.setId(trainingActionRecord.getId());
//        vo.setVideos(collect);
//        return vo;
//    }
//
//    private PositionFileVo convertToPositionFileVo(PositionFile positionFile) {
//        PositionFileVo vo = new PositionFileVo();
//        vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
//        vo.setPosition(positionFile.getPosition());
//        return vo;
//    }
//
////    private TrainingRecordVo convertToTrainingRecordVo(TrainingActionRecord tr) {
////        if (tr == null) {
////            return null;
////        }
////        ActionVo actionVo = actionService.getActionVo(tr.getActionId());
////        String videos = tr.getVideos();
////        List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
////        List<TrainingRecordVideoVo> videoVos = new ArrayList<TrainingRecordVideoVo>();
////        for (PositionFile positionFile : positionFiles) {
////            TrainingRecordVideoVo vo = new TrainingRecordVideoVo();
////            vo.setPosition(positionFile.getPosition());
////            vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
////            videoVos.add(vo);
////        }
////        return TrainingRecordVo.builder()
////                .created(DateUtil.getYMDHMSDate(tr.getStartTime()))
////                .duration(DateUtil.secondToHMS(tr.getDuration()))
////                .id(tr.getId())
////                .name(actionVo.getActionName())
////                .equipment(actionVo.getEquipment())
////                .instrument(actionVo.getInstrument())
////                .trainingStatusCode(tr.getTrainingStatus())
////                .trainingStatus(TrainingStatus.getStatus(tr.getTrainingStatus()).getDesc())
////                .videos(videoVos)
////                .build();
////    }
////
////    private String getActions(long recordId) {
////        TrainingVideoRecord tvr = new TrainingVideoRecord();
////        tvr.setTrainingRecordId(recordId);
////        tvr.setStatus(BaseStatus.NORMAL.getStatus());
////        QueryWrapper<TrainingVideoRecord> queryWrapper = new QueryWrapper<>(tvr);
////        List<TrainingVideoRecord> trainingVideoRecords = trainingVideoRecordDao.selectList(queryWrapper);
////        if (CollectionUtils.isEmpty(trainingVideoRecords)) {
////            return "";
////        }
////        return trainingVideoRecords.stream().map(TrainingVideoRecord::getName).collect(Collectors.joining(","));
////    }
//
//
}
