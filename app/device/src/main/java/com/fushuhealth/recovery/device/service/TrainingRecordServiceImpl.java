package com.fushuhealth.recovery.device.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.constant.ReportStatus;
import com.fushuhealth.recovery.common.constant.TrainingType;
import com.fushuhealth.recovery.common.storage.FileStorage;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.common.util.StringUtil;
import com.fushuhealth.recovery.dal.dao.TrainingRecordDao;
import com.fushuhealth.recovery.dal.entity.TrainingRecord;
import com.fushuhealth.recovery.dal.vo.ActionVo;
import com.fushuhealth.recovery.dal.vo.TrainingPlanVo;
import com.fushuhealth.recovery.dal.vo.TrainingRecordVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingRecordServiceImpl implements TrainingRecordService {

    private final static Logger log = LoggerFactory.getLogger(TrainingRecordServiceImpl.class);

    @Autowired
    private TrainingRecordDao trainingRecordDao;

//    @Autowired
//    private TrainingActionRecordDao trainingActionRecordDao;
//
    @Autowired
    private TrainingPlanService trainingPlanService;
//
//    @Autowired
//    private TrainingActionRecordService trainingActionRecordService;
//
    @Autowired
    private ActionService actionService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
////    private FileStorage fileStorage;
//
//    @Autowired
//    private FileService fileService;

//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private TrainingRecordRemarkService trainingRecordRemarkService;
//
//    @Autowired
//    private OrganizationService organizationService;

//    @Value("${gait-analysis.url}")
//    private String gaitAnalysisUrl;
//
//    @Value("${gait-analysis.notify-url}")
//    private String notifyUrl;

//    @Value(("${wx.share.url}"))
//    private String shareUrl;

    @Override
    public TrainingRecordVo getTrainingRecordVo(long id) {
        TrainingRecord trainingRecord = trainingRecordDao.selectById(id);
        return convertToTrainingRecordVo(trainingRecord);
    }

//    @Override
//    public ListTrainingRecordResponse listTrainingRecord(int pageNo, int pageSize, long patientId, byte trainingStatus, String planName) {
//
//        TrainingPlanQuery query = TrainingPlanQuery.builder()
//                .doctorId(0l)
//                .pageNo(pageNo)
//                .pageSize(pageSize)
//                .patientId(patientId)
//                .planName(planName)
//                .trainingStatus(trainingStatus).build();
//
//        ListTrainingPlanResponse response = trainingPlanService.getPlansByPatient(query, false);
//        PageVo page = response.getPage();
//        List<TrainingPlanListVo> plans = response.getPlans();
//
//        List<TrainingRecordListVo> list = new ArrayList<>();
//        for (TrainingPlanListVo trainingPlanListVo : plans) {
//            TrainingRecordListVo vo = new TrainingRecordListVo();
//            vo.setActionCount(trainingPlanListVo.getActionCount());
//            vo.setActions(trainingPlanListVo.getPlanActionListVos());
//            List<Long> trainingDays = getTrainingDaysByPlanIdAndPatientId(trainingPlanListVo.getId(), query.getPatientId());
//            vo.setDays(trainingDays);
//            vo.setDoctorId(trainingPlanListVo.getDoctorId());
//            vo.setDoctorName(trainingPlanListVo.getDoctorName());
//            vo.setEndDay(trainingPlanListVo.getEndTime());
//            vo.setFrequency(trainingPlanListVo.getFrequency());
//            vo.setId(trainingPlanListVo.getId());
//            vo.setPatientId(trainingPlanListVo.getPatientId());
//            vo.setPatientName(trainingPlanListVo.getPatientName());
//            vo.setPlanId(trainingPlanListVo.getId());
//            vo.setPlanName(trainingPlanListVo.getPlanName());
//            vo.setStartDay(trainingPlanListVo.getStartTime());
//            vo.setTotalPlanCount(trainingPlanListVo.getTotalPlanCount());
//            vo.setTotalPlanDay(trainingPlanListVo.getTotalPlanDay());
//            vo.setTotalTrainedCount(trainingPlanListVo.getTotalTrainedCount());
//            vo.setTotalTrainedDay(trainingPlanListVo.getTotalTrainedDay());
//            vo.setTrainingStatus(trainingPlanListVo.getTrainingStatus());
//            vo.setTarget(trainingPlanListVo.getTarget());
//            list.add(vo);
//        }
//        return new ListTrainingRecordResponse(page, list);
//    }
//
//    @Override
//    public List<TrainingRecord> listTrainingRecordByPlanId(long planId, long day) {
//        QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("plan_id", planId);
//        if (day != 0) {
//            wrapper.eq("training_day", day);
//        }
//        wrapper.orderByDesc("id");
//        return trainingRecordDao.selectList(wrapper);
//    }
//
//    @Transactional
//    @Override
//    public void saveTrainingRecord(SaveTrainingRecordRequest request, LoginVo loginVo) {
//        TrainingRecord trainingRecord = trainingRecordDao.selectById(request.getRecordId());
//
//        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlan(trainingRecord.getPlanId());
//
//        int actionCount = trainingPlan.getActionCount();
////        long startTime = DateUtil.getDateTimeStamp(request.getStartTime());
//        long startTime = DateUtil.getCurrentTimeStamp();
//        long startDay = DateUtil.getStartTimeOfDay(startTime);
//
//        ActionVo actionVo = actionService.getActionVo(request.getActionId());
//
//        String analysisPosition = actionVo.getAnalysisPosition();
//
//        String key = request.getKey();
//
//        String fileName = FilenameUtils.getName(key);
//        Files files = new Files();
//        files.setStatus(BaseStatus.NORMAL.getStatus());
//        files.setRawName(fileName);
//        files.setOriginalName(fileName);
//        files.setFileType(FileType.getType(request.getBucket()).getCode());
//        files.setCreated(DateUtil.getCurrentTimeStamp());
//        files.setFileSize(request.getSize());
//        files.setFilePath(request.getKey());
//        files.setExtension(FilenameUtils.getExtension(fileName));
//        files.setUpdated(DateUtil.getCurrentTimeStamp());
//        fileService.insertFiles(files);
//
//        File file = fileService.downloadFile(files.getId());
//
//        if (key.endsWith(".webm")) {
//            try {
//                File mp4File = VideoTool.convertToMp4(file);
//
//                key = fileService.saveFile(FileType.VIDEO, mp4File, mp4File.getName());
//
//                Files mp4 = new Files();
//                mp4.setStatus(BaseStatus.NORMAL.getStatus());
//                mp4.setRawName(mp4File.getName());
//                mp4.setOriginalName(mp4File.getName());
//                mp4.setFileType(FileType.VIDEO.getCode());
//                mp4.setCreated(DateUtil.getCurrentTimeStamp());
//                mp4.setFileSize(mp4File.getTotalSpace());
//                mp4.setFilePath(key);
//                mp4.setExtension(FilenameUtils.getExtension(mp4File.getName()));
//                mp4.setUpdated(DateUtil.getCurrentTimeStamp());
//                fileService.insertFiles(mp4);
//
//                files = mp4;
//                file = mp4File;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        fileStorage.convertToM3u8(FileType.VIDEO.getName(), key);
////        long duration = 0l;
////        File tempFile = fileService.getTempFile();
////        String fileUrl = fileService.getFileUrl(files.getId(), true);
////        try {
////            String coverName = StringUtil.uuid() + ".jpg";
////            HttpUtil.downloadFile(fileUrl, tempFile);
////            VideoInfo videoInfo = VideoTool.getVideoInfo(tempFile.getAbsolutePath(), coverName);
////            duration = videoInfo.getDuration();
////        } catch (Exception e) {
////
////        }
//
//        String coverName = StringUtil.uuid() + ".jpg";
//        VideoInfo videoInfo = null;
//        String coverPath = "";
//        long coverFileId = 0;
//        try {
//            videoInfo = VideoTool.getVideoInfo(file.getAbsolutePath(), coverName);
//            coverPath = fileService.saveFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//            log.info("cover file name : {}", coverPath);
//            Files coverFile = new Files();
//            coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//            coverFile.setExtension(FilenameUtils.getExtension(coverName));
//            coverFile.setFilePath(coverPath);
//            coverFile.setFileSize(videoInfo.getCover().length());
//            coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//            coverFile.setFileType(FileType.PICTURE.getCode());
//            coverFile.setOriginalName(coverName);
//            coverFile.setRawName(coverName);
//            coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//            fileService.insertFiles(coverFile);
//
//            coverFileId = coverFile.getId();
//        } catch (Exception e) {
//            log.error("error:", e);
//            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//        }
//
//        long duration = videoInfo.getDuration();
//
//        //保存 training action record
//        String position = actionVo.getAnalysisPosition();
//        List<PositionFile> positionFiles = new ArrayList<>();
//
//        PositionFile positionFile = new PositionFile(position, files.getId());
//        positionFiles.add(positionFile);
//        TrainingActionRecord trainingActionRecord = new TrainingActionRecord();
//        trainingActionRecord.setRecordId(trainingRecord.getId());
//        trainingActionRecord.setCreated(DateUtil.getCurrentTimeStamp());
//        trainingActionRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//        trainingActionRecord.setEndTime(0l);
//        trainingActionRecord.setStartTime(startTime);
//        trainingActionRecord.setStartDay(startDay);
//        trainingActionRecord.setDuration(duration);
//        trainingActionRecord.setActionId(actionVo.getId());
//        trainingActionRecord.setStatus(BaseStatus.NORMAL.getStatus());
//        trainingActionRecord.setTrainingStatus(TrainingStatus.RECORDED.getStatus());
//        trainingActionRecord.setUserId(request.getUserId());
//        trainingActionRecord.setDoctorId(loginVo.getId());
//        trainingActionRecord.setEquipmentId(actionVo.getEquipmentId());
//        trainingActionRecord.setInstrumentId(actionVo.getInstrumentId());
//        trainingActionRecord.setVideos(JSON.toJSONString(positionFiles));
//        trainingActionRecord.setCoverFileId(coverFileId);
//        trainingActionRecordService.saveTrainingAction(trainingActionRecord);
//
//        //更新 training record
//        List<TrainingActionRecord> trainingActionRecords = trainingActionRecordService.listTrainingActionRecordByRecordId(trainingRecord.getId());
//        trainingRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//        trainingRecord.setTrainingProgress(trainingActionRecords.size() / actionCount * 100);
//        trainingRecord.setTrainedAction(trainingActionRecords.size());
//        trainingRecord.setDuration(trainingRecord.getDuration() + duration);
//        trainingRecordDao.updateById(trainingRecord);
//
//        String taskUrl = gaitAnalysisUrl;
//        OrganizationVo organizationVo = organizationService.getOrganizationVo(AuthContext.getUser().getOrganizationId());
//        if (organizationVo != null && StringUtils.isNotBlank(organizationVo.getTaskUrl())) {
//            taskUrl = organizationVo.getTaskUrl();
//        }
//        //提交任务
//        SubmitGaitAnalysisTaskRequest taskRequest = new SubmitGaitAnalysisTaskRequest();
//        taskRequest.setEv_id(trainingActionRecord.getId());
//        taskRequest.setMotion_id(actionVo.getMotionId());
//        taskRequest.setPosition(analysisPosition);
//        taskRequest.setUrl(fileService.getFilePath(files.getId()));
//        taskRequest.setNotify_url(notifyUrl);
//        log.info("task request : {}", JSON.toJSONString(taskRequest));
//        String s = restTemplate.postForObject(taskUrl, taskRequest, String.class);
//        log.info("task response : {}", s);
//        SubmitTaskResponse response = JSON.parseObject(s, SubmitTaskResponse.class);
//        log.info("task response : {}", JSON.toJSONString(response));
//        if (response.getStatus() != 0) {
//            log.error("submit task error");
//        } else {
//            log.info("submit task success");
//            trainingActionRecord.setTrainingStatus(TrainingStatus.PROCESSING.getStatus());
//            trainingActionRecordDao.updateById(trainingActionRecord);
//            log.info("finish submit task");
//        }
//
//        User us = userService.getUser(request.getUserId());
//
//        //更新训练计划数据
//        //判断是否增加训练天数，如果存在同一天的数据，则不增加天数
//        int trainingRecordByDay = listTrainingRecordByDay(request.getUserId(), trainingPlan.getId(), startDay);
//
//        if (TrainingPlanStatus.WAITING.getStatus() == trainingPlan.getTrainingStatus()) {
//            trainingPlan.setTrainingStatus(TrainingPlanStatus.STUDYING.getStatus());
//        } else if (TrainingPlanStatus.STUDYING.getStatus() == trainingPlan.getTrainingStatus()) {
//            if (DateUtil.getCurrentTimeStamp() > DateUtil.getStartTimeOfDay(trainingPlan.getEndTime() + 24 * 60 * 60)) {
//                trainingPlan.setTrainingStatus(TrainingPlanStatus.FINISHED.getStatus());
//            }
//        }
//
//        int count = countByPlanId(trainingPlan.getId());
//        trainingPlan.setTotalTrainedCount(count);
//        trainingPlan.setTotalTrainedTime(trainingPlan.getTotalTrainedTime() + duration);
//        if (trainingPlan.getTotalPlanCount() == 0) {
//            trainingPlan.setProgressRate(0);
//        } else {
//            double i = trainingPlan.getTotalTrainedCount() / trainingPlan.getTotalPlanCount();
//            if(i >= 1) {
//                trainingPlan.setProgressRate(100);
//            } else {
//                trainingPlan.setProgressRate((int)(i * 100));
//            }
//
//        }
//        trainingPlanService.updateTrainingPlan(trainingPlan);
//
//        //更新用户统计数据
//        us.setTrainedCount(us.getTrainedCount() + 1);
//        //更新完成训练的课程数
//        List<TrainingPlan> trainingPlans = trainingPlanService.listFinishTrainingPlan(us.getId());
//        us.setTrainedPlan((long)trainingPlans.size());
//        us.setTrainedTime(us.getTrainedTime() + duration);
//        userService.update(us);
//    }
//
//    @Transactional
//    @Override
//    public void createTrainingRecord(CreateTrainingRecordRequest request, LoginVo loginVo) {
//        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlan(request.getPlanId());
//        TrainingRecord trainingRecord = new TrainingRecord();
//        trainingRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//        trainingRecord.setStatus(BaseStatus.NORMAL.getStatus());
//        trainingRecord.setCreated(DateUtil.getCurrentTimeStamp());
//        trainingRecord.setDuration(0l);
//        trainingRecord.setPlanId(request.getPlanId());
//        trainingRecord.setReportStatus(ReportStatus.RECORDED.getStatus());
//        trainingRecord.setTotalAction(trainingPlan.getActionCount());
//        trainingRecord.setTrainingProgress(0);
//        trainingRecord.setTrainingTime(DateUtil.getCurrentTimeStamp());
//        trainingRecord.setTrainingDay(DateUtil.getStartTimeOfDay(DateUtil.getCurrentTimeStamp()));
//        trainingRecord.setTrainedAction(1);
//        trainingRecord.setTrainingType(TrainingType.PLAN.getType());
//        trainingRecord.setUserId(request.getUserId());
//        trainingRecordDao.insert(trainingRecord);
//
//        //更新训练计划数据
//        //判断是否增加训练天数，如果存在同一天的数据，则不增加天数
//        int trainingRecordByDay = listTrainingRecordByDay(request.getUserId(), trainingPlan.getId(), trainingRecord.getTrainingDay());
//
//        User us = userService.getUser(request.getUserId());
//
//        if (trainingRecordByDay == 0) {
//            trainingPlan.setTotalTrainedDay(trainingPlan.getTotalTrainedDay() + 1);
//            if (TrainingPlanStatus.WAITING.getStatus() == trainingPlan.getTrainingStatus()) {
//                trainingPlan.setTrainingStatus(TrainingPlanStatus.STUDYING.getStatus());
//            } else if (TrainingPlanStatus.STUDYING.getStatus() == trainingPlan.getTrainingStatus()) {
//                if (DateUtil.getCurrentTimeStamp() > DateUtil.getStartTimeOfDay(trainingPlan.getEndTime() + 24 * 60 * 60)) {
//                    trainingPlan.setTrainingStatus(TrainingPlanStatus.FINISHED.getStatus());
//                }
//            }
//            QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//            wrapper.select("DISTINCT training_day");
//            wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//            wrapper.eq("user_id", us.getId());
//            Integer count = trainingRecordDao.selectCount(wrapper);
//            us.setTrainedDay((long)count);
//        }
//
//        int count = countByPlanId(trainingPlan.getId());
//        trainingPlan.setTotalTrainedCount(count);
//        trainingPlanService.updateTrainingPlan(trainingPlan);
//
//        us.setTrainedCount(us.getTrainedCount() + 1);
//        userService.update(us);
//    }
//
//    @Override
//    public List<TrainingRecord> listByPlanIdAndDay(long planId, long patientId, long day) {
//        QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("plan_id", planId);
//        wrapper.eq("user_id", patientId);
//        wrapper.eq("training_day", day);
//        wrapper.orderByAsc("id");
//        return trainingRecordDao.selectList(wrapper);
//    }
//
//    @Override
//    public List<TrainingRecordListByDayVo> getTrainingRecordList(TrainingRecordQuery query) {
//        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlan(query.getPlanId());
//        if (trainingPlan == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        List<TrainingRecordListByDayVo> resultList = new ArrayList<>();
//        List<TrainingRecord> trainingRecords = listByPlanIdAndDay(trainingPlan.getId(), trainingPlan.getPatientId(), query.getDay());
//        if (CollectionUtils.isEmpty(trainingRecords)) {
//            return resultList;
//        }
//
//        for (TrainingRecord trainingRecord : trainingRecords) {
//            List<TrainingActionRecordListVoV2> trainingActionRecordListVoV2s = trainingActionRecordService.listByRecordId(trainingRecord);
//            TrainingRecordListByDayVo trainingRecordListByDayVo = new TrainingRecordListByDayVo();
//            trainingRecordListByDayVo.setId(trainingRecord.getId());
//            trainingRecordListByDayVo.setTrainingTime(trainingRecord.getTrainingTime());
//            trainingRecordListByDayVo.setActions(trainingActionRecordListVoV2s);
//
//            resultList.add(trainingRecordListByDayVo);
//        }
//        return resultList;
//    }
//
//    @Transactional
//    @Override
//    public void updateTrainingRecordRemark(UpdateTrainingRecordRemarkRequest request) {
//        TrainingRecord trainingRecord = trainingRecordDao.selectById(request.getId());
//        if (trainingRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        trainingRecord.setRemark(request.getRemark());
//        trainingRecord.setDoctorName(request.getDoctorName());
//        trainingRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//        trainingRecordDao.updateById(trainingRecord);
//
//        SaveRemarkRequest saveRemarkRequest = SaveRemarkRequest.builder()
//                .doctorName(request.getDoctorName())
//                .remark(request.getRemark())
//                .trainingPlanId(trainingRecord.getPlanId())
//                .trainingRecordId(trainingRecord.getId())
//                .userId(trainingRecord.getUserId()).build();
//        trainingRecordRemarkService.saveRemark(saveRemarkRequest);
//    }
//
//    @Override
//    public ShareRecordResultVo shareRecordResult(long id) {
//        TrainingRecord trainingRecord = trainingRecordDao.selectById(id);
//        if (trainingRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        return new ShareRecordResultVo(shareUrl + id);
//    }
//
//    @Override
//    public int countByPlanId(long planId) {
//        QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("plan_id", planId);
//        return trainingRecordDao.selectCount(wrapper);
//    }
//
//    @Override
//    public ListMechanismTrainingRecordResponse listMechanismRecord(int pageNo, int pageSize, long patientId) {
//
//        Page<TrainingRecord> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("user_id", patientId);
//        wrapper.eq("training_type", TrainingType.ACTION.getType());
//        wrapper.orderByDesc("training_time");
//        page = trainingRecordDao.selectPage(page, wrapper);
//
//        List<TrainingRecord> records = page.getRecords();
//        List<MechanismTrainingRecordListVo> collect = records.stream().map(record -> convertToMechanismTrainingRecordListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListMechanismTrainingRecordResponse(pageVo, collect);
//    }
//
//    @Transactional
//    @Override
//    public void deleteRecord(long id) {
//        TrainingRecord trainingRecord = trainingRecordDao.selectById(id);
//        if (trainingRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        List<TrainingActionRecord> trainingActionRecords = trainingActionRecordService.listTrainingActionRecordByRecordId(trainingRecord.getId());
//
//        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlan(trainingRecord.getPlanId());
//
//        User user = userService.getUser(trainingRecord.getUserId());
//
//        //修改 training plan
//        int count = countByPlanId(trainingPlan.getId());
//        trainingPlan.setTotalTrainedCount(count);
//        trainingPlan.setTotalTrainedTime(trainingPlan.getTotalTrainedTime() - trainingRecord.getDuration());
//
//        int trainingRecordByDay = listTrainingRecordByDay(trainingRecord.getUserId(), trainingPlan.getId(), trainingRecord.getTrainingDay());
//        if (trainingRecordByDay == 1) {
//            trainingPlan.setTotalTrainedDay(trainingPlan.getTotalTrainedDay() - 1);
//            user.setTrainedDay(user.getTrainedDay() - 1);
//        }
//        trainingPlanService.updateTrainingPlan(trainingPlan);
//
//        //修改 user
//        user.setTrainedTime(user.getTrainedTime() - trainingRecord.getDuration());
//        user.setTrainedCount(user.getTrainedCount() - 1);
//        user.setUpdated(DateUtil.getCurrentTimeStamp());
//        userService.update(user);
//
//        //删除 training action record
//        for (TrainingActionRecord trainingActionRecord : trainingActionRecords) {
//            trainingActionRecord.setStatus(BaseStatus.DELETE.getStatus());
//            trainingActionRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//            trainingActionRecordService.update(trainingActionRecord);
//        }
//
//        //删除 training record
//        trainingRecord.setStatus(BaseStatus.DELETE.getStatus());
//        trainingRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//        trainingRecordDao.updateById(trainingRecord);
//    }
//
//    @Override
//    public void updateTrainingRecordAction(long id, long actionId) {
//        TrainingActionRecord trainingActionRecord = trainingActionRecordDao.selectById(id);
//        if (trainingActionRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ActionVo actionVo = actionService.getActionVo(actionId);
//        trainingActionRecord.setActionId(actionVo.getId());
//        trainingActionRecord.setEquipmentId(actionVo.getEquipmentId());
//        trainingActionRecord.setInstrumentId(actionVo.getInstrumentId());
//        trainingActionRecordDao.updateById(trainingActionRecord);
//    }
//
//    private MechanismTrainingRecordListVo convertToMechanismTrainingRecordListVo(TrainingRecord trainingRecord) {
//        MechanismTrainingRecordListVo vo = new MechanismTrainingRecordListVo();
//        List<TrainingActionRecord> trainingActionRecords = trainingActionRecordService.listTrainingActionRecordByRecordId(trainingRecord.getId());
//
//        ActionVo actionVo = null;
//        if (CollectionUtils.isNotEmpty(trainingActionRecords)) {
//            actionVo = actionService.getActionVo(trainingActionRecords.get(0).getActionId());
//        } else {
//            actionVo = actionService.getActionVo(trainingRecord.getPlanId());
//        }
//
//        vo.setActionName(actionVo == null ? "" : actionVo.getActionName());
//        vo.setDuration(DateUtil.secondToHMS(trainingRecord.getDuration()));
//        vo.setId(trainingRecord.getId());
//        vo.setTrainingTime(DateUtil.getYMDHMSDate(trainingRecord.getTrainingTime()));
//        vo.setResultPage(actionVo == null ? "" : actionVo.getResultPage());
//        vo.setTrainingActionRecordId(trainingActionRecords.get(0).getId());
//        return vo;
//    }
//
//    private List<Long> getTrainingDaysByPlanIdAndPatientId(long planId, Long patientId) {
//        QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("plan_id", planId);
//        wrapper.eq("user_id", patientId);
//        wrapper.orderByDesc("training_day");
//        List<TrainingRecord> trainingRecords = trainingRecordDao.selectList(wrapper);
//        List<Long> list = trainingRecords.stream().map(TrainingRecord::getTrainingDay).distinct().collect(Collectors.toList());
//        return list;
//    }
//
//    private int listTrainingRecordByDay(long userId, long planId, long dayOfTime) {
//        QueryWrapper<TrainingRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus())
//                .eq("user_id", userId)
//                .eq("plan_id", planId)
//                .eq("training_day", dayOfTime);
//        return trainingRecordDao.selectCount(wrapper);
//    }
//
    private TrainingRecordVo convertToTrainingRecordVo(TrainingRecord trainingRecord) {
        TrainingRecordVo vo = new TrainingRecordVo();
        if (trainingRecord.getTrainingType().equals(TrainingType.PLAN.getType())) {
            TrainingPlanVo trainingPlanVo = trainingPlanService.getTrainingPlanVo(trainingRecord.getPlanId());
            vo.setPlanId(trainingPlanVo.getId());
            vo.setPlanName(trainingPlanVo.getPlanName());
        } else {
            vo.setPlanId(trainingRecord.getPlanId());
            ActionVo actionVo = actionService.getActionVo(trainingRecord.getPlanId());
            vo.setPlanName(actionVo.getActionName());
        }

        vo.setDuration(OldDateUtil.secondToHMS(trainingRecord.getDuration()));
        vo.setId(trainingRecord.getId());
        vo.setReportStatus(ReportStatus.getStatus(trainingRecord.getReportStatus()).getDesc());
        vo.setStartTime(DateUtil.getYMDHMSDate(trainingRecord.getTrainingTime()));
        vo.setTrainingType(trainingRecord.getTrainingType());
        vo.setRemark(trainingRecord.getRemark());
        vo.setDoctorName(trainingRecord.getDoctorName());
        return vo;
    }
}
