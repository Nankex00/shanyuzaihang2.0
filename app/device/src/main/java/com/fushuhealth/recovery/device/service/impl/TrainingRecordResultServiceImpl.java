package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.common.util.StringUtils;
import com.fushuhealth.recovery.dal.dao.TrainingRecordResultDao;
import com.fushuhealth.recovery.dal.entity.PlanAction;
import com.fushuhealth.recovery.dal.entity.ScaleEvaluationRecord;
import com.fushuhealth.recovery.dal.entity.TrainingActionRecord;
import com.fushuhealth.recovery.dal.entity.TrainingRecordResult;
import com.fushuhealth.recovery.dal.vo.*;
import com.fushuhealth.recovery.device.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingRecordResultServiceImpl implements TrainingRecordResultService {

    private final static Logger log = LoggerFactory.getLogger(TrainingRecordResultServiceImpl.class);
//
    @Autowired
    private TrainingRecordResultDao recordResultDao;
//
//    @Autowired
//    private TrainingRecordDao trainingRecordDao;
//
    @Autowired
    private TrainingPlanService trainingPlanService;
//
//    @Autowired
//    private TrainingPlanTemplateService trainingPlanTemplateService;
//
    @Autowired
    private TrainingRecordService trainingRecordService;

    @Autowired
    private TrainingActionRecordService trainingActionRecordService;

    @Autowired
    private PlanActionService planActionService;
//
//    @Autowired
//    private ActionVideoService actionVideoService;

    @Autowired
    private FileService fileService;
//
//    @Autowired
//    private FileStorage fileStorage;
//
    @Autowired
    private ActionService actionService;

    @Autowired
    private ISysUserService userService;
//
//    @Autowired
//    private WxMpService wxMpService;
//
//    @Value("${wx.template.generate-report}")
//    private String generateReport;
//
//    @Value("${wx.template.report-url}")
//    private String reportUrl;
//
//    @Value("${file.domain}")
//    private String fileDomain;
//
    @Autowired
    private ScaleRecordService scaleRecordService;

    @Autowired
    private IChildrenService childrenService;
//
//    @Override
//    public TrainingRecordResultPlanVo getResultByPlanId(long planId, long day) {
//        TrainingPlanVo trainingPlanVo = trainingPlanService.getTrainingPlanVo(planId);
//        if (trainingPlanVo == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//
//        List<PlanActionListVo> planActionListVos = planActionService.listPlanActionByPlanId(trainingPlanVo.getId());
//
//        LoginVo userLogin = userService.getUserLogin(trainingPlanVo.getPatientId());
//        TrainingRecordResultPlanVo trainingRecordResultPlanVo = new TrainingRecordResultPlanVo();
//        trainingRecordResultPlanVo.setPlanId(trainingPlanVo.getId());
//        trainingRecordResultPlanVo.setTrainingStatus(trainingPlanVo.getTrainingStatus());
//        trainingRecordResultPlanVo.setEndTime(trainingPlanVo.getEndTime());
//        trainingRecordResultPlanVo.setPlanName(trainingPlanVo.getPlanName());
//        trainingRecordResultPlanVo.setStartTime(trainingPlanVo.getStartTime());
//        trainingRecordResultPlanVo.setFrequency(trainingPlanVo.getFrequency());
//        trainingRecordResultPlanVo.setTarget(trainingPlanVo.getTarget());
//
//        if (trainingPlanVo.getTemplateId() != 0) {
//            TrainingPlanTemplate trainingPlanTemplate = trainingPlanTemplateService.getTrainingPlanTemplate(trainingPlanVo.getTemplateId());
//            trainingRecordResultPlanVo.setPlanTemplateName(trainingPlanTemplate.getName());
//        } else {
//            trainingRecordResultPlanVo.setPlanTemplateName(trainingPlanVo.getPlanName());
//        }
//
//        trainingRecordResultPlanVo.setAge(userLogin.getAge());
//        trainingRecordResultPlanVo.setUserId(userLogin.getId());
//        trainingRecordResultPlanVo.setGender(userLogin.getGender());
//        trainingRecordResultPlanVo.setPhone(userLogin.getPhone());
//        trainingRecordResultPlanVo.setUserName(trainingPlanVo.getPatientName());
//
//
//
//        //获取此计划对应的训练记录列表
//        List<TrainingRecord> trainingRecords = trainingRecordService.listTrainingRecordByPlanId(planId, day);
//        TrainingRecord tr = null;
//        if (CollectionUtils.isNotEmpty(trainingRecords)) {
//            tr = trainingRecords.get(0);
//            trainingRecordResultPlanVo.setTime(DateUtil.getYMDHMSDate(tr.getTrainingTime()));
//
//        }
//
//        List<TrainingActionRecordResultListVo> trainingActionRecordResultListVos = new ArrayList<TrainingActionRecordResultListVo>();
//        for (TrainingRecord trainingRecord : trainingRecords) {
//
//            //获取此次训练记录对应的动作记录列表
//            List<TrainingActionRecord> trainingActionRecords =
//                    trainingActionRecordService.listTrainingActionRecordByRecordId(trainingRecord.getId());
//
//            for (TrainingActionRecord trainingActionRecord : trainingActionRecords) {
//
//                //获取此计划和动作对应的 PlanAction
//                PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(planId, trainingActionRecord.getActionId());
//
//                //查询此训练动作记录对应的算法分析结果
//                QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
//                wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//                wrapper.eq("training_action_record_id", trainingActionRecord.getId());
//                List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);
//
//                TrainingActionRecordResultListVo trainingActionRecordResultListVo = new TrainingActionRecordResultListVo();
//
//                trainingActionRecordResultListVo.setActionId(trainingActionRecord.getActionId());
//                ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//                trainingActionRecordResultListVo.setActionName(actionVo.getActionName());
//                trainingActionRecordResultListVo.setResultPage(actionVo.getResultPage());
//                trainingActionRecordResultListVo.setTime(DateUtil.getYMD(trainingActionRecord.getStartDay()));
//                trainingActionRecordResultListVo.setTrainingActionRecordId(trainingActionRecord.getId());
//
//                String videos = trainingActionRecord.getVideos();
//                List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
//                PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//
//                TrainingRecordResult trainingRecordResult = null;
//                if (CollectionUtils.isNotEmpty(list)) {
//                    trainingRecordResult = list.get(0);
//                    PositionFile pf = new PositionFile(positionFile.getPosition(), trainingRecordResult.getDstVideoFileId());
//                    if (positionFile != null) {
//                        positionFiles.remove(positionFile);
//                        positionFiles.add(pf);
//                        Collections.swap(positionFiles, positionFiles.indexOf(pf), 0);
//                    }
//
//                    Long cycle = (trainingActionRecord.getDuration() + 1) / Math.max(planAction.getVideoDuration(), planAction.getEveryDuration());
//                    trainingActionRecordResultListVo.setFinishCount(cycle.intValue());
//                    trainingActionRecordResultListVo.setPlanCount(planAction.getCycles());
//
//                    trainingActionRecordResultListVo.setId(trainingRecordResult.getId());
////                    trainingRecordResultPlanVo.setTrainingRecordResultId(trainingRecordResult.getId());
//                    trainingRecordResultPlanVo.setDoctorName(trainingRecordResult.getDoctorName());
//                    trainingRecordResultPlanVo.setRemark(trainingRecordResult.getRemark());
//
//                    if (trainingRecordResult.getFeatureFileId() != null && trainingRecordResult.getFeatureFileId() > 0) {
//                        trainingActionRecordResultListVo.setFeatureFileUrl(fileService.getFileUrl(trainingRecordResult.getFeatureFileId(), false));
//                    } else {
//                        trainingActionRecordResultListVo.setFeatureFileUrl("");
//                    }
//                    if (trainingRecordResult.getKeyPointsFileId() != null && trainingRecordResult.getKeyPointsFileId() > 0) {
//                        trainingActionRecordResultListVo.setKeypointFileUrl(fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), false));
//                    } else {
//                        trainingActionRecordResultListVo.setKeypointFileUrl("");
//                    }
//                } else {
//                    trainingActionRecordResultListVo.setFeatureFileUrl("");
//                    trainingActionRecordResultListVo.setKeypointFileUrl("");
////                    trainingRecordResultPlanVo.setTrainingRecordResultId(0l);
//                    trainingActionRecordResultListVo.setId(0l);
//                    trainingRecordResultPlanVo.setDoctorName("");
//                    trainingRecordResultPlanVo.setRemark("");
//                }
//
//                List<PositionFileVo> collect = positionFiles.stream().map(pff -> convertToPositionFileVo(pff)).collect(Collectors.toList());
//                trainingActionRecordResultListVo.setVideos(collect);
//
//                trainingActionRecordResultListVos.add(trainingActionRecordResultListVo);
//            }
//            trainingRecordResultPlanVo.setRecords(trainingActionRecordResultListVos);
//        }
//        return trainingRecordResultPlanVo;
//    }
//
    @Override
    public List<TrainingActionRecordResultVo> listResultByRecordId(long recordId) {


        //获取训练记录
        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(recordId);

        //获取训练列表
        TrainingPlanVo trainingPlanVo = trainingPlanService.getTrainingPlanVo(trainingRecordVo.getPlanId());

        //获取此次训练记录对应的动作记录列表
        List<TrainingActionRecord> trainingActionRecords =
                trainingActionRecordService.listTrainingActionRecordByRecordId(recordId);

        List<TrainingActionRecordResultVo> trainingActionRecordResultListVos = new ArrayList<TrainingActionRecordResultVo>();
        for (TrainingActionRecord trainingActionRecord : trainingActionRecords) {

            //获取此计划和动作对应的 PlanAction
            PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(trainingRecordVo.getPlanId(), trainingActionRecord.getActionId());

            //查询此训练动作记录对应的算法分析结果
            QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
            wrapper.eq("status", BaseStatus.NORMAL.getStatus());
            wrapper.eq("training_action_record_id", trainingActionRecord.getId());
            List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);

            TrainingActionRecordResultVo trainingActionRecordResultVo = new TrainingActionRecordResultVo();

            if (CollectionUtils.isNotEmpty(list)) {
                TrainingRecordResult trainingRecordResult = list.get(0);
                trainingActionRecordResultVo.setEffectCount(trainingRecordResult.getEffectCount());
                trainingActionRecordResultVo.setFinishCount(trainingRecordResult.getFinishCount());
            } else {
                trainingActionRecordResultVo.setEffectCount(0);
                trainingActionRecordResultVo.setFinishCount(0);
            }

            ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());

            trainingActionRecordResultVo.setActionId(trainingActionRecord.getActionId());
            trainingActionRecordResultVo.setActionName(actionVo.getActionName());
            //单个计划动作的总数
            trainingActionRecordResultVo.setPlanCount(planAction.getCycles() * trainingPlanVo.getTotalPlanDay());
            trainingActionRecordResultVo.setTime(OldDateUtil.getYMD(trainingActionRecord.getStartDay()));
            trainingActionRecordResultVo.setId(trainingActionRecord.getId());
            trainingActionRecordResultVo.setResultPage(actionVo.getResultPage());


            trainingActionRecordResultListVos.add(trainingActionRecordResultVo);
        }
        return trainingActionRecordResultListVos;
    }

