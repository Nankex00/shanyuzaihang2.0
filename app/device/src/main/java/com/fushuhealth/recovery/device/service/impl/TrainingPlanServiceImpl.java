package com.fushuhealth.recovery.device.service.impl;

import com.fushuhealth.recovery.common.constant.TrainingPlanStatus;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.dal.dao.TrainingPlanDao;
import com.fushuhealth.recovery.dal.entity.TrainingPlan;
import com.fushuhealth.recovery.dal.vo.PlanActionListVo;
import com.fushuhealth.recovery.dal.vo.TrainingPlanVo;
import com.fushuhealth.recovery.device.service.PlanActionService;
import com.fushuhealth.recovery.device.service.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//package com.fushuhealth.recovery.device.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.fushuhealth.recovery.common.api.ResultCode;
//import com.fushuhealth.recovery.common.constant.BaseStatus;
//import com.fushuhealth.recovery.common.util.DateUtil;
//import com.fushuhealth.recovery.common.util.StringUtil;
//import com.fushuhealth.recovery.device.service.TrainingPlanService;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {
//
//    private final static Logger log = LoggerFactory.getLogger(TrainingPlanServiceImpl.class);
//
    @Autowired
    private TrainingPlanDao trainingPlanDao;
//
//    @Autowired
//    private PlanVideoService planVideoService;
//
//    @Autowired
//    private VideoService videoService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private UserService userService;
//
    @Autowired
    private PlanActionService planActionService;