//    @Override
//    public List<TrainingRecordResult> listResultByActionRecordId(long actionRecordId) {
//        QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("training_action_record_id", actionRecordId);
//        List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);
//        return list;
//    }
//
//    @Override
//    public RecordResultPictureVo getResultPicture(long id, int index) {
//        TrainingRecordResult trainingRecordResult = recordResultDao.selectById(id);
//        if (trainingRecordResult == null) {
//            return null;
//        }
//        String pictures = trainingRecordResult.getPictures();
//        List<PictureModel> list = new ArrayList<PictureModel>();
//        if (StringUtils.isNotBlank(pictures)) {
//            List<PictureModel> pictureModels = JSON.parseArray(pictures, PictureModel.class);
//            int size = pictureModels.size();
//
//            if (size < 12) {
//                list = pictureModels;
//            } else {
//                if (index < 5) {
//                    list = pictureModels.subList(0, 11);
//                } else if (index > size - 5) {
//                    list = pictureModels.subList(size - 12, size - 1);
//                } else {
//                    list = pictureModels.subList(index - 5, index + 6);
//                }
//            }
//        }
//        String keypointUrl = "";
//        if (trainingRecordResult.getKeyPointsFileId() != 0) {
//            keypointUrl = fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), false);
//        }
//        List<PictureModelVo> collect = list.stream().map(pictureModel -> convertToPictureModelVo(pictureModel)).collect(Collectors.toList());
//        return new RecordResultPictureVo(id, keypointUrl, collect);
//    }
//
//    @Transactional
//    @Override
//    public RecordResultPictureVo updatePictureKeyPoints(UpdateRecordResultPictureRequest recordResultPictureRequest) {
//        TrainingRecordResult trainingRecordResult = recordResultDao.selectById(recordResultPictureRequest.getId());
//        if (trainingRecordResult == null) {
//            return null;
//        }
//        Integer index = recordResultPictureRequest.getIndex();
//        if (trainingRecordResult.getKeyPointsFileId() != 0) {
//            File file = fileService.downloadFile(trainingRecordResult.getKeyPointsFileId());
//            try {
//                String content = FileUtils.readFileToString(file, "UTF-8");
//                List<KeyPoints> keyPoints = JSON.parseArray(content, KeyPoints.class);
//
//
//
//                KeyPoints points = new KeyPoints();
//                if (CollectionUtils.isNotEmpty(keyPoints)) {
//                    KeyPoints kp = keyPoints.get(index - 1);
//                    if (kp != null && kp.getIndex() == index) {
//                        points = kp;
//                    } else {
//                        kp = keyPoints.get(0);
//                        points.setEdit_status(kp.getEdit_status());
//                        points.setFps(kp.getFps());
//                        points.setHeight(kp.getHeight());
//                        points.setImage_count(kp.getImage_count());
//                        points.setIndex(index);
//                        String name = String.format("%08d", index);
//                        points.setName(name + ".jpg");
//                        points.setWidth(kp.getWidth());
//                    }
//                }
//                KeyPointsData keyPointsData = new KeyPointsData();
//                keyPointsData.setBbox(recordResultPictureRequest.getBbox());
//                keyPointsData.setKeypoints(recordResultPictureRequest.getKeyPoints());
//                points.setData(keyPointsData);
//
//                keyPoints.add(index - 1, points);
//
//                File randomFile = fileStorage.getTempFileStorage().randomFile();
//                FileUtils.writeByteArrayToFile(randomFile, JSON.toJSONString(keyPoints).getBytes());
//
//                String fileName = "keypoints.json";
//                String path = fileService.saveFile(FileType.JSON, randomFile, fileName);
//
//                Files keyPointsFile = new Files();
//                keyPointsFile.setFilePath(path);
//                keyPointsFile.setStatus(BaseStatus.NORMAL.getStatus());
//                keyPointsFile.setRawName(fileName);
//                keyPointsFile.setOriginalName(fileName);
//                keyPointsFile.setFileType(FileType.JSON.getCode());
//                keyPointsFile.setCreated(DateUtil.getCurrentTimeStamp());
//                keyPointsFile.setFileSize(0l);
//                keyPointsFile.setExtension(FilenameUtils.getExtension(fileName));
//                keyPointsFile.setUpdated(DateUtil.getCurrentTimeStamp());
//                fileService.insertFiles(keyPointsFile);
//
//                trainingRecordResult.setKeyPointsFileId(keyPointsFile.getId());
//                recordResultDao.updateById(trainingRecordResult);
//
//                String pictures = trainingRecordResult.getPictures();
//                List<PictureModel> list = new ArrayList<PictureModel>();
//                if (StringUtils.isNotBlank(pictures)) {
//                    List<PictureModel> pictureModels = JSON.parseArray(pictures, PictureModel.class);
//                    int size = pictureModels.size();
//
//                    if (size < 12) {
//                        list = pictureModels;
//                    } else {
//                        if (index < 5) {
//                            list = pictureModels.subList(0, 11);
//                        } else if (index > size - 5) {
//                            list = pictureModels.subList(size - 12, size - 1);
//                        } else {
//                            list = pictureModels.subList(index - 5, index + 6);
//                        }
//                    }
//                }
//                String keypointUrl = "";
//                if (trainingRecordResult.getKeyPointsFileId() != 0) {
//                    keypointUrl = fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), false);
//                }
//                List<PictureModelVo> collect = list.stream().map(pictureModel -> convertToPictureModelVo(pictureModel)).collect(Collectors.toList());
//                return new RecordResultPictureVo(recordResultPictureRequest.getId(), keypointUrl, collect);
//            } catch (IOException e) {
//                log.error("update key points error:{}",e);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public TrainingRecordResultPlanVo getResultByRecordId(long recordId) {
//        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(recordId);
//        log.info("TrainingRecordVo:{}", JSON.toJSONString(trainingRecordVo));
//        if (trainingRecordVo == null) {
//            return null;
//        }
//        TrainingPlanVo trainingPlanVo = trainingPlanService.getTrainingPlanVo(trainingRecordVo.getPlanId());
//        if (trainingPlanVo == null) {
//            return null;
//        }
//
//        LoginVo userLogin = userService.getUserLogin(trainingPlanVo.getPatientId());
//        TrainingRecordResultPlanVo trainingRecordResultPlanVo = new TrainingRecordResultPlanVo();
//        trainingRecordResultPlanVo.setPlanId(trainingPlanVo.getId());
//        trainingRecordResultPlanVo.setTrainingStatus(trainingPlanVo.getTrainingStatus());
//        trainingRecordResultPlanVo.setEndTime(trainingPlanVo.getEndTime());
//        trainingRecordResultPlanVo.setPlanName(trainingPlanVo.getPlanName());
//        trainingRecordResultPlanVo.setStartTime(trainingPlanVo.getStartTime());
//        trainingRecordResultPlanVo.setFrequency(trainingPlanVo.getFrequency());
//        trainingRecordResultPlanVo.setTarget(trainingPlanVo.getTarget());
//        trainingRecordResultPlanVo.setDuration(trainingRecordVo.getDuration());
//
//        if (trainingPlanVo.getTemplateId() != 0) {
//            TrainingPlanTemplate trainingPlanTemplate = trainingPlanTemplateService.getTrainingPlanTemplate(trainingPlanVo.getTemplateId());
//            trainingRecordResultPlanVo.setPlanTemplateName(trainingPlanTemplate.getName());
//        } else {
//            trainingRecordResultPlanVo.setPlanTemplateName(trainingPlanVo.getPlanName());
//        }
//
//        trainingRecordResultPlanVo.setAge(userLogin.getAge());
//        trainingRecordResultPlanVo.setUserId(userLogin.getId());
//        trainingRecordResultPlanVo.setGender(userLogin.getGender());
//        trainingRecordResultPlanVo.setPhone(userLogin.getPhone());
//        trainingRecordResultPlanVo.setUserName(trainingPlanVo.getPatientName());
//        trainingRecordResultPlanVo.setTime(trainingRecordVo.getStartTime());
//        trainingRecordResultPlanVo.setDoctorName(trainingRecordVo.getDoctorName());
//        trainingRecordResultPlanVo.setRemark(trainingRecordVo.getRemark());
//        trainingRecordResultPlanVo.setTrainingRecordId(trainingRecordVo.getId());
//
//        //获取此次训练记录对应的动作记录列表
//        List<TrainingActionRecord> trainingActionRecords =
//                trainingActionRecordService.listTrainingActionRecordByRecordId(trainingRecordVo.getId());
//
//        List<TrainingActionRecordResultListVo> trainingActionRecordResultListVos = new ArrayList<TrainingActionRecordResultListVo>();
//        for (TrainingActionRecord trainingActionRecord : trainingActionRecords) {
//
//            //获取此计划和动作对应的 PlanAction
//            PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(trainingPlanVo.getId(), trainingActionRecord.getActionId());
//
//            //查询此训练动作记录对应的算法分析结果
//            QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
//            wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//            wrapper.eq("training_action_record_id", trainingActionRecord.getId());
//            List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);
//
//            TrainingActionRecordResultListVo trainingActionRecordResultListVo = new TrainingActionRecordResultListVo();
//
//            trainingActionRecordResultListVo.setActionId(trainingActionRecord.getActionId());
//            ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//            trainingActionRecordResultListVo.setActionName(actionVo.getActionName());
//            trainingActionRecordResultListVo.setResultPage(actionVo.getResultPage());
//            trainingActionRecordResultListVo.setTime(DateUtil.getYMD(trainingActionRecord.getStartDay()));
//            trainingActionRecordResultListVo.setTrainingActionRecordId(trainingActionRecord.getId());
//
//            String videos = trainingActionRecord.getVideos();
//            List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
//            PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//
//            TrainingRecordResult trainingRecordResult = null;
//            if (CollectionUtils.isNotEmpty(list)) {
//                trainingRecordResult = list.get(0);
//                PositionFile pf = new PositionFile(positionFile.getPosition(), trainingRecordResult.getDstVideoFileId());
//                if (positionFile != null) {
//                    positionFiles.remove(positionFile);
//                    positionFiles.add(pf);
//                    Collections.swap(positionFiles, positionFiles.indexOf(pf), 0);
//                }
//
//                Long cycle = (trainingActionRecord.getDuration() + 1) / Math.max(planAction.getVideoDuration(), planAction.getEveryDuration());
//                trainingActionRecordResultListVo.setFinishCount(cycle.intValue());
//                trainingActionRecordResultListVo.setPlanCount(planAction.getCycles());
//
//                trainingActionRecordResultListVo.setId(trainingRecordResult.getId());
////                    trainingRecordResultPlanVo.setTrainingRecordResultId(trainingRecordResult.getId());
//
//                if (trainingRecordResult.getFeatureFileId() != null && trainingRecordResult.getFeatureFileId() > 0) {
//                    trainingActionRecordResultListVo.setFeatureFileUrl(fileService.getFileUrl(trainingRecordResult.getFeatureFileId(), false));
//                } else {
//                    trainingActionRecordResultListVo.setFeatureFileUrl("");
//                }
//                if (trainingRecordResult.getKeyPointsFileId() != null && trainingRecordResult.getKeyPointsFileId() > 0) {
//                    trainingActionRecordResultListVo.setKeypointFileUrl(fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), false));
//                } else {
//                    trainingActionRecordResultListVo.setKeypointFileUrl("");
//                }
//            } else {
//                trainingActionRecordResultListVo.setFeatureFileUrl("");
//                trainingActionRecordResultListVo.setKeypointFileUrl("");
////                    trainingRecordResultPlanVo.setTrainingRecordResultId(0l);
//                trainingActionRecordResultListVo.setId(0l);
//                Long cycle = (trainingActionRecord.getDuration() + 1) / Math.max(planAction.getVideoDuration(), planAction.getEveryDuration());
//                trainingActionRecordResultListVo.setFinishCount(cycle.intValue());
//                trainingActionRecordResultListVo.setPlanCount(planAction.getCycles());
//            }
//
//            List<PositionFileVo> collect = positionFiles.stream().map(pff -> convertToPositionFileVo(pff)).collect(Collectors.toList());
//            trainingActionRecordResultListVo.setVideos(collect);
//
//            trainingActionRecordResultListVos.add(trainingActionRecordResultListVo);
//        }
//        trainingRecordResultPlanVo.setRecords(trainingActionRecordResultListVos);
//        return trainingRecordResultPlanVo;
//    }
//
//    @Override
//    public ActionVo getTrainingAction(long id) {
//        TrainingActionRecord trainingActionRecord = trainingActionRecordService.getById(id);
//        ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//        return actionVo;
//    }
//
//    private PictureModelVo convertToPictureModelVo(PictureModel pictureModel) {
//        PictureModelVo vo = new PictureModelVo();
//        vo.setName(pictureModel.getName());
//        vo.setUrl(fileService.getFileUrl(pictureModel.getFileId(), false));
//        return vo;
//    }
//
//    @Override
//    public void resolveGaitAnalysisResult(GaitAnalysisResultNotifyRequest request) {
//        log.info("notify request:{}", JSON.toJSONString(request));
//        TrainingActionRecord trainingActionRecord = trainingActionRecordService.getById(request.getEvId());
//
//        if (trainingActionRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
////        if (TrainingStatus.FINISHED.equals(TrainingStatus.getStatus(trainingActionRecord.getTrainingStatus()))) {
////            return;
////        }
//        GaitAnalysisTaskNotifyHandler notifyHandler = new GaitAnalysisTaskNotifyHandler(trainingActionRecord, request);
//        TaskExecutor.getExecutor().submit(notifyHandler);
//    }
//
//    @Transactional
//    @Override
//    public void resolveResult(TrainingActionRecord trainingActionRecord, GaitAnalysisResultNotifyRequest request) {
//
//        ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//
//        List<GaitAnalysisResult> list = request.getDatas();
//
//        if (request.getStatus() == 0) {
//            for (GaitAnalysisResult gaitAnalysisResult : list) {
//                log.info("result: {}", JSON.toJSONString(gaitAnalysisResult));
//                String dstVideoKey = gaitAnalysisResult.getDstVideoKey();
//                String paramKey = gaitAnalysisResult.getParamKey();
//                String srcVideoKey = gaitAnalysisResult.getSrcVideoKey();
//                String keypointsKey = gaitAnalysisResult.getKeypointsKey();
//                List<String> imgKeys = request.getImgKey();
//
//                //处理图片
////                ArrayList<PictureModel> pictureModels = new ArrayList<>(imgKeys.size());
////                for (String imgKey : imgKeys) {
////                    log.info("move {} to {}", imgKey, FileType.PICTURE.getName());
////                    String imgKeyAfterMove = fileStorage.moveFile(imgKey, FileType.PICTURE);
////                    fileStorage.addWaterMark(FileType.PICTURE.getName(), imgKeyAfterMove);
////                    String imgName = FilenameUtils.getName(imgKeyAfterMove);
////
////                    Files image = new Files();
////                    image.setFilePath(imgKeyAfterMove);
////                    image.setStatus(BaseStatus.NORMAL.getStatus());
////                    image.setRawName(imgName);
////                    image.setOriginalName(imgName);
////                    image.setFileType(FileType.PICTURE.getCode());
////                    image.setCreated(DateUtil.getCurrentTimeStamp());
////                    image.setFileSize(0l);
////                    image.setExtension(FilenameUtils.getExtension(imgName));
////                    image.setUpdated(DateUtil.getCurrentTimeStamp());
////                    fileService.insertFiles(image);
////
////                    pictureModels.add(new PictureModel(imgName, image.getId()));
////                }
//
//                Files distVideo = null;
//                if (StringUtils.isNotBlank(dstVideoKey)) {
//                    log.info("move {} to {}", dstVideoKey, FileType.VIDEO.getName());
//                    String dstVideoKeyAfterMove = fileStorage.moveFile(dstVideoKey, FileType.VIDEO);
//                    fileStorage.convertToM3u8(FileType.VIDEO.getName(), dstVideoKeyAfterMove);
//                    distVideo = new Files();
//                    String distVideoName = FilenameUtils.getName(dstVideoKeyAfterMove);
//                    distVideo.setFilePath(dstVideoKeyAfterMove);
//                    distVideo.setStatus(BaseStatus.NORMAL.getStatus());
//                    distVideo.setRawName(distVideoName);
//                    distVideo.setOriginalName(distVideoName);
//                    distVideo.setFileType(FileType.VIDEO.getCode());
//                    distVideo.setCreated(DateUtil.getCurrentTimeStamp());
//                    distVideo.setFileSize(0l);
//                    distVideo.setExtension(FilenameUtils.getExtension(distVideoName));
//                    distVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(distVideo);
//                }
//
//                Files srcVideo = null;
//                if (StringUtils.isNotBlank(srcVideoKey)) {
//                    log.info("move {} to {}", srcVideoKey, FileType.VIDEO.getName());
//                    String srcVideoKeyAfterMove = fileStorage.moveFile(srcVideoKey, FileType.VIDEO);
//                    fileStorage.convertToM3u8(FileType.VIDEO.getName(), srcVideoKeyAfterMove);
//                    String srcVideoName = FilenameUtils.getName(srcVideoKeyAfterMove);
//                    srcVideo = new Files();
//                    srcVideo.setFilePath(srcVideoKeyAfterMove);
//                    srcVideo.setStatus(BaseStatus.NORMAL.getStatus());
//                    srcVideo.setRawName(srcVideoName);
//                    srcVideo.setOriginalName(srcVideoName);
//                    srcVideo.setFileType(FileType.VIDEO.getCode());
//                    srcVideo.setCreated(DateUtil.getCurrentTimeStamp());
//                    srcVideo.setFileSize(0l);
//                    srcVideo.setExtension(FilenameUtils.getExtension(srcVideoName));
//                    srcVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(srcVideo);
//                }
//
//                int finishCount = 0;
//                int effectCount = 0;
//
//                Files param = null;
//                if (StringUtils.isNotBlank(paramKey)) {
//                    log.info("move {} to {}", paramKey, FileType.JSON.getName());
//                    String paramKeyAfterMove = fileStorage.moveFile(paramKey, FileType.JSON);
//                    String paramName = FilenameUtils.getName(paramKeyAfterMove);
//                    param = new Files();
//                    param.setFilePath(paramKeyAfterMove);
//                    param.setStatus(BaseStatus.NORMAL.getStatus());
//                    param.setRawName(paramName);
//                    param.setOriginalName(paramName);
//                    param.setFileType(FileType.JSON.getCode());
//                    param.setCreated(DateUtil.getCurrentTimeStamp());
//                    param.setFileSize(0l);
//                    param.setExtension(FilenameUtils.getExtension(paramName));
//                    param.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(param);
//
////                    try {
////                        File file = fileService.downloadFile(param.getId());
////                        String content = FileUtils.readFileToString(file, "UTF-8");
////
////                        log.info(" param json file content : {}", content);
////                        JSONObject jsonObject = JSON.parseObject(content);
////                        log.info(" content json : {}", jsonObject.toJSONString());
////                        JSONObject result = jsonObject.getJSONObject(actionVo.getActionCode());
////                        log.info(" key : {}, value : {}", actionVo.getActionCode(), result.toJSONString());
////                        finishCount = result.getInteger("count");
////                        effectCount = result.getInteger("effect_count");
////                    } catch (Exception e) {
////                        log.error("resolve param json file error: {}", e.getMessage());
//////                    throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
////                    }
//                }
//
//                Files keypoints = null;
//                if (StringUtils.isNotBlank(keypointsKey)) {
//                    log.info("move {} to {}", keypointsKey, FileType.JSON.getName());
//                    String keypointsAfterMove = fileStorage.moveFile(keypointsKey, FileType.JSON);
//                    String keypointsName = FilenameUtils.getName(keypointsAfterMove);
//                    keypoints = new Files();
//                    keypoints.setFilePath(keypointsAfterMove);
//                    keypoints.setStatus(BaseStatus.NORMAL.getStatus());
//                    keypoints.setRawName(keypointsName);
//                    keypoints.setOriginalName(keypointsName);
//                    keypoints.setFileType(FileType.JSON.getCode());
//                    keypoints.setCreated(DateUtil.getCurrentTimeStamp());
//                    keypoints.setFileSize(0l);
//                    keypoints.setExtension(FilenameUtils.getExtension(keypointsName));
//                    keypoints.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(keypoints);
//                }
//
//
//                TrainingRecordResult trainingRecordResult = new TrainingRecordResult();
//                trainingRecordResult.setImageCount(gaitAnalysisResult.getImageCount());
//                trainingRecordResult.setCreated(DateUtil.getCurrentTimeStamp());
//                trainingRecordResult.setDoctorId(0l);
//                trainingRecordResult.setDoctorName("");
//                trainingRecordResult.setPosition(request.getPosition());
//                trainingRecordResult.setDstVideoFileId(distVideo.getId());
//                trainingRecordResult.setFeatureFileId(param.getId());
//                trainingRecordResult.setKeyPointsFileId(keypoints.getId());
//                trainingRecordResult.setRemark("");
//                trainingRecordResult.setSrcVideoFileId(srcVideo.getId());
//                trainingRecordResult.setTrainingRecordId(trainingActionRecord.getRecordId());
//                trainingRecordResult.setUpdated(DateUtil.getCurrentTimeStamp());
//                trainingRecordResult.setStatus(BaseStatus.NORMAL.getStatus());
//                trainingRecordResult.setFinishCount(finishCount);
//                trainingRecordResult.setEffectCount(effectCount);
//                trainingRecordResult.setTrainingActionRecordId(request.getEvId());
////                List<PictureModel> sortedPictures = pictureModels.stream().sorted(Comparator.comparing(PictureModel::getName)).collect(Collectors.toList());
//                trainingRecordResult.setPictures("");
//                recordResultDao.insert(trainingRecordResult);
//
//                trainingActionRecord.setTrainingStatus(TrainingStatus.FINISHED.getStatus());
//                trainingActionRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//                trainingActionRecordService.update(trainingActionRecord);
//            }
//            //发送微信模板消息
//            sendWxTemplateMsg(trainingActionRecord);
//        } else if (request.getStatus() == 1) {
//            for (GaitAnalysisResult gaitAnalysisResult : list) {
//                log.info("result: {}", JSON.toJSONString(gaitAnalysisResult));
//                String dstVideoKey = gaitAnalysisResult.getDstVideoKey();
//                String srcVideoKey = gaitAnalysisResult.getSrcVideoKey();
//
//                log.info("move {} to {}", srcVideoKey, FileType.VIDEO.getName());
//                String srcVideoKeyAfterMove = fileStorage.moveFile(srcVideoKey, FileType.VIDEO);
//                fileStorage.convertToM3u8(FileType.VIDEO.getName(), srcVideoKeyAfterMove);
//                String srcVideoName = FilenameUtils.getName(srcVideoKeyAfterMove);
//                Files srcVideo = new Files();
//                srcVideo.setFilePath(srcVideoKeyAfterMove);
//                srcVideo.setStatus(BaseStatus.NORMAL.getStatus());
//                srcVideo.setRawName(srcVideoName);
//                srcVideo.setOriginalName(srcVideoName);
//                srcVideo.setFileType(FileType.VIDEO.getCode());
//                srcVideo.setCreated(DateUtil.getCurrentTimeStamp());
//                srcVideo.setFileSize(0l);
//                srcVideo.setExtension(FilenameUtils.getExtension(srcVideoName));
//                srcVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//                fileService.insertFiles(srcVideo);
//
//                Files distVideo = new Files();
//                if (!dstVideoKey.equalsIgnoreCase(srcVideoKey)) {
//                    log.info("move {} to {}", dstVideoKey, FileType.VIDEO.getName());
//                    String dstVideoKeyAfterMove = fileStorage.moveFile(dstVideoKey, FileType.VIDEO);
//                    fileStorage.convertToM3u8(FileType.VIDEO.getName(), dstVideoKeyAfterMove);
//
//                    String distVideoName = FilenameUtils.getName(dstVideoKeyAfterMove);
//                    distVideo.setFilePath(dstVideoKeyAfterMove);
//                    distVideo.setStatus(BaseStatus.NORMAL.getStatus());
//                    distVideo.setRawName(distVideoName);
//                    distVideo.setOriginalName(distVideoName);
//                    distVideo.setFileType(FileType.VIDEO.getCode());
//                    distVideo.setCreated(DateUtil.getCurrentTimeStamp());
//                    distVideo.setFileSize(0l);
//                    distVideo.setExtension(FilenameUtils.getExtension(distVideoName));
//                    distVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(distVideo);
//                } else {
//                    distVideo = srcVideo;
//                }
//
//                TrainingRecordResult trainingRecordResult = new TrainingRecordResult();
//                trainingRecordResult.setImageCount(gaitAnalysisResult.getImageCount());
//                trainingRecordResult.setCreated(DateUtil.getCurrentTimeStamp());
//                trainingRecordResult.setDoctorId(0l);
//                trainingRecordResult.setDoctorName("");
//                trainingRecordResult.setPosition(request.getPosition());
//                trainingRecordResult.setDstVideoFileId(distVideo.getId());
//                trainingRecordResult.setFeatureFileId(0l);
//                trainingRecordResult.setKeyPointsFileId(0l);
//                trainingRecordResult.setRemark("");
//                trainingRecordResult.setSrcVideoFileId(srcVideo.getId());
//                trainingRecordResult.setTrainingRecordId(trainingActionRecord.getRecordId());
//                trainingRecordResult.setUpdated(DateUtil.getCurrentTimeStamp());
//                trainingRecordResult.setStatus(BaseStatus.NORMAL.getStatus());
//                trainingRecordResult.setFinishCount(0);
//                trainingRecordResult.setEffectCount(0);
//                trainingRecordResult.setTrainingActionRecordId(request.getEvId());
//                trainingRecordResult.setPictures("");
//                recordResultDao.insert(trainingRecordResult);
//
//                trainingActionRecord.setTrainingStatus(TrainingStatus.FINISHED.getStatus());
//                trainingActionRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//                trainingActionRecordService.update(trainingActionRecord);
//            }
//        }
//    }
//
//    @Override
//    public void resolveScaleResult(GaitAnalysisResultNotifyRequest request) {
//
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleRecordService.getScaleEvaluationRecord(request.getEvId());
//
//        List<GaitAnalysisResult> list = request.getDatas();
//
//        if (request.getStatus() == 0) {
//            for (GaitAnalysisResult gaitAnalysisResult : list) {
//                log.info("result: {}", JSON.toJSONString(gaitAnalysisResult));
//                String dstVideoKey = gaitAnalysisResult.getDstVideoKey();
//                String paramKey = gaitAnalysisResult.getParamKey();
//                String srcVideoKey = gaitAnalysisResult.getSrcVideoKey();
//                String keypointsKey = gaitAnalysisResult.getKeypointsKey();
//                List<String> imgKeys = request.getImgKey();
//
//                Files distVideo = null;
//                if (StringUtils.isNotBlank(dstVideoKey)) {
//                    log.info("move {} to {}", dstVideoKey, FileType.VIDEO.getName());
//                    String dstVideoKeyAfterMove = fileStorage.moveFile(dstVideoKey, FileType.VIDEO);
//                    fileStorage.convertToM3u8(FileType.VIDEO.getName(), dstVideoKeyAfterMove);
//                    distVideo = new Files();
//                    String distVideoName = FilenameUtils.getName(dstVideoKeyAfterMove);
//                    distVideo.setFilePath(dstVideoKeyAfterMove);
//                    distVideo.setStatus(BaseStatus.NORMAL.getStatus());
//                    distVideo.setRawName(distVideoName);
//                    distVideo.setOriginalName(distVideoName);
//                    distVideo.setFileType(FileType.VIDEO.getCode());
//                    distVideo.setCreated(DateUtil.getCurrentTimeStamp());
//                    distVideo.setFileSize(0l);
//                    distVideo.setExtension(FilenameUtils.getExtension(distVideoName));
//                    distVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(distVideo);
//                }
//
//                Files srcVideo = null;
//                if (StringUtils.isNotBlank(srcVideoKey)) {
//                    log.info("move {} to {}", srcVideoKey, FileType.VIDEO.getName());
//                    String srcVideoKeyAfterMove = fileStorage.moveFile(srcVideoKey, FileType.VIDEO);
//                    fileStorage.convertToM3u8(FileType.VIDEO.getName(), srcVideoKeyAfterMove);
//                    String srcVideoName = FilenameUtils.getName(srcVideoKeyAfterMove);
//                    srcVideo = new Files();
//                    srcVideo.setFilePath(srcVideoKeyAfterMove);
//                    srcVideo.setStatus(BaseStatus.NORMAL.getStatus());
//                    srcVideo.setRawName(srcVideoName);
//                    srcVideo.setOriginalName(srcVideoName);
//                    srcVideo.setFileType(FileType.VIDEO.getCode());
//                    srcVideo.setCreated(DateUtil.getCurrentTimeStamp());
//                    srcVideo.setFileSize(0l);
//                    srcVideo.setExtension(FilenameUtils.getExtension(srcVideoName));
//                    srcVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(srcVideo);
//                }
//
//                int finishCount = 0;
//                int effectCount = 0;
//
//                Files param = null;
//                if (StringUtils.isNotBlank(paramKey)) {
//                    log.info("move {} to {}", paramKey, FileType.JSON.getName());
//                    String paramKeyAfterMove = fileStorage.moveFile(paramKey, FileType.JSON);
//                    String paramName = FilenameUtils.getName(paramKeyAfterMove);
//                    param = new Files();
//                    param.setFilePath(paramKeyAfterMove);
//                    param.setStatus(BaseStatus.NORMAL.getStatus());
//                    param.setRawName(paramName);
//                    param.setOriginalName(paramName);
//                    param.setFileType(FileType.JSON.getCode());
//                    param.setCreated(DateUtil.getCurrentTimeStamp());
//                    param.setFileSize(0l);
//                    param.setExtension(FilenameUtils.getExtension(paramName));
//                    param.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(param);
//                }
//
//                Files keypoints = null;
//                if (StringUtils.isNotBlank(keypointsKey)) {
//                    log.info("move {} to {}", keypointsKey, FileType.JSON.getName());
//                    String keypointsAfterMove = fileStorage.moveFile(keypointsKey, FileType.JSON);
//                    String keypointsName = FilenameUtils.getName(keypointsAfterMove);
//                    keypoints = new Files();
//                    keypoints.setFilePath(keypointsAfterMove);
//                    keypoints.setStatus(BaseStatus.NORMAL.getStatus());
//                    keypoints.setRawName(keypointsName);
//                    keypoints.setOriginalName(keypointsName);
//                    keypoints.setFileType(FileType.JSON.getCode());
//                    keypoints.setCreated(DateUtil.getCurrentTimeStamp());
//                    keypoints.setFileSize(0l);
//                    keypoints.setExtension(FilenameUtils.getExtension(keypointsName));
//                    keypoints.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(keypoints);
//                }
//
//
//                TrainingRecordResult trainingRecordResult = new TrainingRecordResult();
//                trainingRecordResult.setImageCount(gaitAnalysisResult.getImageCount());
//                trainingRecordResult.setCreated(DateUtil.getCurrentTimeStamp());
//                trainingRecordResult.setDoctorId(0l);
//                trainingRecordResult.setDoctorName("");
//                trainingRecordResult.setPosition(request.getPosition());
//                trainingRecordResult.setDstVideoFileId(distVideo.getId());
//                trainingRecordResult.setFeatureFileId(param == null ? 0l : param.getId());
//                trainingRecordResult.setKeyPointsFileId(keypoints.getId());
//                trainingRecordResult.setRemark("");
//                trainingRecordResult.setSrcVideoFileId(srcVideo.getId());
//                trainingRecordResult.setTrainingRecordId(scaleEvaluationRecord.getId());
//                trainingRecordResult.setUpdated(DateUtil.getCurrentTimeStamp());
//                trainingRecordResult.setStatus(BaseStatus.NORMAL.getStatus());
//                trainingRecordResult.setFinishCount(finishCount);
//                trainingRecordResult.setEffectCount(effectCount);
//                trainingRecordResult.setTrainingActionRecordId(request.getEvId());
////                List<PictureModel> sortedPictures = pictureModels.stream().sorted(Comparator.comparing(PictureModel::getName)).collect(Collectors.toList());
//                trainingRecordResult.setPictures("");
//                recordResultDao.insert(trainingRecordResult);
//            }
//        }
//    }
//
//    @Override
//    public TrainingResultVo getResult(long id) {
//
//        TrainingActionRecord trainingActionRecord = trainingActionRecordService.getById(id);
//        ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//        UserDetailVo user = userService.findUser(trainingActionRecord.getUserId());
//
//        Long recordId = trainingActionRecord.getRecordId();
//        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(recordId);
//
//        QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("training_action_record_id", id);
//        wrapper.orderByDesc("id");
//        List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);
//
//        TrainingResultVo vo = new TrainingResultVo();
//
//        String videos = trainingActionRecord.getVideos();
//        List<PositionFile> positionFiles = JSON.parseArray(videos, PositionFile.class);
//        PositionFile positionFile = actionVideoService.getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//        if (positionFile != null) {
//            Collections.swap(positionFiles, positionFiles.indexOf(positionFile), 0);
//        }
//        List<PositionFileVo> positionFileVos = positionFiles.stream().map(pf -> convertToPositionFileVo(pf)).collect(Collectors.toList());
//        vo.setSrcVideoUrl(positionFileVos);
//
//        vo.setRecordId(recordId);
//
//        if (CollectionUtils.isNotEmpty(list)) {
//            TrainingRecordResult trainingRecordResult = list.get(0);
//
//            for (TrainingRecordResult recordResult : list) {
//                if (recordResult.getPosition().equalsIgnoreCase(actionVo.getAnalysisPosition())) {
//                    trainingRecordResult = recordResult;
//                }
//            }
//
//            if (StringUtils.isNotBlank(trainingRecordResult.getPosition())) {
//                List<VideoResultVo> videoResultVos = list.stream().map(record -> getVideoResultVo(record, actionVo)).collect(Collectors.toList());
//                vo.setResultVideoUrl(videoResultVos);
//            } else {
//                List<VideoResultVo> videoResultVos = new ArrayList<>();
//                VideoResultVo videoResultVo = getVideoResultVo(trainingRecordResult, actionVo);
//                videoResultVos.add(videoResultVo);
//                vo.setResultVideoUrl(videoResultVos);
//            }
//
//            if (trainingRecordResult.getDstVideoFileId() != 0) {
//                vo.setDstVideoUrl(fileService.getFileUrl(trainingRecordResult.getDstVideoFileId(), false));
//            }
//            if (trainingRecordResult.getKeyPointsFileId() != 0) {
//                vo.setKeypointFileUrl(fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), true));
//            }
//            if (trainingRecordResult.getFeatureFileId() != 0) {
//                vo.setFeatureFileUrl(fileService.getFileUrl(trainingRecordResult.getFeatureFileId(), true));
//            }
//            vo.setImageCount(trainingRecordResult.getImageCount());
//            vo.setActionName(actionVo.getActionName());
//            vo.setAge(user.getAge());
//            vo.setDoctorName(trainingRecordResult.getDoctorName());
//            vo.setDuration(DateUtil.secondToHMS(trainingActionRecord.getDuration()));
//            vo.setGender(user.getGender());
//            vo.setId(trainingRecordResult.getId());
//            vo.setName(user.getName());
//            vo.setPhone(user.getPhone());
//            if (trainingRecordVo.getTrainingType() == TrainingType.PLAN.getType()) {
//                Long planId = trainingRecordVo.getPlanId();
//                PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(planId, actionVo.getId());
//                vo.setCycles(planAction.getCycles());
//            } else {
//                vo.setCycles(1);
//            }
//            vo.setRemark(trainingRecordResult.getRemark());
//            vo.setActionCode(actionVo.getActionCode());
////            if (trainingRecordResult.getSrcVideoFileId() != 0) {
////                vo.setSrcVideoUrl(fileService.getFileUrl(trainingRecordResult.getSrcVideoFileId(), false));
////            }
//            vo.setTrainingTime(DateUtil.getYMDHMSDate(trainingActionRecord.getStartTime()));
//            vo.setAnalysisPosition(actionVo.getAnalysisPosition());
//        } else {
//            //TODO
//            vo.setImageCount(0);
//            vo.setActionName(actionVo.getActionName());
//            vo.setAge(user.getAge());
//            vo.setDoctorName("");
//            vo.setActionCode(actionVo.getActionCode());
//            vo.setDuration(DateUtil.secondToHMS(trainingActionRecord.getDuration()));
//            vo.setFeatureFileUrl("");
//            vo.setKeypointFileUrl("");
//            vo.setGender(user.getGender());
//            if (trainingRecordVo.getTrainingType() == TrainingType.PLAN.getType()) {
//                Long planId = trainingRecordVo.getPlanId();
//                PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(planId, actionVo.getId());
//                vo.setCycles(planAction.getCycles());
//            } else {
//                vo.setCycles(1);
//            }
//            vo.setId(0l);
//            vo.setName(user.getName());
//            vo.setPhone(user.getPhone());
//            vo.setRemark("");
////            vo.setSrcVideoUrl(fileService.getFileUrl(positionFile.getFileId(), false));
//            vo.setDstVideoUrl(fileService.getFileUrl(positionFile.getFileId(), false));
//            vo.setTrainingTime(DateUtil.getYMDHMSDate(trainingActionRecord.getStartTime()));
//            vo.setAnalysisPosition(actionVo.getAnalysisPosition());
//        }
//        return vo;
//    }
//
//    @Override
//    public TrainingResultVo getMockResult(long id) {
//        id = 2058;
//        TrainingActionRecord trainingActionRecord = trainingActionRecordService.getById(id);
//        ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//        UserDetailVo user = userService.findUser(trainingActionRecord.getUserId());
//
//        Long recordId = trainingActionRecord.getRecordId();
//        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(recordId);
//
//        QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("training_action_record_id", id);
//        wrapper.orderByDesc("id");
//        List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);
//
//        TrainingResultVo vo = new TrainingResultVo();
//        vo.setRecordId(recordId);
//
//        TrainingRecordResult trainingRecordResult = list.get(0);
//
//        ArrayList<PositionFileVo> positionFileVos = new ArrayList<>();
//        PositionFileVo positionFileVo = new PositionFileVo();
//        positionFileVo.setPosition("Anterior");
//        positionFileVo.setUrl(fileService.getFileUrl(trainingRecordResult.getSrcVideoFileId(), false));
//        positionFileVos.add(positionFileVo);
//        vo.setSrcVideoUrl(positionFileVos);
//
//        if (StringUtils.isNotBlank(trainingRecordResult.getPosition())) {
//            List<VideoResultVo> videoResultVos = list.stream().map(record -> getVideoResultVo(record, actionVo)).collect(Collectors.toList());
//            vo.setResultVideoUrl(videoResultVos);
//        } else {
//            List<VideoResultVo> videoResultVos = new ArrayList<>();
//            VideoResultVo videoResultVo = getVideoResultVo(trainingRecordResult, actionVo);
//            videoResultVo.setPosition("Anterior");
//            videoResultVos.add(videoResultVo);
//            vo.setResultVideoUrl(videoResultVos);
//        }
//
//        if (trainingRecordResult.getDstVideoFileId() != 0) {
//            vo.setDstVideoUrl(fileService.getFileUrl(trainingRecordResult.getDstVideoFileId(), false));
//        }
//        if (trainingRecordResult.getKeyPointsFileId() != 0) {
//            vo.setKeypointFileUrl(fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), true));
//        }
//        if (trainingRecordResult.getFeatureFileId() != 0) {
//            vo.setFeatureFileUrl(fileService.getFileUrl(trainingRecordResult.getFeatureFileId(), true));
//        }
//        vo.setImageCount(trainingRecordResult.getImageCount());
//        vo.setActionName(actionVo.getActionName());
//        vo.setAge(user.getAge());
//        vo.setDoctorName(trainingRecordResult.getDoctorName());
//        vo.setDuration(DateUtil.secondToHMS(trainingActionRecord.getDuration()));
//        vo.setGender(user.getGender());
//        vo.setId(trainingRecordResult.getId());
//        vo.setName(user.getName());
//        vo.setPhone(user.getPhone());
//        if (trainingRecordVo.getTrainingType() == TrainingType.PLAN.getType()) {
//            Long planId = trainingRecordVo.getPlanId();
//            PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(planId, actionVo.getId());
//            vo.setCycles(planAction.getCycles());
//        } else {
//            vo.setCycles(1);
//        }
//        vo.setRemark(trainingRecordResult.getRemark());
//        vo.setActionCode(actionVo.getActionCode());
////            if (trainingRecordResult.getSrcVideoFileId() != 0) {
////                vo.setSrcVideoUrl(fileService.getFileUrl(trainingRecordResult.getSrcVideoFileId(), false));
////            }
//        vo.setTrainingTime(DateUtil.getYMDHMSDate(trainingActionRecord.getStartTime()));
//        vo.setAnalysisPosition("Anterior");
//        return vo;
//    }
//
    @Override
    public TrainingResultVo getScaleRecordResult(long id) {

        ScaleEvaluationRecord scaleEvaluationRecord = scaleRecordService.getScaleEvaluationRecord(id);
        if (scaleEvaluationRecord == null) {
            return null;
        }

        ActionVo actionVo = actionService.getActionVo(1620l);
//        String answerWithRemark = scaleEvaluationRecord.getAnswerWithRemark();
//        if (StringUtils.isNotBlank(answerWithRemark)) {
//            List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//            if (CollectionUtils.isNotEmpty(answerWithRemarkDtos)) {
//                AnswerWithRemarkDto answerWithRemarkDto = answerWithRemarkDtos.get(0);
//                if (answerWithRemarkDto != null) {
//                    List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
//                    if (CollectionUtils.isNotEmpty(attachmentDtos)) {
//                        AttachmentDto attachmentDto = attachmentDtos.get(0);
//                        if (attachmentDto != null) {
//                            Long fileId = attachmentDto.getFileId();
//                        }
//                    }
//                }
//            }
//
//        }
//        id = 2058;
//        UserDetailVo user = userService.findUser(scaleEvaluationRecord.getUserId());
        ChildrenVo children = childrenService.getChildren(scaleEvaluationRecord.getChildrenId());
        SysUser user = userService.getUser(scaleEvaluationRecord.getUserId());

        Long recordId = scaleEvaluationRecord.getId();

        QueryWrapper<TrainingRecordResult> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
        wrapper.eq("training_action_record_id", id);
        wrapper.orderByDesc("id");
        List<TrainingRecordResult> list = recordResultDao.selectList(wrapper);

        TrainingResultVo vo = new TrainingResultVo();
        vo.setRecordId(recordId);

        TrainingRecordResult trainingRecordResult = list.get(0);

        ArrayList<PositionFileVo> positionFileVos = new ArrayList<>();
        PositionFileVo positionFileVo = new PositionFileVo();
        positionFileVo.setPosition("Anterior");
        positionFileVo.setUrl(fileService.getFileUrl(trainingRecordResult.getSrcVideoFileId(), false));
        positionFileVos.add(positionFileVo);
        vo.setSrcVideoUrl(positionFileVos);

        if (StringUtils.isNotBlank(trainingRecordResult.getPosition())) {
            List<VideoResultVo> videoResultVos = list.stream().map(record -> getVideoResultVo(record, actionVo)).collect(Collectors.toList());
            vo.setResultVideoUrl(videoResultVos);
        } else {
            List<VideoResultVo> videoResultVos = new ArrayList<>();
            VideoResultVo videoResultVo = getVideoResultVo(trainingRecordResult, actionVo);
            videoResultVo.setPosition("Anterior");
            videoResultVos.add(videoResultVo);
            vo.setResultVideoUrl(videoResultVos);
        }

        if (trainingRecordResult.getDstVideoFileId() != 0) {
            vo.setDstVideoUrl(fileService.getFileUrl(trainingRecordResult.getDstVideoFileId(), false));
        }
        if (trainingRecordResult.getKeyPointsFileId() != 0) {
            vo.setKeypointFileUrl(fileService.getFileUrl(trainingRecordResult.getKeyPointsFileId(), true));
        }
        if (trainingRecordResult.getFeatureFileId() != 0) {
            vo.setFeatureFileUrl(fileService.getFileUrl(trainingRecordResult.getFeatureFileId(), true));
        }
        vo.setImageCount(trainingRecordResult.getImageCount());
        vo.setActionName(actionVo.getActionName());
        vo.setAge(0);
        vo.setDoctorName(trainingRecordResult.getDoctorName());
        vo.setDuration(OldDateUtil.secondToHMS(0L));
        vo.setGender(children.getGender());
        vo.setId(trainingRecordResult.getId());
        vo.setName(children.getName());
        vo.setPhone(user.getPhone());
//        if (trainingRecordVo.getTrainingType() == TrainingType.PLAN.getType()) {
//            Long planId = trainingRecordVo.getPlanId();
//            PlanAction planAction = planActionService.getPlanActionByPlanIdAndActionId(planId, actionVo.getId());
//            vo.setCycles(planAction.getCycles());
//        } else {
//            vo.setCycles(1);
//        }
        vo.setCycles(1);
        vo.setRemark(trainingRecordResult.getRemark());
        vo.setActionCode(actionVo.getActionCode());
//            if (trainingRecordResult.getSrcVideoFileId() != 0) {
//                vo.setSrcVideoUrl(fileService.getFileUrl(trainingRecordResult.getSrcVideoFileId(), false));
//            }
        vo.setTrainingTime(OldDateUtil.getYMDHMSDate(0L));
        vo.setAnalysisPosition("Anterior");
        return vo;
    }