//
//    @Autowired
//    private ActionService actionService;
//
//    @Autowired
//    private ActionVideoService actionVideoService;
//
//    @Autowired
//    private TrainingPlanTemplateService trainingPlanTemplateService;
//
//    @Autowired
//    private TrainingRecordService trainingRecordService;
//
//    @Override
//    public ListTrainingPlanResponse getPlansByPatient(TrainingPlanQuery query, boolean selfOnly) {
//
//        Page<TrainingPlan> page = new Page<>(query.getPageNo(), query.getPageSize());
//        QueryWrapper<TrainingPlan> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        if (query.getDoctorId() != 0) {
//            wrapper.eq("user_id", query.getDoctorId());
//        }
//        if (query.getPatientId() != 0) {
//            wrapper.eq("patient_id", query.getPatientId());
//        }
//        if (!StringUtils.isBlank(query.getPlanName())) {
//            wrapper.like("plan_name", query.getPlanName());
//        }
//        if (query.getTrainingStatus() != 0) {
//            wrapper.like("training_status", query.getTrainingStatus());
//        }
//        wrapper.orderByDesc("id");
//
//        page = trainingPlanDao.selectPage(page, wrapper);
//        List<TrainingPlan> records = page.getRecords();
//
//        List<TrainingPlanListVo> list = records.stream()
//                .map(trainingPlan -> convertToTrainingPlanListVo(trainingPlan)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//
//        return new ListTrainingPlanResponse(pageVo, list);
//    }
//
//    @Transactional
//    @Override
//    public void saveTrainingPlan(long userId, SaveTrainingPlanRequest request) {
//
//        log.info("save training plan request : {}", JSON.toJSONString(request));
//
//        TrainingPlanTemplate trainingPlanTemplate = trainingPlanTemplateService.getTrainingPlanTemplate(request.getPlanTemplateId());
//        String actionStr = trainingPlanTemplate.getActions();
//        List<SavePlanActionRequest> actions = JSON.parseArray(actionStr, SavePlanActionRequest.class);
//
//        if (CollectionUtils.isEmpty(actions)) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
////        List<ActionVo> actionList = actions.stream().map(action -> actionService.getActionVo(action.getActionId())).collect(Collectors.toList());
//        ActionVo firstAction = actionService.getActionVo(actions.get(0).getActionId());
//        long endTime = 0l;
//        if (request.getUnit() == 1) {
//            endTime = DateUtil.getTimeAfterDay(request.getStartTime(), request.getNum());
//        } else if (request.getUnit() == 2) {
//            endTime = DateUtil.getTimeAfterWeek(request.getStartTime(), request.getNum());
//        }
//
//        long startTime = DateUtil.getDateTimeStamp(request.getStartTime());
//        List<Long> trainingDays = DateUtil.getDaysBetweenTwoDayInWeek(startTime, endTime, request.getFrequency());
//
//        List<Long> patientIds = request.getUserId();
//        for (Long patientId : patientIds) {
//            TrainingPlan trainingPlan = new TrainingPlan();
//            trainingPlan.setBatch(StringUtil.uuid());
//            trainingPlan.setNum(request.getNum());
//            trainingPlan.setUnit(request.getUnit());
//            trainingPlan.setCreated(DateUtil.getCurrentTimeStamp());
//            trainingPlan.setDays(trainingDays.size());
//            trainingPlan.setEndTime(endTime);
//            trainingPlan.setPatientId(patientId);
//            trainingPlan.setPlanName(request.getPlanName());
//            trainingPlan.setPlanTemplateId(trainingPlanTemplate.getId());
//            trainingPlan.setStartTime(startTime);
//            trainingPlan.setStatus(BaseStatus.NORMAL.getStatus());
//            trainingPlan.setTimeConsuming(0l);
//            trainingPlan.setUpdated(DateUtil.getCurrentTimeStamp());
//            trainingPlan.setUserId(userId);
//            trainingPlan.setActionCount(actions.size());
//            trainingPlan.setFrequency(StringUtils.strip(Arrays.toString(request.getFrequency().toArray()), "[]"));
//            trainingPlan.setCoverFileId(firstAction.getCoverFileId());
//            trainingPlan.setProgressRate(0);
//            trainingPlan.setTarget(request.getTarget());
//            trainingPlan.setTotalPlanCount(trainingDays.size() * actions.size());
//            trainingPlan.setTotalPlanDay(trainingDays.size());
//            trainingPlan.setTotalTrainedTime(0l);
//            trainingPlan.setTotalTrainedCount(0);
//            trainingPlan.setTotalTrainedDay(0);
//            if (DateUtil.getCurrentTimeStamp() >= trainingPlan.getStartTime()) {
//                trainingPlan.setTrainingStatus(TrainingPlanStatus.WAITING_STUDY.getStatus());
//            } else {
//                trainingPlan.setTrainingStatus(TrainingPlanStatus.WAITING.getStatus());
//            }
//            trainingPlanDao.insert(trainingPlan);
//
//            for (SavePlanActionRequest spar : actions) {
//                PlanAction planAction = new PlanAction();
//                ActionVo ac = actionService.getActionVo(spar.getActionId());
//                ActionVideo actionVideo = actionVideoService.getById(spar.getActionVideoId());
//                planAction.setCheckEnable((byte)0);
////                planAction.setCheckEnable(spar.getCheck() ? (byte)1 : (byte)0);
//                planAction.setActionCoverFileId(actionVideo.getCoverFileId());
//                planAction.setActionId(ac.getId());
//                planAction.setActionName(ac.getActionName());
//                planAction.setActionVideoId(actionVideo.getId());
//                planAction.setCreated(DateUtil.getCurrentTimeStamp());
//                planAction.setCycles(spar.getCycles());
//                planAction.setEveryDuration(spar.getEveryDuration());
//                planAction.setPlanId(trainingPlan.getId());
//                planAction.setStatus(BaseStatus.NORMAL.getStatus());
//                planAction.setUpdated(DateUtil.getCurrentTimeStamp());
//                planAction.setVideoDuration(actionVideo.getDuration());
//                planAction.setSort(0);
////                planAction.setSort(spar.getSort());
//                planAction.setRestTime(spar.getRestTime());
//                planActionService.savePlanAction(planAction);
//            }
//        }
//    }
//
    @Override
    public TrainingPlanVo getTrainingPlanVo(long id) {
        TrainingPlan trainingPlan = trainingPlanDao.selectById(id);
        if (trainingPlan == null) {
            return null;
        }
        return convertToVoTrainingPlanVo(trainingPlan);
    }