//
//    //
//    @Override
//    public void updateResult(UpdateRecordResultRequest request) {
//        TrainingRecordResult trainingRecordResult = recordResultDao.selectById(request.getId());
//        if (trainingRecordResult == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        trainingRecordResult.setRemark(request.getRemark());
//        trainingRecordResult.setDoctorName(request.getDoctorName());
//        recordResultDao.updateById(trainingRecordResult);
//    }
//
//    private PositionFileVo convertToPositionFileVo(PositionFile positionFile) {
//        PositionFileVo vo = new PositionFileVo();
//        vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
//        vo.setPosition(positionFile.getPosition());
//        return vo;
//    }
//
    private VideoResultVo getVideoResultVo(TrainingRecordResult recordResult, ActionVo actionVo) {
        VideoResultVo vo = new VideoResultVo();
        if (StringUtils.isBlank(recordResult.getPosition())) {
            vo.setPosition(actionVo.getAnalysisPosition());
        } else {
            vo.setPosition(recordResult.getPosition());
        }
        if (recordResult.getDstVideoFileId() != 0) {
            vo.setUrl(fileService.getFileUrl(recordResult.getDstVideoFileId(), false));
        } else {
            vo.setUrl("");
        }
        if (recordResult.getKeyPointsFileId() != 0) {
            vo.setKeypointFileUrl(fileService.getFileUrl(recordResult.getKeyPointsFileId(), true));
        } else {
            vo.setKeypointFileUrl("");
        }
        if (recordResult.getFeatureFileId() != 0) {
            vo.setFeatureFileUrl(fileService.getFileUrl(recordResult.getFeatureFileId(), true));
        } else {
            vo.setFeatureFileUrl("");
        }
        return vo;
    }