//
//    @Override
//    public List<SimpleTrainingPlanListVo> listSimpleTrainingPlan(long userId, byte[] status) {
//        QueryWrapper<TrainingPlan> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus())
//                .eq("patient_id", userId)
//                .in("training_status", status).orderByDesc("id");
//        List<TrainingPlan> list = trainingPlanDao.selectList(wrapper);
//        List<SimpleTrainingPlanListVo> collect = list.stream().map(trainingPlan ->
//                convertToSimpleTrainingPlanListVo(trainingPlan)).collect(Collectors.toList());
//        return collect;
//    }
//
//    @Transactional
//    @Override
//    public void updateTrainingPlan(long userId, UpdateTrainingPlanRequest request) {
//        TrainingPlan trainingPlan = trainingPlanDao.selectById(request.getId());
//        if (trainingPlan == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        List<SavePlanActionRequest> actions = request.getActions();
//        if (CollectionUtils.isEmpty(actions)) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        if (TrainingPlanStatus.WAITING.getStatus() != trainingPlan.getTrainingStatus()) {
//            throw new ServiceException(ResultCode.STATUS_ERROR);
//        }
//
//        //删除原有的 planAction
//        planActionService.deletePlanAction(trainingPlan.getId());
//
//        ActionVo firstAction = actionService.getActionVo(actions.get(0).getActionId());
//        long endTime = 0l;
//        if (request.getUnit() == 1) {
//            endTime = DateUtil.getTimeAfterDay(request.getStartTime(), request.getNum());
//        } else if (request.getUnit() == 2) {
//            endTime = DateUtil.getTimeAfterWeek(request.getStartTime(), request.getNum());
//        }
//
//        long startTime = DateUtil.getDateTimeStamp(request.getStartTime());
//        List<Long> trainingDays = DateUtil.getDaysBetweenTwoDayInWeek(startTime, endTime, request.getFrequency());
//
//        //修改 plan
//        trainingPlan.setNum(request.getNum());
//        trainingPlan.setUnit(request.getUnit());
//        trainingPlan.setDays(request.getNum());
//        trainingPlan.setEndTime(endTime);
//        trainingPlan.setPlanName(request.getPlanName());
//        trainingPlan.setStartTime(startTime);
//        trainingPlan.setStatus(BaseStatus.NORMAL.getStatus());
//        trainingPlan.setTimeConsuming(0l);
//        trainingPlan.setUpdated(DateUtil.getCurrentTimeStamp());
//        trainingPlan.setUserId(userId);
//        trainingPlan.setActionCount(actions.size());
//        trainingPlan.setFrequency(StringUtils.strip(Arrays.toString(request.getFrequency().toArray()), "[]"));
//        trainingPlan.setCoverFileId(firstAction.getCoverFileId());
//        trainingPlan.setProgressRate(0);
//        trainingPlan.setTarget(request.getTarget());
//        trainingPlan.setTotalPlanCount(trainingDays.size() * actions.size());
//        trainingPlan.setTotalPlanDay(trainingDays.size());
//        trainingPlan.setTotalTrainedTime(0l);
//        trainingPlan.setTotalTrainedCount(0);
//        trainingPlan.setTotalTrainedDay(0);
//        if (DateUtil.getCurrentTimeStamp() < trainingPlan.getStartTime()) {
//            trainingPlan.setTrainingStatus(TrainingPlanStatus.WAITING_STUDY.getStatus());
//        } else {
//            trainingPlan.setTrainingStatus(TrainingPlanStatus.WAITING.getStatus());
//        }
//        trainingPlanDao.updateById(trainingPlan);
//
//        //新增 planAction
//        for (SavePlanActionRequest spar : actions) {
//            PlanAction planAction = new PlanAction();
//            ActionVo ac = actionService.getActionVo(spar.getActionId());
//            ActionVideo actionVideo = actionVideoService.getById(spar.getActionVideoId());
//            planAction.setActionCoverFileId(actionVideo.getCoverFileId());
//            planAction.setCheckEnable((byte)0);
////            planAction.setCheckEnable(spar.getCheck() ? (byte)1 : (byte)0);
//            planAction.setActionId(ac.getId());
//            planAction.setActionName(ac.getActionName());
//            planAction.setActionVideoId(actionVideo.getId());
//            planAction.setCreated(DateUtil.getCurrentTimeStamp());
//            planAction.setCycles(spar.getCycles());
//            planAction.setEveryDuration(spar.getEveryDuration());
//            planAction.setPlanId(trainingPlan.getId());
//            planAction.setStatus(BaseStatus.NORMAL.getStatus());
//            planAction.setUpdated(DateUtil.getCurrentTimeStamp());
//            planAction.setVideoDuration(actionVideo.getDuration());
//            planActionService.savePlanAction(planAction);
//        }
//    }
//
//    @Override
//    public TrainingPlan getTrainingPlan(long id) {
//        return trainingPlanDao.selectById(id);
//    }
//
//    @Override
//    public void updateTrainingPlan(TrainingPlan trainingPlan) {
//        trainingPlanDao.updateById(trainingPlan);
//    }
//
//    @Override
//    public List<TrainingPlan> listFinishTrainingPlan(long userId) {
//        QueryWrapper<TrainingPlan> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("patient_id", userId);
//        wrapper.eq("training_status", TrainingPlanStatus.FINISHED.getStatus());
//        return trainingPlanDao.selectList(wrapper);
//    }
//
//    @Override
//    public List<TrainingPlanListVo> listPlanByPatient(TrainingPlanQuery query) {
//        QueryWrapper<TrainingPlan> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("patient_id", query.getPatientId());
//        if (!StringUtils.isBlank(query.getPlanName())) {
//            wrapper.like("plan_name", query.getPlanName());
//        }
//        if (query.getTrainingStatus() != 0) {
//            wrapper.like("training_status", query.getTrainingStatus());
//        }
//        wrapper.orderByDesc("id");
//
//        List<TrainingPlan> trainingPlans = trainingPlanDao.selectList(wrapper);
//
//        List<TrainingPlanListVo> list = trainingPlans.stream()
//                .map(trainingPlan -> convertToTrainingPlanListVo(trainingPlan)).collect(Collectors.toList());
//        return list;
//    }
//
//    private TrainingPlanListVo convertToTrainingPlanListVo(TrainingPlan trainingPlan) {
//        TrainingPlanListVo vo = new TrainingPlanListVo();
//        byte trainingStatus =  trainingPlan.getTrainingStatus();
//        if (DateUtil.getCurrentTimeStamp() < DateUtil.getStartTimeOfDay(trainingPlan.getStartTime())) {
//            if (trainingStatus != TrainingPlanStatus.WAITING.getStatus() ) {
//                trainingPlan.setTrainingStatus(TrainingPlanStatus.WAITING.getStatus());
//                trainingPlanDao.updateById(trainingPlan);
//            }
//        } else if (DateUtil.getCurrentTimeStamp() > DateUtil.getStartTimeOfDay(trainingPlan.getEndTime() + 24*60*60)) {
//            if (trainingStatus != TrainingPlanStatus.FINISHED.getStatus()) {
//                trainingPlan.setTrainingStatus(TrainingPlanStatus.FINISHED.getStatus());
//                trainingPlanDao.updateById(trainingPlan);
//            }
//        } else {
//            int count = trainingRecordService.countByPlanId(trainingPlan.getId());
//            if (count > 0) {
//                if (trainingStatus != TrainingPlanStatus.STUDYING.getStatus()) {
//                    trainingPlan.setTrainingStatus(TrainingPlanStatus.STUDYING.getStatus());
//                    trainingPlanDao.updateById(trainingPlan);
//                }
//            } else {
//                if (trainingStatus != TrainingPlanStatus.WAITING_STUDY.getStatus()) {
//                    trainingPlan.setTrainingStatus(TrainingPlanStatus.WAITING_STUDY.getStatus());
//                    trainingPlanDao.updateById(trainingPlan);
//                }
//            }
//
//        }
//
//        trainingPlan.getStartTime();
//        DoctorVo doctorVo = doctorService.getDoctorVo(trainingPlan.getUserId());
//        List<PlanActionListVo> planActionListVos = planActionService.listPlanActionByPlanId(trainingPlan.getId());
//        vo.setActions(planActionListVos.stream().map(PlanActionListVo::getActionName).distinct().collect(Collectors.joining(",")));
//        vo.setDays(trainingPlan.getDays());
//        vo.setFrequency(Arrays.asList(trainingPlan.getFrequency()
//                .split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList()));
//        vo.setId(trainingPlan.getId());
//        vo.setPatientId(trainingPlan.getPatientId());
//        vo.setPatientName(userService.findUser(trainingPlan.getPatientId()).getName());
//        vo.setPlanName(trainingPlan.getPlanName());
//        vo.setProgressRate(trainingPlan.getProgressRate());
//        vo.setStartTime(DateUtil.getYMD(trainingPlan.getStartTime()));
//        vo.setTrainingStatus(TrainingPlanStatus.getStatus(trainingPlan.getTrainingStatus()).getDesc());
//        vo.setDoctorName(doctorVo.getName());
//        vo.setDoctorId(doctorVo.getId());
//        vo.setActionCount(trainingPlan.getActionCount());
//        vo.setEndTime(DateUtil.getYMD(trainingPlan.getEndTime()));
//        vo.setTotalPlanCount(trainingPlan.getTotalPlanCount());
//        vo.setTotalPlanDay(trainingPlan.getTotalPlanDay());
//        vo.setTotalTrainedCount(trainingPlan.getTotalTrainedCount());
//        vo.setTotalTrainedDay(trainingPlan.getTotalTrainedDay());
//        vo.setPlanActionListVos(planActionListVos);
//        vo.setTarget(trainingPlan.getTarget());
//        return vo;
//    }
//
//    //TODO fix
    private TrainingPlanVo convertToVoTrainingPlanVo(TrainingPlan trainingPlan) {
        TrainingPlanVo vo = new TrainingPlanVo();
        //todo:复用虎哥代码 需要调试这个参数
//        LoginVo userLogin = userService.getUserLogin(trainingPlan.getPatientId());
        List<PlanActionListVo> planActionListVos = planActionService.listPlanActionByPlanId(trainingPlan.getId());
        vo.setActions(planActionListVos);
        vo.setEndTime(OldDateUtil.getYMD(trainingPlan.getEndTime()));
        vo.setFrequency(Arrays.asList(trainingPlan.getFrequency()
                .split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList()));
        vo.setId(trainingPlan.getId());
        vo.setPlanActionCount(trainingPlan.getActionCount());
        vo.setPlanName(trainingPlan.getPlanName());
        vo.setStartTime(OldDateUtil.getYMD(trainingPlan.getStartTime()));
        vo.setTarget(trainingPlan.getTarget());
        vo.setTotalPlanCount(trainingPlan.getTotalPlanCount());
        vo.setTotalPlanDay(trainingPlan.getTotalPlanDay());
        vo.setTotalTrainedCount(trainingPlan.getTotalTrainedCount());
        vo.setTotalTrainedDay(trainingPlan.getTotalTrainedDay());
        vo.setTotalTrainedTime(OldDateUtil.secondToHMS(trainingPlan.getTotalTrainedTime()));
        vo.setTrainingStatus(TrainingPlanStatus.getStatus(trainingPlan.getTrainingStatus()).getDesc());
        vo.setNum(trainingPlan.getNum());
        vo.setUnit(trainingPlan.getUnit());
        vo.setPatientId(trainingPlan.getPatientId());
//        vo.setPatientName(userLogin.getName());
        vo.setTemplateId(trainingPlan.getPlanTemplateId());
        return vo;
    }
//
//    private SimpleTrainingPlanListVo convertToSimpleTrainingPlanListVo(TrainingPlan trainingPlan) {
//        SimpleTrainingPlanListVo vo = new SimpleTrainingPlanListVo();
//        vo.setPlanId(trainingPlan.getId());
//        vo.setPlanName(trainingPlan.getPlanName());
//        return vo;
//    }
//
//    private String getFrequency(String week) {
//        String[] strings = week.split(",");
//        if (strings == null || strings.length == 0 ) {
//            return "";
//        }
//        if (strings.length == 7) {
//            return "每天";
//        }
//        return "每周" + strings.length + "天";
//    }
//
//    private String getPlanStatus(long startTime, long endTime) {
//        long now = DateUtil.getCurrentTimeStamp();
//        if (now < startTime) {
//            return "未开始";
//        } else if (now > endTime) {
//            return "进行中";
//        }
//        return "已完成";
//    }
}