//
//    private void sendWxTemplateMsg(TrainingActionRecord trainingActionRecord) {
//        LoginVo user = userService.getUserLogin(trainingActionRecord.getUserId());
//
//        //发送模板消息
//        if (StringUtils.isNotBlank(user.getOpenId())) {
//            TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordVo(trainingActionRecord.getRecordId());
//            ActionVo actionVo = actionService.getActionVo(trainingActionRecord.getActionId());
//
//            ArrayList<WxMpTemplateData> list = new ArrayList<>();
//            list.add(new WxMpTemplateData("first", "您的专属报告已生成，请查看！", "#173177"));
//            list.add(new WxMpTemplateData("keyword1", user.getOrganizationName(), "#173177"));
//            list.add(new WxMpTemplateData("keyword2", trainingRecordVo.getPlanName(), "#173177"));
//            list.add(new WxMpTemplateData("keyword3", actionVo.getActionName() , "#173177"));
//            list.add(new WxMpTemplateData("keyword4", (trainingActionRecord.getDuration() / 60 + 1) + "分钟" , "#173177"));
//            list.add(new WxMpTemplateData("keyword5", DateUtil.getYMDHMSDate(trainingActionRecord.getCreated()) , "#173177"));
//            list.add(new WxMpTemplateData("remark", "复数健康，脑科学数字化精准康复变革者！", "#173177"));
//
//            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
//                    .toUser(user.getOpenId()).templateId(generateReport).data(list).url(reportUrl + trainingActionRecord.getRecordId()).build();
//            try {
//                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
//            } catch (WxErrorException e) {
//                log.error("发送生成报告微信模板消息失败,code:{}, msg{}", e.getError().getErrorCode(), e.getError().getErrorMsg());
//            }
//        }
//    }
}
