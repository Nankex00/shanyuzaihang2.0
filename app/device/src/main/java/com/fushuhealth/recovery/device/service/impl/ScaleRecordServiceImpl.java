package com.fushuhealth.recovery.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.Gender;
import com.fushuhealth.recovery.common.constant.QuestionSubjectEnum;
import com.fushuhealth.recovery.common.constant.ScaleStatus;
import com.fushuhealth.recovery.common.constant.ScaleTableConstant;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.dal.dao.ScaleEvaluationRecordDao;
import com.fushuhealth.recovery.dal.dto.*;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.Files;
import com.fushuhealth.recovery.dal.entity.ScaleEvaluationRecord;
import com.fushuhealth.recovery.dal.entity.ScaleTable;
import com.fushuhealth.recovery.dal.scale.ScaleTableResolver;
import com.fushuhealth.recovery.dal.vo.*;
import com.fushuhealth.recovery.device.service.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScaleRecordServiceImpl extends ServiceImpl<ScaleEvaluationRecordDao, ScaleEvaluationRecord> implements ScaleRecordService {

    private final static Logger log = LoggerFactory.getLogger(ScaleRecordServiceImpl.class);
//
    @Autowired
    private ScaleEvaluationRecordDao scaleEvaluationRecordDao;
//
    @Autowired
    private ISysUserService userService;
//
//    @Autowired
//    private DoctorService doctorService;
//
    @Autowired
    private FileService fileService;
//
//    @Autowired
//    private WxMpService wxMpService;
//
//    @Autowired
//    private WxUserService wxUserService;
//
//    @Autowired
//    private WxMaService wxMaService;
//
//    @Autowired
//    private ScaleTableService scaleTableService;
//
    @Autowired
    private IChildrenService childrenService;

    @Autowired
    private IRisksService risksService;
//
//    @Autowired
//    private ReserveService reserveService;
//
//    @Autowired
//    private ReportInfo reportInfo;
//
//    @Autowired
//    private ScaleEvaluateLogService scaleEvaluateLogService;
//
//    @Autowired
//    private RecoveryTrainingRecordService recoveryTrainingRecordService;
//
//    @Autowired
//    private WorkScheduleService workScheduleService;
//
//    @Autowired
//    private OrganizationService organizationService;
//
//    @Autowired
//    private SmsSender smsSender;
//
//    @Autowired
//    private FileStorage fileStorage;
//
//    @Autowired
//    private ScaleOrderDao scaleOrderDao;
//
//    @Autowired
//    private ScaleOrderService scaleOrderService;
//
//    @Value("${wx.template.scale-feedback}")
//    private String scaleFeedbackTemplateId;
//
//    @Value("${wx.template.scale-feedback-url}")
//    private String scaleFeedbackUrl;
//
//    @Value("${spring.profiles.active}")
//    private String env;
//
    private static String resultFilePath;
//
//    @Value("${scale-table.result.cerebralPalsyPath:}")
//    public void setResultFilePath(String filePath) {
//        resultFilePath = filePath;
//    }
//
    private static String abnormalVideoFilePath;

    @Value("${scale-table.actionVideoFilePath:}")
    public void setAbnormalVideoFilePath(String filePath) {
        abnormalVideoFilePath = filePath;
    }
//
    private static Map<String, CerebralPalsyScaleEvaluateResult> suggestMap;
//
    private static Map<String, AbnormalActionVideo> abnormalActionVideoMap;
//
    @PostConstruct
    public void init() {
        suggestMap = new HashMap<>();
        abnormalActionVideoMap = new HashMap<>();
        if (StringUtils.isNotBlank(resultFilePath)) {
            File file = new File(resultFilePath);
            try {
                final String content = FileUtils.readFileToString(file, "UTF-8");
                List<CerebralPalsyScaleEvaluateResult> list = JSON.parseArray(content, CerebralPalsyScaleEvaluateResult.class);
                for (CerebralPalsyScaleEvaluateResult result : list) {
                    suggestMap.put(result.getResult(), result);
                }

                final String videoContent = FileUtils.readFileToString(new File(abnormalVideoFilePath), "UTF-8");
                List<AbnormalActionVideo> abnormalActionVideos = JSON.parseArray(videoContent, AbnormalActionVideo.class);
                for (AbnormalActionVideo abnormalActionVideo : abnormalActionVideos) {
                    abnormalActionVideoMap.put(abnormalActionVideo.getName(), abnormalActionVideo);
                }
            } catch (Exception e) {
                log.error("parse Cerebral Palsy Result map error : {}", e);
            }
        }

    }
//
//    @Override
//    public ListScaleRecordResponse queryScaleRecord(int pageNo, int pageSize, LoginVo loginVo, ScaleRecordQuery query) {
//
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("organization_id", AuthContext.getUser().getOrganizationId());
//        wrapper.eq("reserve_type", WorkScheduleType.AI_EVALUATE.getType());
//
//        if (query.getUserId() != 0) {
//            wrapper.eq("user_id", query.getUserId());
//        }
//        wrapper.ne("data_status", DataStatus.WAIT_UPLOAD.getStatus());
////        else {
////            wrapper.ne("data_status", DataStatus.WAIT_UPLOAD.getStatus());
////        }
//
////        if(query.getUserId() == 0) {
////            List<Long> userIds = userService.queryUserIds(loginVo.getId(), loginVo.getOrganizationId(), query.getKeyword());
////
////            if (StringUtils.isNotBlank(query.getKeyword())) {
////                List<Long> userIds = userService.queryUserIds(loginVo.getId(), loginVo.getOrganizationId(), query.getKeyword());
////                wrapper.in("user_id", userIds);
////            }
//////            if (CollectionUtils.isNotEmpty(userIds)) {
//////                wrapper.in("user_id", userIds);
//////            }
////        } else {
////            wrapper.eq("user_id", query.getUserId());
////        }
//        if (StringUtils.isNotBlank(query.getKeyword())) {
//            List<Long> userIds = userService.queryUserIds(loginVo.getId(), loginVo.getOrganizationId(), query.getKeyword());
//            List<Long> childrenIds = childrenService.queryChildrenIdByKeywordAndOrganizationId(query.getKeyword(), loginVo.getOrganizationId());
//
//            wrapper.and(q -> {
//                q.in("user_id", CollectionUtils.isNotEmpty(userIds) ? userIds : Arrays.asList(0l))
//                        .or().in("children_id", CollectionUtils.isNotEmpty(childrenIds) ? childrenIds : Arrays.asList(0l));
//            });
//
////            if (CollectionUtils.isNotEmpty(userIds)) {
////                wrapper.in("user_id", userIds);
////            } else {
////                wrapper.in("user_id", Arrays.asList(0l));
////            }
//        }
//        DoctorVo doctorVo = doctorService.getDoctorVo(loginVo.getId());
//        if (doctorVo.getDataPermission() == DataPermission.SELF.getCode()) {
//            LambdaQueryWrapper<ScaleOrder> orderQueryWrapper = new QueryWrapper<ScaleOrder>().lambda()
//                    .eq(ScaleOrder::getDoctorId, AuthContext.getUser().getId());
//            List<ScaleOrder> list = scaleOrderService.list(orderQueryWrapper);
//            if (CollectionUtils.isNotEmpty(list)) {
//                List<Long> scaleOrderIds = list.stream().map(ScaleOrder::getId).collect(Collectors.toList());
//                wrapper.in("order_id", scaleOrderIds);
//            } else {
//                PageVo pageVo = PageVo.builder()
//                        .page(1L).totalPage(1l).totalRecord(0).build();
//                return new ListScaleRecordResponse(pageVo, Collections.EMPTY_LIST);
//            }
//        }
////        String roleName = AuthContext.getUser().getRoleName();
////        if (roleName.equals(RoleEnum.ROLE_DOCTOR.getName())) {
////            LambdaQueryWrapper<ScaleOrder> orderQueryWrapper = new QueryWrapper<ScaleOrder>().lambda()
////                    .eq(ScaleOrder::getDoctorId, AuthContext.getUser().getId());
////            List<ScaleOrder> list = scaleOrderService.list(orderQueryWrapper);
////            if (CollectionUtils.isNotEmpty(list)) {
////                List<Long> scaleOrderIds = list.stream().map(ScaleOrder::getId).collect(Collectors.toList());
////                wrapper.in("order_id", scaleOrderIds);
////            } else {
////                PageVo pageVo = PageVo.builder()
////                        .page(1L).totalPage(1l).totalRecord(0).build();
////                return new ListScaleRecordResponse(pageVo, Collections.EMPTY_LIST);
////            }
////        }
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        if (query.getScaleCode() != 0) {
//            wrapper.eq("scale_table_code", query.getScaleCode());
//        }
//
//        if (query.getStatus() != 0) {
//            if (query.getStatus() == ScaleStatus.NOT_EVALUATE.getStatus()) {
//                wrapper.in("progress_status", Arrays.asList(ScaleStatus.NOT_EVALUATE.getStatus(), ScaleStatus.REVIEW_NOT_PASS.getStatus()));
//            } else {
//                wrapper.eq("progress_status", query.getStatus());
//            }
//        }
//
//        if (query.getStart() > 0 && query.getEnd() > 0 && query.getEnd() > query.getStart()) {
//            wrapper.and(i -> i.ge("created", query.getStart()).le("created", query.getEnd()));
//        }
//        wrapper.orderByDesc("created");
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//        List<ScaleRecordListVo> list = records.stream().map(record -> convertToScaleRecordListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListScaleRecordResponse(pageVo, list);
//    }
//
//    @Override
//    public ListScaleRecordResponse queryScaleRecord(int pageNo, int pageSize, Long childrenId) {
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        LambdaQueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<ScaleEvaluationRecord>().lambda();
//        wrapper.eq(ScaleEvaluationRecord::getReserveType, WorkScheduleType.AI_EVALUATE.getType())
//                .eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
//                .eq(ScaleEvaluationRecord::getChildrenId, childrenId)
//                .orderByDesc(ScaleEvaluationRecord::getCreated);
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//        List<ScaleRecordListVo> list = records.stream().map(record -> convertToScaleRecordListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListScaleRecordResponse(pageVo, list);
//    }
//
    @Override
    public ScaleRecordVo getScaleRecordVo(long id, byte type, long fileId) {
        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
        if (record == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }

        Children children = childrenService.getChildrenById(record.getChildrenId());
//        ChildrenVo children = childrenService.getChildren(record.getChildrenId());

        SysUser user = userService.getUser(record.getUserId());

        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());

        if(record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode()){
//                    BaseScaleEvaluationResult baseScaleEvaluationResult = getReport(record, children);
//                    vo.setResult(baseScaleEvaluationResult);
            //TODO 补全格里菲斯报告

            GriffithsResult griffithsResult = (GriffithsResult)getReport(record, children);

            GriffithsRecordVo vo = new GriffithsRecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode());
            vo.setScaleTableName(ScaleTableConstant.ScaleTableCode.GRIFFITHS.getDesc());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            vo.setResult(griffithsResult);
            return vo;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.WEISHI_TODDLER_INTELLIGENCE.getCode()) {
            WeishiToddlerIntelligenceResult result = (WeishiToddlerIntelligenceResult) getReport(record, children);
            WeishiToddlerIntelligenceRecordVo vo = new WeishiToddlerIntelligenceRecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(ScaleTableConstant.ScaleTableCode.WEISHI_TODDLER_INTELLIGENCE.getCode());
            vo.setScaleTableName(ScaleTableConstant.ScaleTableCode.WEISHI_TODDLER_INTELLIGENCE.getDesc());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            vo.setResult(result);
            return vo;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.WEISHI_CHILD_INTELLIGENCE.getCode()) {
            WeishiChildIntelligenceResult result = (WeishiChildIntelligenceResult) getReport(record, children);
            WeishiChildIntelligenceRecordVo vo = new WeishiChildIntelligenceRecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(ScaleTableConstant.ScaleTableCode.WEISHI_CHILD_INTELLIGENCE.getCode());
            vo.setScaleTableName(ScaleTableConstant.ScaleTableCode.WEISHI_CHILD_INTELLIGENCE.getDesc());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            vo.setResult(result);
            return vo;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_8.getCode()) {
            GMs8Result result = (GMs8Result) getReport(record, children);
            GMs8RecordVo vo = new GMs8RecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(ScaleTableConstant.ScaleTableCode.GMS_8.getCode());
            vo.setScaleTableName(ScaleTableConstant.ScaleTableCode.GMS_8.getDesc());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            String answerWithRemark = record.getAnswerWithRemark();
            List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);

            if (CollectionUtils.isNotEmpty(answers)) {
                AnswerWithRemarkDto answerWithRemarkDto = answers.get(0);
                List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
                if (attachmentDtos != null) {
                    for (AttachmentDto attachmentDto : attachmentDtos) {
                        Long videoId = attachmentDto.getFileId();
                        Files file = fileService.getFile(videoId);
                        if (file.getFileType() == OldFileType.VIDEO.getCode()) {
                            vo.setCoverUrl(attachmentDto.getCoverFileId() == null || attachmentDto.getCoverFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getCoverFileId(), false));
                            vo.setVideoUrl(fileService.getFileUrl(attachmentDto.getFileId(), false));
                            vo.setVideoId(attachmentDto.getFileId());
                        }
                    }
                }
            } else {
                vo.setCoverUrl("");
                vo.setVideoUrl("");
                vo.setVideoId(0l);
            }
            vo.setResult(result);
            return vo;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMFM88.getCode()) {
            GMFM88Result result = (GMFM88Result) getReport(record, children);
            GMFG88RecordVo vo = new GMFG88RecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(ScaleTableConstant.ScaleTableCode.GMFM88.getCode());
            vo.setScaleTableName(ScaleTableConstant.ScaleTableCode.GMFM88.getDesc());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            vo.setResult(result);
            return vo;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.CAREGIVER_BURDEN.getCode()) {
            CaregiverBurdenResult result = (CaregiverBurdenResult) getReport(record, children);
            CaregiverBurdenRecordVo vo = new CaregiverBurdenRecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(ScaleTableConstant.ScaleTableCode.CAREGIVER_BURDEN.getCode());
            vo.setScaleTableName(ScaleTableConstant.ScaleTableCode.CAREGIVER_BURDEN.getDesc());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            vo.setResult(result);
            return vo;
        }

        List<ScaleTableConstant.ScaleTableCode> scaleTables = ScaleTableConstant.ScaleTableCode.getScaleTableByClassification(ScaleTableConstant.ScaleTableClassification.DDST);
        Set<Byte> scaleTableCodesSet = scaleTables.stream().map(ScaleTableConstant.ScaleTableCode::getCode).collect(Collectors.toSet());
        if (scaleTableCodesSet.contains(record.getScaleTableCode())) {

            DDSTRecordVo vo = new DDSTRecordVo();
            DDSTResult result = (DDSTResult) getReport(record, children);
            vo.setUserScore(record.getUserScore());
            vo.setChildrenId(children.getId());
            vo.setPhone(user.getPhone());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(scaleTable.getCode());
            vo.setScaleTableName(scaleTable.getName());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            vo.setChildRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfChild()).split(",")));
            vo.setMotherRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfMother()).split(",")));
            vo.setExtraRisks(children.getExtraRisks());
            String correctAge = "";
            long week = 0;
            long day = 0;
            long days = OldDateUtil.getDaysBetweenTime(record.getCreated(), children.getBirthday());
            if (children.getGestationalWeeks() >= 37) {
                week = days / 7;
                day = days % 7;
                correctAge = week + "周" + day + "天";
            } else {
                week = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) / 7;
                day = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) % 7;
                correctAge = week + "周" + day + "天";
            }
            vo.setCorrectAge(correctAge);
            vo.setResult(result);
            List<QuestionDto> questions = scaleTable.getQuestions();
            String answerWithRemark = record.getAnswerWithRemark();
            List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
            if (CollectionUtils.isEmpty(answers)) {
                vo.setAnswers(Collections.EMPTY_LIST);
            } else {
                ArrayList<ScaleRecordQuestionVo> scaleRecordQuestionVos = new ArrayList<>(answers.size());
                for (AnswerWithRemarkDto answer : answers) {
                    ScaleRecordQuestionVo scaleRecordQuestionVo = new ScaleRecordQuestionVo();
                    QuestionDto questionDto = getQuestionDto(questions, answer.getQuestionSn());

                    scaleRecordQuestionVo.setAnswer(Collections.EMPTY_LIST);
                    scaleRecordQuestionVo.setUserScore(0);
                    scaleRecordQuestionVo.setDoctorScore(0);
                    scaleRecordQuestionVo.setId(questionDto.getSn());
                    scaleRecordQuestionVo.setName(questionDto.getName());
                    scaleRecordQuestionVo.setRemark(answer.getRemark());
                    scaleRecordQuestionVo.setType(QuestionSubjectEnum.getQuestionSubject(questionDto.getSubject()).getDesc());


                    //设置附件
                    List<AttachmentDto> attachmentDtos = answer.getAttachmentDtos();
                    if (CollectionUtils.isNotEmpty(attachmentDtos)) {
                        ArrayList<AttachmentVo> attachmentVos = new ArrayList<>(attachmentDtos.size());
                        for (AttachmentDto attachmentDto : attachmentDtos) {
                            AttachmentVo attachmentVo = new AttachmentVo();
                            attachmentVo.setFileId(attachmentDto.getFileId());
                            Files file = fileService.getFile(attachmentDto.getFileId());
                            Byte fileType = file.getFileType();
                            if (attachmentDto.getCoverFileId() != 0) {
                                String coverUrl = fileService.getFileUrl(attachmentDto.getCoverFileId(), false);
                                attachmentVo.setCoverUrl(coverUrl);
                            } else {
                                attachmentVo.setCoverUrl(null);
                            }
                            attachmentVo.setType(fileType);
                            boolean origin = false;
                            if (fileType == OldFileType.AUDIO.getCode()) {
                                origin = true;
                            }
                            attachmentVo.setUrl(fileService.getFileUrl(file.getId(), origin));
                            attachmentVo.setPosition(StringUtils.isBlank(attachmentDto.getPosition()) ? "" : attachmentDto.getPosition());
                            attachmentVo.setVideoType(StringUtils.isBlank(attachmentDto.getVideoType()) ? "" : attachmentDto.getVideoType());
                            attachmentVos.add(attachmentVo);
                        }
                        scaleRecordQuestionVo.setAttachments(attachmentVos);
                    } else {
                        scaleRecordQuestionVo.setAttachments(new ArrayList<>());
                    }

                    scaleRecordQuestionVos.add(scaleRecordQuestionVo);
                }
                List<ScaleRecordQuestionVo> collect = scaleRecordQuestionVos.stream().sorted(Comparator.comparing(ScaleRecordQuestionVo::getId)).collect(Collectors.toList());
                vo.setAnswers(collect);
            }
            return vo;
        }


        if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
            return getScaleRecordVo((byte)1, record, children, scaleTable, fileId);
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS.getCode()) {
            return getScaleRecordVo((byte)2, record, children, scaleTable, fileId);
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
            return getScaleRecordVo(type, record, children, scaleTable, fileId);
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            return getScaleRecordVo(type, record, children, scaleTable, fileId);
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
            return getScaleRecordVo(type, record, children, scaleTable, fileId);
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode()) {
            return getScaleRecordVo(type, record, children, scaleTable, fileId);
        }else {
            ScaleRecordVo vo = new ScaleRecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setAge(OldDateUtil.getAge(record.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMDHMSDate(record.getBirthday()));
            vo.setBirthWeight(children.getBirthWeight());
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(record.getGender()).getDesc());
            vo.setName(children.getName());
            vo.setChildrenId(children.getId());
            vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(scaleTable.getCode());
            vo.setScaleTableName(scaleTable.getName());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setChildRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfChild()).split(",")));
            vo.setMotherRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfMother()).split(",")));
            vo.setExtraRisks(children.getExtraRisks());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            addAnswers(vo, scaleTable, record);
            return vo;
        }

    }
//
//    @Override
//    public ScaleEvaluationRecord getScaleEvaluationRecord(long id) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(id);
//        if (scaleEvaluationRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        return scaleEvaluationRecord;
//    }
//
    private ScaleRecordVo getScaleRecordVo(byte type, ScaleEvaluationRecord record, Children children, ScaleTable scaleTable, long fileId) {
        if (type == 1) {
            CerebralPalsyRecordVo vo = new CerebralPalsyRecordVo();
            vo.setUserScore(record.getUserScore());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getGender()).getDesc());
            vo.setName(children.getName());
            vo.setMedicalNumber(children.getMedicalCardNumber());

            SysUser user = userService.getUser(record.getUserId());
            vo.setPhone(user.getPhone());
            String result = record.getResult();
            if (StringUtils.isNotBlank(result)) {
                if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
                    vo.setResult(JSON.parseObject(result, GMsAndCerebralPalsyResult.class).getCerebralPalsyResult());
                } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()){
                    LeiBoAllBodyTestResult leiBoAllBodyTestResult = (LeiBoAllBodyTestResult) getReport(record, children);
                    List<PositionAndSportIterm> positionAndSportAbnormal = leiBoAllBodyTestResult.getCerebralPalsyResult().getPositionAndSportAbnormal();
                    List<PositionAndSportIterm> collect = positionAndSportAbnormal.stream().sorted(Comparator.comparing(PositionAndSportIterm::getQuestionSn)).collect(Collectors.toList());
                    leiBoAllBodyTestResult.getCerebralPalsyResult().setPositionAndSportAbnormal(collect);
                    vo.setResult(leiBoAllBodyTestResult);
                } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()){
                    LeiBoCerebralPalsySelfTestResult report = (LeiBoCerebralPalsySelfTestResult) getReport(record, children);
                    List<PositionAndSportIterm> positionAndSportAbnormal = report.getPositionAndSportAbnormal();
                    List<PositionAndSportIterm> collect = positionAndSportAbnormal.stream().sorted(Comparator.comparing(PositionAndSportIterm::getQuestionSn)).collect(Collectors.toList());
                    report.setPositionAndSportAbnormal(collect);
                    LeiBoAllBodyTestResult leiBoAllBodyTestResult = new LeiBoAllBodyTestResult();
                    leiBoAllBodyTestResult.setCerebralPalsyResult(report);
                    vo.setResult(leiBoAllBodyTestResult);
                } else {
                    vo.setResult(JSON.parseObject(result, CerebralPalsyScaleEvaluateResult.class));
                }
            } else {
                if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()){
                    LeiBoAllBodyTestResult leiBoAllBodyTestResult = new LeiBoAllBodyTestResult();
                    LeiBoCerebralPalsySelfTestResult cerebralPalsyScaleEvaluateResult = new LeiBoCerebralPalsySelfTestResult();
                    cerebralPalsyScaleEvaluateResult.setPositionAndSportAbnormal(getDefaultPositionAndSportIterms());
                    cerebralPalsyScaleEvaluateResult.setHint("以上建议是基于此次婴幼儿神经运动发育风险家庭自测影像和GMs影像，经过计算机自动测评，专业人员审核视频得出。由于家庭自测影像和GMs影像的拍摄角度、清晰度、光线、拍摄质量会存在很大差异，以及拍摄人员的专业程度和文化背景不同，计算机自动识别判断会有学习遗漏和判断差异，人工审核视频也不能同面诊相比。因此，此报告只对此次影像和家长填写的问卷负责。请及时预约线下服务，通过专业机构明确诊断。");
                    cerebralPalsyScaleEvaluateResult.setVideoStatus(2);
                    leiBoAllBodyTestResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);
                    record.setResult(JSON.toJSONString(leiBoAllBodyTestResult));
                    scaleEvaluationRecordDao.updateById(record);

                    leiBoAllBodyTestResult = (LeiBoAllBodyTestResult) getReport(record, children);
                    List<PositionAndSportIterm> positionAndSportAbnormal = leiBoAllBodyTestResult.getCerebralPalsyResult().getPositionAndSportAbnormal();
                    List<PositionAndSportIterm> collect = positionAndSportAbnormal.stream().sorted(Comparator.comparing(PositionAndSportIterm::getQuestionSn)).collect(Collectors.toList());
                    leiBoAllBodyTestResult.getCerebralPalsyResult().setPositionAndSportAbnormal(collect);
                    vo.setResult(leiBoAllBodyTestResult);
                }
            }

            vo.setChildRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfChild()).split(",")));
            vo.setMotherRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfMother()).split(",")));
            vo.setExtraRisks(children.getExtraRisks());
            vo.setChildrenId(children.getId());
            vo.setCreated(DateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(scaleTable.getCode());
            vo.setScaleTableName(scaleTable.getName());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            String correctAge = "";
            long week = 0;
            long day = 0;
            long days = DateUtil.getDaysBetweenTime(record.getCreated(), children.getBirthday());
            if (children.getGestationalWeeks() >= 37) {
                week = days / 7;
                day = days % 7;
                correctAge = week + "周" + day + "天";
            } else {
                week = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) / 7;
                day = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) % 7;
                correctAge = week + "周" + day + "天";
            }
            vo.setCorrectAge(correctAge);
            addAnswers(vo, scaleTable, record);
            return vo;
        } else {
            GMsScaleRecordVo vo = new GMsScaleRecordVo();
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            vo.setBirthdayWeight(children.getBirthWeight());
            vo.setBirthWeight(children.getBirthWeight());
            vo.setUserScore(record.getUserScore());
            vo.setAge(OldDateUtil.getAge(children.getBirthday()));
            vo.setBirthday(DateUtil.getYMD(children.getBirthday()));
            vo.setDoctorScore(record.getDoctorScore());
            vo.setGender(Gender.getGender(children.getSex()).getDesc());
            vo.setName(children.getName());
            vo.setChildrenId(children.getId());
            vo.setCreated(DateUtil.getYMDHMSDate(record.getCreated()));
            vo.setId(record.getId());
            vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
            vo.setProgressStatusCode(record.getProgressStatus());
            vo.setScaleTableCode(scaleTable.getCode());
            vo.setScaleTableName(scaleTable.getName());
            vo.setUserId(record.getUserId());
            vo.setConclusion(record.getConclusion());
            vo.setMedicalNumber(children.getMedicalCardNumber());
            String answerWithRemark = record.getAnswerWithRemark();
            List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
            if (CollectionUtils.isNotEmpty(answers)) {
                AttachmentDto attachment = new AttachmentDto();
                int questionId = 1;
                if (fileId == 0) {
                    AnswerWithRemarkDto answerWithRemarkDto = answers.get(0);
                    List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
                    if (CollectionUtils.isNotEmpty(attachmentDtos)) {
                        attachment = attachmentDtos.get(0);
                        questionId = answerWithRemarkDto.getQuestionSn();
                    }
                } else {
                    for (AnswerWithRemarkDto answer : answers) {
                        List<AttachmentDto> attachmentDtos = answer.getAttachmentDtos();
                        if (CollectionUtils.isNotEmpty(attachmentDtos)) {
                            for (AttachmentDto attachmentDto : attachmentDtos) {
                                if (attachmentDto.getFileId().equals(fileId)) {
                                    attachment = attachmentDto;
                                    questionId = answer.getQuestionSn();
                                }
                            }
                        }
                    }
                }
                Long videoId = attachment.getFileId();
                if (videoId != null && videoId > 0) {
                    Files file = fileService.getFile(videoId);
                    if (file.getFileType() == OldFileType.VIDEO.getCode()) {
                        vo.setCoverUrl(attachment.getCoverFileId() == null || attachment.getCoverFileId() == 0 ? "" : fileService.getFileUrl(attachment.getCoverFileId(), false));
                        vo.setVideoUrl(fileService.getFileUrl(attachment.getFileId(), false));
                        vo.setVideoId(videoId);
                        vo.setQuestionSn(questionId);
                    }
                }
            }

            vo.setBirthWeight(children.getBirthWeight());
            vo.setChildRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfChild()).split(",")));
            vo.setMotherRisks(Arrays.asList(risksService.RisksExChanged(children.getDangerOfMother()).split(",")));
            vo.setExtraRisks(children.getExtraRisks());
            String correctAge = "";
            long week = 0;
            long day = 0;
            long days = DateUtil.getDaysBetweenTime(record.getCreated(), children.getDateOfBirth());
            if (children.getGestationalWeeks() >= 37) {
                week = days / 7;
                day = days % 7;
                correctAge = week + "周" + day + "天";
            } else {
                week = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) / 7;
                day = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) % 7;
                correctAge = week + "周" + day + "天";
            }
            vo.setCorrectAge(correctAge);
            vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
            if (StringUtils.isNotBlank(record.getResult())) {
                if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
                    vo.setResult(JSON.parseObject(record.getResult(), GMsAndCerebralPalsyResult.class).getGmsResult());
                } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()){
                    LeiBoAllBodyTestResult leiBoAllBodyTestResult = (LeiBoAllBodyTestResult) getReport(record, children);
                    vo.setResult(leiBoAllBodyTestResult.getGmsResult());
                } else {
                    vo.setResult(JSON.parseObject(record.getResult(), GMsScaleEvaluationResult.class));
                }
            }
            return vo;
        }
    }

    private void addAnswers(ScaleRecordVo vo, ScaleTable scaleTable, ScaleEvaluationRecord record) {
        List<QuestionDto> questions = scaleTable.getQuestions();
        String answerWithRemark = record.getAnswerWithRemark();
        List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);

        if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
            answers = answers.subList(1, answers.size());
        }
        if (CollectionUtils.isEmpty(answers)) {
            return;
        }

        ArrayList<ScaleRecordQuestionVo> scaleRecordQuestionVos = new ArrayList<>(answers.size());
        for (AnswerWithRemarkDto answer : answers) {
            ScaleRecordQuestionVo scaleRecordQuestionVo = new ScaleRecordQuestionVo();
            QuestionDto questionDto = getQuestionDto(questions, answer.getQuestionSn());

            int score = 0;
            List<String> content = new ArrayList<>();
            String optionSn = answer.getOptionSn();
            String[] split = optionSn.split(",");
            for (String s : split) {
                if (StringUtils.isNotBlank(s)) {
                    Option option = getOption(questionDto.getOptions(), Integer.parseInt(s));
                    if (option != null) {
                        content.add(option.getContent());
                        score += option.getScore();
                    }
                }
            }

            scaleRecordQuestionVo.setAnswer(content);
            scaleRecordQuestionVo.setUserScore(score);
            scaleRecordQuestionVo.setDoctorScore(answer.getDoctorScore() == null ? score : answer.getDoctorScore());
            scaleRecordQuestionVo.setId(questionDto.getSn());
            scaleRecordQuestionVo.setName(questionDto.getName());
            scaleRecordQuestionVo.setRemark(answer.getRemark());
            scaleRecordQuestionVo.setType(QuestionSubjectEnum.getQuestionSubject(questionDto.getSubject()).getDesc());


            //设置附件
            List<AttachmentDto> attachmentDtos = answer.getAttachmentDtos();
            if (CollectionUtils.isNotEmpty(attachmentDtos)) {
                ArrayList<AttachmentVo> attachmentVos = new ArrayList<>(attachmentDtos.size());
//                List<Files> fileList = attachmentFileIds.stream().map(fileId -> fileService.getFile(fileId)).collect(Collectors.toList());
//                for (Files files : fileList) {
//                    AttachmentVo attachmentVo = new AttachmentVo();
//                    Byte fileType = files.getFileType();
//                    attachmentVo.setType(fileType);
//                    boolean origin = false;
//                    if (fileType == FileType.AUDIO.getCode()) {
//                        origin = true;
//                    }
//                    attachmentVo.setUrl(fileService.getFileUrl(files.getId(), origin));
//                    attachmentVos.add(attachmentVo);
//                }
                for (AttachmentDto attachmentDto : attachmentDtos) {
                    AttachmentVo attachmentVo = new AttachmentVo();
                    attachmentVo.setFileId(attachmentDto.getFileId());
                    Files file = fileService.getFile(attachmentDto.getFileId());
                    Byte fileType = file.getFileType();
                    if (attachmentDto.getCoverFileId() != 0) {
                        String coverUrl = fileService.getFileUrl(attachmentDto.getCoverFileId(), false);
                        attachmentVo.setCoverUrl(coverUrl);
                    } else {
                        attachmentVo.setCoverUrl(null);
                    }
                    attachmentVo.setType(fileType);
                    boolean origin = false;
                    if (fileType == OldFileType.AUDIO.getCode()) {
                        origin = true;
                    }
                    attachmentVo.setUrl(fileService.getFileUrl(file.getId(), origin));
                    attachmentVo.setPosition(StringUtils.isBlank(attachmentDto.getPosition()) ? "" : attachmentDto.getPosition());
                    attachmentVo.setVideoType(StringUtils.isBlank(attachmentDto.getVideoType()) ? "" : attachmentDto.getVideoType());
                    attachmentVos.add(attachmentVo);
                }
                scaleRecordQuestionVo.setAttachments(attachmentVos);
            } else {
                scaleRecordQuestionVo.setAttachments(new ArrayList<>());
            }

            scaleRecordQuestionVos.add(scaleRecordQuestionVo);
        }
        List<ScaleRecordQuestionVo> collect = scaleRecordQuestionVos.stream().sorted(Comparator.comparing(ScaleRecordQuestionVo::getId)).collect(Collectors.toList());
        vo.setAnswers(collect);
    }

//    @Override
//    public void updateScaleAnswerScore(LoginVo user, UpdateScaleRecordAnswerScoreRequest request) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(request.getId());
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//
//        String answerWithRemark = record.getAnswerWithRemark();
//        List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//
//        AnswerWithRemarkDto answer = getAnswerWithRemarkDto(answers, request.getAnswerId());
//        if (answer == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        Integer oldScore = answer.getDoctorScore();
//
//        AnswerWithRemarkDto newAnswer = new AnswerWithRemarkDto();
//        newAnswer.setAttachmentDtos(answer.getAttachmentDtos());
//        newAnswer.setRemark(answer.getRemark());
//        newAnswer.setQuestionSn(answer.getQuestionSn());
//        newAnswer.setOptionSn(answer.getOptionSn());
//        newAnswer.setDoctorScore(request.getScore());
//
//        answers.set(answers.indexOf(answer), newAnswer);
//
//        record.setAnswerWithRemark(JSON.toJSONString(answers));
//        if (record.getDoctorId() == 0) {
//            record.setDoctorId(user.getId());
//        }
//        if (oldScore == null || oldScore == 0) {
//            record.setDoctorScore(record.getDoctorScore() + request.getScore());
//        } else {
//            record.setDoctorScore(record.getDoctorScore() + (request.getScore() - oldScore));
//        }
//
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
//        List<QuestionDto> questions = scaleTable.getQuestions();
//
//        QuestionDto questionDto = getQuestionDto(questions, answer.getQuestionSn());
//        QuestionSubjectEnum questionSubject = QuestionSubjectEnum.getQuestionSubject(questionDto.getSubject());
//        String abnormalItem = questionSubject.getDesc() + "-" + questionDto.getName();
//
//        List<String> abnormalItems = new ArrayList<>();
//        String result = record.getResult();
//
//        Children children = childrenService.getChildrenById(record.getChildrenId());
//        List<String> childrenRisks = Arrays.asList(children.getChildRisks().split(","));
//
//        if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
//            CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult;
//            if (StringUtils.isNotBlank(result)) {
//                cerebralPalsyScaleEvaluateResult = JSON.parseObject(result, CerebralPalsyScaleEvaluateResult.class);
//                abnormalItems = cerebralPalsyScaleEvaluateResult.getAbnormalIterm();
//            } else {
//                cerebralPalsyScaleEvaluateResult = new CerebralPalsyScaleEvaluateResult();
//            }
//            //分数从 0 到到大于 0 时，增加异常项，反之去掉异常项
//            if (request.getScore() == 0) {
//                if (abnormalItems.contains(abnormalItem)) {
//                    abnormalItems.remove(abnormalItem);
//                }
//            } else if (request.getScore() > 0){
//                if (!abnormalItems.contains(abnormalItem)) {
//                    abnormalItems.add(abnormalItem);
//                }
//            }
//            cerebralPalsyScaleEvaluateResult.setAbnormalIterm(abnormalItems);
//            cerebralPalsyScaleEvaluateResult.setResult(calScaleRecordResult(childrenRisks, abnormalItems));
//            result = JSON.toJSONString(cerebralPalsyScaleEvaluateResult);
//
//        } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
//            GMsAndCerebralPalsyResult gMsAndCerebralPalsyResult;
//            if (StringUtils.isNotBlank(result)) {
//                gMsAndCerebralPalsyResult = JSON.parseObject(result, GMsAndCerebralPalsyResult.class);
//            } else {
//                gMsAndCerebralPalsyResult = new GMsAndCerebralPalsyResult();
//            }
//            CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = gMsAndCerebralPalsyResult.getCerebralPalsyResult();
//            //分数从 0 到到大于 0 时，增加异常项，反之去掉异常项
//            if (oldScore != null) {
//                if (oldScore == 0 && request.getScore() > 0) {
//                    abnormalItems.add(abnormalItem);
//                } else if (oldScore > 0 && request.getScore() < 0) {
//                    if (abnormalItems.contains(abnormalItem)) {
//                        abnormalItems.remove(abnormalItem);
//                    }
//                }
//            }
//            cerebralPalsyScaleEvaluateResult.setAbnormalIterm(abnormalItems);
//            cerebralPalsyScaleEvaluateResult.setResult(calScaleRecordResult(childrenRisks, abnormalItems));
//
//            gMsAndCerebralPalsyResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);
//
//            result = JSON.toJSONString(gMsAndCerebralPalsyResult);
//        }
//        record.setResult(result);
//        scaleEvaluationRecordDao.updateById(record);
//    }
//
    private String calScaleRecordResult(List<String> childrenRisks, List<String> abnormalItems) {
        if (CollectionUtils.isNotEmpty(childrenRisks) && CollectionUtils.isNotEmpty(abnormalItems)) {
            return "4";
        } else if (CollectionUtils.isNotEmpty(childrenRisks) && CollectionUtils.isEmpty(abnormalItems)) {
            return "2";
        } else if (CollectionUtils.isEmpty(childrenRisks) && CollectionUtils.isNotEmpty(abnormalItems)) {
            return "3";
        } else {
            return "1";
        }
    }
//
//    @Override
//    public void updateScaleRemark(LoginVo user, String request) {
//
//        log.info("update remark request : {}", request);
//        String remark = "";
//        boolean evaluated = false;
//        boolean previewReport = true;
//        UpdateScaleRecordRemarkRequest baseRequest = JSON.parseObject(request, UpdateScaleRecordRemarkRequest.class);
////        baseRequest.setDraft(false);
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(baseRequest.getId());
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ScaleStatus currentStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (currentStatus != ScaleStatus.NOT_EVALUATE && currentStatus != ScaleStatus.SCHEDULED && currentStatus != ScaleStatus.REVIEW_NOT_PASS) {
//            throw new ServiceException(ResultCode.STATUS_ERROR);
//        }
//        if (record.getDoctorId() == 0) {
//            record.setDoctorId(user.getId());
//        }
//        String result = "";
//        if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
//            CerebralPalsyRemarkRequest cerebralPalsyRemarkRequest = JSON.parseObject(request, CerebralPalsyRemarkRequest.class);
//            CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = new CerebralPalsyScaleEvaluateResult();
//            cerebralPalsyScaleEvaluateResult.setAbnormalIterm(cerebralPalsyRemarkRequest.getAbnormalIterm());
//            cerebralPalsyScaleEvaluateResult.setHighRisk(cerebralPalsyRemarkRequest.getHighRisk());
//            cerebralPalsyScaleEvaluateResult.setRemark("");
//            cerebralPalsyScaleEvaluateResult.setResult(cerebralPalsyRemarkRequest.getResult());
//            result = JSON.toJSONString(cerebralPalsyScaleEvaluateResult);
//            remark = cerebralPalsyRemarkRequest.getRemark();
//            record.setConclusion(cerebralPalsyRemarkRequest.getRemark());
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS.getCode()) {
//            GMsRemarkRequest gMsRemarkRequest = JSON.parseObject(request, GMsRemarkRequest.class);
//            GMsScaleEvaluationResult gMsScaleEvaluationResult = new GMsScaleEvaluationResult();
//
//
//            GMs8Result gms8Result = gMsRemarkRequest.getResult();
//            gMsScaleEvaluationResult.setStageResult(gms8Result.getStageResult());
//            gMsScaleEvaluationResult.setStage(gms8Result.getStage());
//            gMsScaleEvaluationResult.setSuggest(gms8Result.getSuggest());
//            gMsScaleEvaluationResult.setNextReserve(gms8Result.getNextReserve());
//            gMsScaleEvaluationResult.setShowVideo(gms8Result.getShowVideo());
//
//            result = JSON.toJSONString(gMsScaleEvaluationResult);
//            remark = gMsRemarkRequest.getRemark();
//            record.setConclusion("");
//            evaluated = true;
//
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
//            GMsAndCerebralPalsyResult gMsAndCerebralPalsyResult;
//            String existResult = record.getResult();
//            if (StringUtils.isNotBlank(existResult)) {
//                gMsAndCerebralPalsyResult = JSON.parseObject(existResult, GMsAndCerebralPalsyResult.class);
//            } else {
//                gMsAndCerebralPalsyResult = new GMsAndCerebralPalsyResult();
//            }
//
//            JSONObject jsonObject = JSON.parseObject(request);
//
//            if (jsonObject.containsKey("result")) {
//
//                CerebralPalsyRemarkRequest cerebralPalsyRemarkRequest = JSON.parseObject(request, CerebralPalsyRemarkRequest.class);
//                CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = new CerebralPalsyScaleEvaluateResult();
//                cerebralPalsyScaleEvaluateResult.setAbnormalIterm(cerebralPalsyRemarkRequest.getAbnormalIterm());
//                cerebralPalsyScaleEvaluateResult.setHighRisk(cerebralPalsyRemarkRequest.getHighRisk());
//                cerebralPalsyScaleEvaluateResult.setRemark("");
//                cerebralPalsyScaleEvaluateResult.setResult(cerebralPalsyRemarkRequest.getResult());
//
//                gMsAndCerebralPalsyResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);
//                record.setConclusion(cerebralPalsyRemarkRequest.getRemark());
//                remark = cerebralPalsyRemarkRequest.getRemark();
//
//                if (!baseRequest.isDraft()) {
//                    if (ScaleStatus.getStatus(record.getProgressStatus()) == ScaleStatus.GMS_EVALUATED) {
//                        record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//                    } else {
//                        record.setProgressStatus(ScaleStatus.BERE_EVALUATED.getStatus());
//                    }
//                }
//            } else if (jsonObject.containsKey("stage") || jsonObject.containsKey("stageResult")) {
//
//                GMsRemarkRequest gMsRemarkRequest = JSON.parseObject(request, GMsRemarkRequest.class);
//                GMsScaleEvaluationResult gMsScaleEvaluationResult = new GMsScaleEvaluationResult();
//
//                GMs8Result gms8Result = gMsRemarkRequest.getResult();
//                gMsScaleEvaluationResult.setStageResult(gms8Result.getStageResult());
//                gMsScaleEvaluationResult.setStage(gms8Result.getStage());
//                gMsScaleEvaluationResult.setSuggest(gms8Result.getSuggest());
//                gMsScaleEvaluationResult.setNextReserve(gms8Result.getNextReserve());
//                gMsScaleEvaluationResult.setShowVideo(gms8Result.getShowVideo());
//                remark = gMsRemarkRequest.getRemark();
//                gMsScaleEvaluationResult.setRemark(remark);
//                gMsAndCerebralPalsyResult.setGmsResult(gMsScaleEvaluationResult);
//
//                if (!baseRequest.isDraft()) {
//                    if (ScaleStatus.getStatus(record.getProgressStatus()) == ScaleStatus.BERE_EVALUATED) {
//                        record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//                    } else {
//                        record.setProgressStatus(ScaleStatus.GMS_EVALUATED.getStatus());
//                    }
//                }
//            }
//            if (gMsAndCerebralPalsyResult.getGmsResult() != null && gMsAndCerebralPalsyResult.getCerebralPalsyResult() != null
//                    && StringUtils.isNotBlank(gMsAndCerebralPalsyResult.getCerebralPalsyResult().getResult())) {
//                evaluated = true;
//            }
//            result = JSON.toJSONString(gMsAndCerebralPalsyResult);
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
//
//            LeiBoAllBodyTestResult leiBoAllBodyTestResult;
//            LeiBoCerebralPalsySelfTestResult cerebralPalsyResult;
//            GMsScaleEvaluationResult gmsResult;
//            String existResult = record.getResult();
//            if (StringUtils.isNotBlank(existResult)) {
//                leiBoAllBodyTestResult  = JSON.parseObject(existResult, LeiBoAllBodyTestResult.class);
//
//                gmsResult = leiBoAllBodyTestResult.getGmsResult();
//                if (gmsResult == null) {
//                    gmsResult = new GMsScaleEvaluationResult();
//                }
//                cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
//                if (cerebralPalsyResult == null) {
//                    cerebralPalsyResult = new LeiBoCerebralPalsySelfTestResult();
//                }
//            } else {
//                leiBoAllBodyTestResult = new LeiBoAllBodyTestResult();
//                gmsResult = new GMsScaleEvaluationResult();
//                cerebralPalsyResult = new LeiBoCerebralPalsySelfTestResult();
//            }
//
//            //判断是评测脑瘫还是 gms
//            JSONObject jsonObject = JSON.parseObject(request);
//            if (jsonObject.containsKey("result")) {
//
//                //处理gms
//                GMsRemarkRequest gMsRemarkRequest = JSON.parseObject(request, GMsRemarkRequest.class);
//                GMs8Result gms8Result = gMsRemarkRequest.getResult();
//                gmsResult.setStageResult(gms8Result.getStageResult());
//                gmsResult.setStage(gms8Result.getStage());
//                gmsResult.setSuggest(gms8Result.getSuggest());
//                gmsResult.setNextReserve(gms8Result.getNextReserve());
//                gmsResult.setShowVideo(gms8Result.getShowVideo());
//                remark = gMsRemarkRequest.getRemark();
//                gmsResult.setRemark(remark);
//
//                leiBoAllBodyTestResult.setGmsResult(gmsResult);
//
//                if (!baseRequest.isDraft()) {
//                    if (ScaleStatus.getStatus(record.getProgressStatus()) == ScaleStatus.BERE_EVALUATED) {
//                        record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//                    } else {
//                        record.setProgressStatus(ScaleStatus.GMS_EVALUATED.getStatus());
//                    }
//                }
//            } else {
////
//                //处理脑瘫
//                LeiBoCerebralPalsySelfTestRequest cerebralRequest = JSON.parseObject(request, LeiBoCerebralPalsySelfTestRequest.class);
//
//                List<PositionAndSportIterm> positionAndSportIterms = answerOption2PositionAndSportIterm(ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode(), cerebralRequest.getPositionAndSportAbnormal());
//                positionAndSportIterms = positionAndSportIterms.stream().sorted(Comparator.comparing(PositionAndSportIterm::getOptionSn).reversed()).collect(Collectors.toList());
////                cerebralPalsyResult.setCerebralPalsyScore(positionAndSportIterms.stream().mapToInt(PositionAndSportIterm::getStatus).sum());
//                cerebralPalsyResult.setPositionAndSportAbnormal(positionAndSportIterms);
//                cerebralPalsyResult.setRemark(cerebralRequest.getRemark());
//                cerebralPalsyResult.setSuggests(cerebralRequest.getSuggests());
//                cerebralPalsyResult.setHint(cerebralRequest.getHint());
//                cerebralPalsyResult.setUseLeiboSuggest(cerebralRequest.getUseLeiboSuggest());
//                cerebralPalsyResult.setObviouslyBehind(cerebralRequest.getObviouslyBehind());
//                cerebralPalsyResult.setTendencyBehind(cerebralRequest.getTendencyBehind());
//                cerebralPalsyResult.setVideoStatus(cerebralRequest.getVideoStatus());
//                if (cerebralPalsyResult.getModifyCount() == null || cerebralPalsyResult.getModifyCount() < 1) {
//                    cerebralPalsyResult.setModifyCount(cerebralRequest.getModified() ? 1 : 0);
//                }
//                remark = cerebralRequest.getRemark();
//                leiBoAllBodyTestResult.setCerebralPalsyResult(cerebralPalsyResult);
//
//                if (!baseRequest.isDraft()) {
//                    if (ScaleStatus.getStatus(record.getProgressStatus()) == ScaleStatus.GMS_EVALUATED) {
//                        record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//                    } else {
//                        record.setProgressStatus(ScaleStatus.BERE_EVALUATED.getStatus());
//                    }
//                }
//            }
//            result = JSON.toJSONString(leiBoAllBodyTestResult);
//            previewReport = false;
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
//            LeiBoCerebralPalsySelfTestResult leiBoCerebralPalsySelfTestResult;
//            LeiBoCerebralPalsySelfTestRequest cerebralRequest = JSON.parseObject(request, LeiBoCerebralPalsySelfTestRequest.class);
//            String existResult = record.getResult();
//            if (StringUtils.isNotBlank(existResult)) {
//                leiBoCerebralPalsySelfTestResult  = JSON.parseObject(existResult, LeiBoCerebralPalsySelfTestResult.class);
//            } else {
//                leiBoCerebralPalsySelfTestResult = new LeiBoCerebralPalsySelfTestResult();
//            }
//            List<PositionAndSportIterm> positionAndSportIterms = answerOption2PositionAndSportIterm(ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode(), cerebralRequest.getPositionAndSportAbnormal());
//            positionAndSportIterms = positionAndSportIterms.stream().sorted(Comparator.comparing(PositionAndSportIterm::getOptionSn).reversed()).collect(Collectors.toList());
//
////                cerebralPalsyResult.setCerebralPalsyScore(positionAndSportIterms.stream().mapToInt(PositionAndSportIterm::getStatus).sum());
//            leiBoCerebralPalsySelfTestResult.setPositionAndSportAbnormal(positionAndSportIterms);
//            leiBoCerebralPalsySelfTestResult.setRemark(cerebralRequest.getRemark());
//            leiBoCerebralPalsySelfTestResult.setSuggests(cerebralRequest.getSuggests());
//            leiBoCerebralPalsySelfTestResult.setHint(cerebralRequest.getHint());
//            leiBoCerebralPalsySelfTestResult.setUseLeiboSuggest(cerebralRequest.getUseLeiboSuggest());
//            remark = cerebralRequest.getRemark();
//            result = JSON.toJSONString(leiBoCerebralPalsySelfTestResult);
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode()) {
//            GriffithsResult griffithsResult = JSON.parseObject(request, GriffithsResult.class);
//            result = JSON.toJSONString(griffithsResult);
////            result = request;
//
//            //用于判断发送模板消息
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.WEISHI_CHILD_INTELLIGENCE.getCode()) {
//            WeishiChildIntelligenceResult weishiChildIntelligenceResult = JSON.parseObject(request, WeishiChildIntelligenceResult.class);
//            result = JSON.toJSONString(weishiChildIntelligenceResult);
//
//            //用于判断发送模板消息
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.WEISHI_TODDLER_INTELLIGENCE.getCode()) {
//            WeishiToddlerIntelligenceResult weishiToddlerIntelligenceResult = JSON.parseObject(request, WeishiToddlerIntelligenceResult.class);
//            result = JSON.toJSONString(weishiToddlerIntelligenceResult);
//
//            //用于判断发送模板消息
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_8.getCode()) {
//            Gms8RemarkRequest gms8RemarkRequest = JSON.parseObject(request, Gms8RemarkRequest.class);
//            result = JSON.toJSONString(gms8RemarkRequest.getResult());
//            //用于判断发送模板消息
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMFM88.getCode()) {
//            String remark1 = baseRequest.getRemark();
//            GMFM88Result gmfm88Result = new GMFM88Result();
//            gmfm88Result.setResult(remark1);
//            result = JSON.toJSONString(gmfm88Result);
//            //用于判断发送模板消息
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.CAREGIVER_BURDEN.getCode()) {
//            CaregiverBurdenRemarkRequest caregiverBurdenRemarkRequest = JSON.parseObject(request, CaregiverBurdenRemarkRequest.class);
//            CaregiverBurdenResult caregiverBurdenResult = caregiverBurdenRemarkRequest.getCaregiverBurdenResult();
//
//            result = JSON.toJSONString(caregiverBurdenResult);
//            //用于判断发送模板消息
//            evaluated = true;
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//        } else {
//            List<ScaleTableConstant.ScaleTableCode> scaleTables = ScaleTableConstant.ScaleTableCode.getScaleTableByClassification(ScaleTableConstant.ScaleTableClassification.DDST);
//            Set<Byte> scaleTableCodesSet = scaleTables.stream().map(ScaleTableConstant.ScaleTableCode::getCode).collect(Collectors.toSet());
//            if (scaleTableCodesSet.contains(record.getScaleTableCode())) {
//                DDSTResultRequest ddstResultRequest = JSON.parseObject(request, DDSTResultRequest.class);
//                result = JSON.toJSONString(ddstResultRequest.getResult());
//            }
//            if (!baseRequest.isDraft()) {
//                record.setProgressStatus(ScaleStatus.EVALUATED.getStatus());
//            }
//            previewReport = false;
//        }
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        record.setResult(result);
//        record.setDoctorId(AuthContext.getUserId());
//        scaleEvaluationRecordDao.updateById(record);
//
//        ScaleTable scaleTable = ScaleTableResolver.getScaleTableMap().get(record.getScaleTableCode());
//
//        if (!baseRequest.isDraft()) {
////            if (evaluated) {
////                try {
////                    //TODO 发送微信小程序消息
////                    sendWxTemplateMsg(record, scaleTable, remark);
////                } catch (Exception e) {
////
////                }
////                scaleEvaluationRecordDao.updateById(record);
////            } else {
////                throw new ServiceException(ResultCode.GMS_NOT_EVALUATED);
////            }
//            if (record.getProgressStatus() == ScaleStatus.EVALUATED.getStatus()) {
//                try {
//                    //TODO 发送微信小程序消息
//                    sendWxTemplateMsg(record, scaleTable, remark);
//                } catch (Exception e) {
//                    log.error("发送微信订阅消息失败:{}", e);
//                }
//            }
//            scaleEvaluationRecordDao.updateById(record);
//        }
//
//        if (previewReport) {
//            previewReport(record.getId());
//        }
//    }
//
//    @Override
//    public String previewReport(Long id) {
//        ScaleEvaluationRecord scaleEvaluationRecord = getScaleEvaluationRecord(id);
//        if (scaleEvaluationRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        if (ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.GMFM88.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.CAREGIVER_BURDEN.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.GMS_8.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.GMS.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.WEISHI_TODDLER_INTELLIGENCE.getCode() != scaleEvaluationRecord.getScaleTableCode() &&
//                ScaleTableConstant.ScaleTableCode.WEISHI_CHILD_INTELLIGENCE.getCode() != scaleEvaluationRecord.getScaleTableCode()) {
//            throw new ServiceException(ResultCode.UNSUPPORTED_SCALE_CODE);
//        }
//
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(scaleEvaluationRecord.getScaleTableCode());
//        if (scaleTable == null) {
//            return null;
//        }
//
//        Children children = childrenService.getChildrenById(scaleEvaluationRecord.getChildrenId());
//        BaseScaleEvaluationResult report = getReport(scaleEvaluationRecord, children);
//
//        ScaleRecordReportVo vo = new ScaleRecordReportVo();
//        vo.setUserId(scaleEvaluationRecord.getUserId());
//        vo.setAge(DateUtil.getAge(children.getBirthday()));
//        vo.setBirthday(DateUtil.getYMD(children.getBirthday()));
//        vo.setName(children.getName());
//        vo.setGender(Gender.getGender(children.getGender()).getDesc());
//        vo.setBirthdayWeight(children.getBirthWeight());
//        vo.setGestationalWeek(children.getGestationalWeek() + "周" + children.getGestationalWeekDay() + "天");
//        vo.setCreated(DateUtil.getYMDHMSDate(scaleEvaluationRecord.getCreated()));
//        vo.setDoctorScore(scaleEvaluationRecord.getDoctorScore());
//        vo.setId(scaleEvaluationRecord.getId());
//        vo.setProgressStatus(ScaleStatus.getStatus(scaleEvaluationRecord.getProgressStatus()).getDesc());
//        vo.setProgressStatusCode(scaleEvaluationRecord.getProgressStatus());
//        vo.setScaleTableName(scaleTable.getName());
//        vo.setScaleTableCode(scaleTable.getCode());
//        vo.setUserScore(scaleEvaluationRecord.getUserScore());
//        vo.setConclusion(scaleEvaluationRecord.getConclusion());
//        vo.setEvaluateDate(DateUtil.getYMDHMDate(scaleEvaluationRecord.getUpdated()));
//        vo.setMonthAge((int)(scaleEvaluationRecord.getCreated() - children.getBirthday()) / 60 / 60 / 24 / 30);
//        vo.setScaleResult(report);
//        vo.setAbnormalVideos(report.getAbnormalVideoNames());
//        vo.setBigMoveVideos(report.getBigMoveVideoNames());
//
//        AnswerWithRemarkDto answerWithRemarkDto = null;
//        String answerWithRemark = scaleEvaluationRecord.getAnswerWithRemark();
//        List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//        if (CollectionUtils.isNotEmpty(answerWithRemarkDtos) && answerWithRemarkDtos.size() >= 3) {
//            answerWithRemarkDto = answerWithRemarkDtos.get(2);
//        }
//        vo.setBigMovement(getBigMovement(answerWithRemarkDto, scaleTable.getQuestions().size() >= 3 ? scaleTable.getQuestions().get(2) : null));
//
//        DoctorVo doctorVo = doctorService.getDoctorVo(scaleEvaluationRecord.getDoctorId());
//        if (doctorVo != null) {
//            vo.setDoctorName(doctorVo.getName());
//        }
//
//        try {
//            File tempFile = fileService.getTempFile();
//            List<ScaleEvaluateLogListVo> scaleEvaluateLogListVos = scaleEvaluateLogService.listScaleEvaluateLogByScaleRecordId(scaleEvaluationRecord.getId());
//
//            EvaluateLogVo evaluateLogVo = new EvaluateLogVo();
//            evaluateLogVo.setRecordId(scaleEvaluationRecord.getId());
//            for (ScaleEvaluateLogListVo scaleEvaluateLogListVo : scaleEvaluateLogListVos) {
//                if (scaleEvaluateLogListVo.getStatusByte() == ScaleStatus.EVALUATED.getStatus()) {
//                    evaluateLogVo.setSendTime(scaleEvaluateLogListVo.getCreated());
//                } else if (scaleEvaluateLogListVo.getStatusByte() == ScaleStatus.REVIEWED_WAIT_SEND.getStatus()) {
//                    evaluateLogVo.setReviewUserName(scaleEvaluateLogListVo.getUserName());
//                } else if (scaleEvaluateLogListVo.getStatusByte() == ScaleStatus.EVALUATED_WAIT_REVIEW.getStatus()) {
//                    evaluateLogVo.setEvaluateUserName(scaleEvaluateLogListVo.getUserName());
//                }
//            }
//
//            OrganizationVo organizationVo = organizationService.getOrganizationVo(AuthContext.getUser().getOrganizationId());
//            ReportType reportType = ReportType.getReportType(organizationVo.getReportType());
//            reportInfo.generateReport(vo, tempFile, scaleTable, answerWithRemarkDtos, evaluateLogVo, reportType);
//
////            File addWaterMarkFile = fileService.getTempFile();
//
//            //增加当前用户的机构名作为 PDF 的水印
////            String organizationName = AuthContext.getUser().getOrganizationName();
////            pdfAddWaterMark(tempFile, addWaterMarkFile, organizationName);
//
//            String fileName = vo.getScaleTableName() + "-" + vo.getId() + DateUtil.getNowYMDHMS() + ".pdf";
//            String filePath = fileService.saveFile(FileType.PDF, tempFile, fileName);
//
//            Files files = new Files();
//            files.setStatus(BaseStatus.NORMAL.getStatus());
//            files.setRawName(fileName);
//            files.setOriginalName(fileName);
//            files.setFileType(FileType.PDF.getCode());
//            files.setCreated(DateUtil.getCurrentTimeStamp());
//            files.setFileSize(tempFile.getTotalSpace());
//            files.setFilePath(filePath);
//            files.setExtension(FilenameUtils.getExtension(fileName));
//            files.setUpdated(DateUtil.getCurrentTimeStamp());
//            fileService.insertFiles(files);
//
//            scaleEvaluationRecord.setResultFileId(files.getId());
//            //新生成 pdf 时清空之前的 pdf 图片
//            scaleEvaluationRecord.setResultPics("");
//            scaleEvaluationRecordDao.updateById(scaleEvaluationRecord);
//
//            return fileService.getFileUrl(scaleEvaluationRecord.getResultFileId(), true);
//        } catch (Exception e) {
//            log.error("生成 PDF 报告失败，{}", e);
//        }
//        return "";
//    }
//
//    @Override
//    public List<String> getReportPic(Long id) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//
//        //如果已存在，直接返回
//        if (StringUtils.isNotBlank(record.getResultPics())) {
//            String[] strings = record.getResultPics().split(",");
//            List<String> collect = Arrays.stream(strings).map(str -> {
//                long aLong = Long.parseLong(str);
//                return fileService.getFileUrl(aLong, true);
//            }).collect(Collectors.toList());
//
//            return collect;
//        }
//
//        Long resultFileId = record.getResultFileId();
//        if (resultFileId != null && resultFileId > 0) {
//            File file = fileService.downloadFile(resultFileId);
//            try {
//                List<File> files = PdfUtil.pdfToImg(file);
//                ArrayList<Long> imgFileIds = new ArrayList<>();
//                for (File imgFile : files) {
//                    String imgFileKey = fileService.saveFile(FileType.PICTURE, imgFile, imgFile.getName());
//                    Files img = new Files();
//                    img.setStatus(BaseStatus.NORMAL.getStatus());
//                    img.setRawName(imgFile.getName());
//                    img.setOriginalName(imgFile.getName());
//                    img.setFileType(FileType.PICTURE.getCode());
//                    img.setCreated(DateUtil.getCurrentTimeStamp());
//                    img.setFileSize(imgFile.getTotalSpace());
//                    img.setFilePath(imgFileKey);
//                    img.setExtension(FilenameUtils.getExtension(imgFile.getName()));
//                    img.setUpdated(DateUtil.getCurrentTimeStamp());
//                    fileService.insertFiles(img);
//                    imgFileIds.add(img.getId());
//                }
//
//                //保存图片 id
//                record.setResultPics(StringUtils.join(imgFileIds.toArray(), ","));
//                scaleEvaluationRecordDao.updateById(record);
//                List<String> collect = imgFileIds.stream().map(imgId -> fileService.getFileUrl(imgId, true)).collect(Collectors.toList());
//                return collect;
//            } catch (IOException e) {
//                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//            }
//        }
//        return new ArrayList<>();
//    }
//
//    @Override
//    public void saveScaleRecord(ScaleEvaluationRecord record) {
//        scaleEvaluationRecordDao.insert(record);
//    }
//
//    @Override
//    public ListClinicEvaluateResponse listClinicEvaluate(int pageNo, int pageSize, LoginVo loginVo, String keyWord, List<Byte> scaleCodes) {
//
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("organization_id", AuthContext.getUser().getOrganizationId());
//        wrapper.eq("reserve_type", WorkScheduleType.CLINIC_EVALUATE.getType());
//        if (StringUtils.isNotBlank(keyWord)) {
//            List<Long> userIds = userService.queryUserIds(loginVo.getId(), loginVo.getOrganizationId(), keyWord);
//            wrapper.in("user_id", userIds);
//        }
//        if (CollectionUtils.isNotEmpty(scaleCodes)) {
//            wrapper.in("scale_table_code", scaleCodes);
//        }
//        wrapper.notIn("progress_status", Arrays.asList(new Byte[]{ScaleStatus.WAIT_SCHEDULE.getStatus(), ScaleStatus.REFUNDED.getStatus()}));
//        wrapper.orderByDesc("created");
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//        List<ClinicEvaluateListVo> collect = records.stream().map(record -> convert2ClinicEvaluateListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListClinicEvaluateResponse(pageVo, collect);
//    }
//
//    @Override
//    public ListScaleOrderResponse listReviewScaleOrder(int pageNo, int pageSize) {
//        Page<ScaleOrder> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ScaleOrder> wrapper = new QueryWrapper<>();
//        wrapper.eq("order_status", ScaleOrderStatus.WAIT_REVIEW.getCode());
//        wrapper.eq("order_type", OrderType.SCALE_TABLE_ORDER.getCode());
//        wrapper.eq("payment", PaymentType.OFF_LINE.getCode());
//        wrapper.eq("organization_id", AuthContext.getUser().getOrganizationId());
//        wrapper.orderByAsc("created");
//        page = scaleOrderDao.selectPage(page, wrapper);
//        List<ScaleOrder> records = page.getRecords();
//        List<ScaleOrderListVo> collect = records.stream().map(scaleOrder -> convert2ScaleOrderListVo(scaleOrder)).collect(Collectors.toList());
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListScaleOrderResponse(pageVo, collect);
//    }
//
//    @Override
//    public void reviewScaleOrder(ReviewScaleOrderRequest request) {
//        ScaleOrder scaleOrder = scaleOrderDao.selectById(request.getId());
//        if (scaleOrder == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        if (ScaleOrderStatus.WAIT_REVIEW == ScaleOrderStatus.getScaleOrderStatus(scaleOrder.getOrderStatus())) {
//            ScaleOrderStatus requestStatus = ScaleOrderStatus.getScaleOrderStatus(request.getStatus());
//            if (requestStatus == ScaleOrderStatus.PAID) {
//                scaleOrder.setOrderStatus(ScaleOrderStatus.PAID.getCode());
//                scaleOrder.setReason("审核通过");
//                scaleOrder.setAvailableTimes(request.getAvailableTimes());
//            } else if (requestStatus == ScaleOrderStatus.REJECT) {
//                scaleOrder.setOrderStatus(ScaleOrderStatus.REJECT.getCode());
//                scaleOrder.setReason(request.getReason());
//            }
//            scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
//            scaleOrderDao.updateById(scaleOrder);
//        }
//    }
//
//    /**
//     * 取消门诊量表评估
//     * @param id
//     */
//    @Override
//    public void cancelClinicScaleTableEvaluation(long id) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(id);
//        if (scaleEvaluationRecord == null) {
//            return;
//        }
//        ScaleStatus status = ScaleStatus.getStatus(scaleEvaluationRecord.getProgressStatus());
//        if (status == ScaleStatus.NOT_EVALUATE || status == ScaleStatus.PARENTS_WAITING_WRITE) {
//            scaleEvaluationRecord.setProgressStatus(ScaleStatus.CANCELLED.getStatus());
//            scaleEvaluationRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//            scaleEvaluationRecordDao.updateById(scaleEvaluationRecord);
//        }
//    }
//
//    @Override
//    public void updateEvaluationReserveTime(long id, long time) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(id);
//        if (scaleEvaluationRecord == null) {
//            return;
//        }
//        ScaleStatus status = ScaleStatus.getStatus(scaleEvaluationRecord.getProgressStatus());
//        if (status == ScaleStatus.NOT_EVALUATE) {
//            Reserve reserve = reserveService.getReserve(scaleEvaluationRecord.getReserveId());
//            if (reserve != null) {
//                reserve.setReserveStartTime(time);
//                reserve.setReserveEndTime(reserve.getReserveStartTime() + reserve.getDuration() * 60);
//                reserve.setUpdated(DateUtil.getCurrentTimeStamp());
//                reserveService.updateReserveById(reserve);
//            }
//        }
//    }
//
//    @Override
//    public void updateScoringBook(long id, List<AnswerRequest> answers) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(id);
//        if (scaleEvaluationRecord == null) {
//            return;
//        }
//        ArrayList<AnswerWithRemarkDto> answerWithRemarkDtos = new ArrayList<>(answers.size());
//        for (AnswerRequest answer : answers) {
//            AnswerWithRemarkDto dto = new AnswerWithRemarkDto();
//            dto.setAttachmentDtos(new ArrayList<>());
//            dto.setOptionSn(StringUtils.join(answer.getAnswerSn().toArray(), ","));
//            dto.setQuestionSn(answer.getQuestionSn());
//            dto.setRemark("");
//            dto.setDoctorScore(0);
//            answerWithRemarkDtos.add(dto);
//        }
//        scaleEvaluationRecord.setAnswerWithRemark(JSON.toJSONString(answerWithRemarkDtos));
//        scaleEvaluationRecord.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleEvaluationRecord.setDoctorId(AuthContext.getUserId());
//        scaleEvaluationRecordDao.updateById(scaleEvaluationRecord);
//    }
//
//    @Override
//    public List<AnswerRequest> getScoringBook(long id) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(id);
//        if (scaleEvaluationRecord != null) {
//            String answerWithRemark = scaleEvaluationRecord.getAnswerWithRemark();
//            if (StringUtils.isNotBlank(answerWithRemark)) {
//                List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//                return answerWithRemarkDtos.stream().map(answerWithRemarkDto -> {
//                    AnswerRequest answerRequest = new AnswerRequest();
//                    answerRequest.setQuestionSn(answerWithRemarkDto.getQuestionSn());
//                    String optionSn = answerWithRemarkDto.getOptionSn();
//                    if (optionSn != null && StringUtils.isNotBlank(optionSn)) {
//                        String[] split = optionSn.split(",");
//                        List<Integer> list = Arrays.stream(split).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
//                        answerRequest.setAnswerSn(list);
//                    } else {
//                        answerRequest.setAnswerSn(new ArrayList<>());
//                    }
//                    return answerRequest;
//                }).collect(Collectors.toList());
//            }
//        }
//        return new ArrayList<>();
//    }
//
//    @Override
//    public void reviewScaleReport(ReviewScaleReportRequest request) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(request.getId());
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ScaleStatus currentStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (currentStatus != ScaleStatus.EVALUATED_WAIT_REVIEW) {
//            throw new ServiceException(ResultCode.STATUS_ERROR);
//        }
//        ScaleStatus nextStatus = ScaleStatus.REVIEWED_WAIT_SEND;
//        if (request.isResult() == false) {
//            nextStatus = ScaleStatus.REVIEW_NOT_PASS;
//        }
//        record.setProgressStatus(nextStatus.getStatus());
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleEvaluationRecordDao.updateById(record);
//
//        //增加操作记录
//        scaleEvaluateLogService.insertLog(record.getId(), AuthContext.getUserId(), nextStatus, request.getRemark());
//    }
//
//    @Transactional
//    @Override
//    public void sendReport(long id) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ScaleStatus currentStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (currentStatus != ScaleStatus.REVIEWED_WAIT_SEND) {
//            throw new ServiceException(ResultCode.STATUS_ERROR);
//        }
//
//        //修改状态
//        ScaleStatus nextStatus = ScaleStatus.EVALUATED;
//        record.setProgressStatus(nextStatus.getStatus());
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleEvaluationRecordDao.updateById(record);
//
//        //增加操作记录
//        scaleEvaluateLogService.insertLog(record.getId(), AuthContext.getUserId(), nextStatus, "");
//
//        //重新生成pdf
//        previewReport(record.getId());
//
//        //发送报告
//        try {
//            //发送微信小程序消息
//            ScaleTable scaleTable = ScaleTableResolver.getScaleTableMap().get(record.getScaleTableCode());
//
//            String conclusion = "";
////            String conclusion = record.getConclusion();
////
////            if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
////                conclusion = "有异常";
////                String result = record.getResult();
////                LeiBoAllBodyTestResult leiBoAllBodyTestResult = JSON.parseObject(result, LeiBoAllBodyTestResult.class);
////                if (leiBoAllBodyTestResult != null) {
////                    LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
////                    GMsScaleEvaluationResult gmsResult = leiBoAllBodyTestResult.getGmsResult();
////                    if (!cerebralPalsyResult.getHaveAbnormalIterm() && gmsResult.getStageResult().contains("正常")) {
////                        conclusion = "无异常";
////                    }
////                }
////            } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
////                conclusion = "有异常";
////                String result = record.getResult();
////                LeiBoCerebralPalsySelfTestResult leiBoCerebralPalsySelfTestResult = JSON.parseObject(result, LeiBoCerebralPalsySelfTestResult.class);
////                if (leiBoCerebralPalsySelfTestResult != null) {
////                    if (!leiBoCerebralPalsySelfTestResult.getHaveAbnormalIterm()) {
////                        conclusion = "无异常";
////                    }
////                }
////            }
//            sendWxTemplateMsg(record, scaleTable, conclusion);
//        } catch (Exception e) {
//            log.error("发送微信订阅消息失败:{}", e);
//        }
//    }
//
//    @Override
//    public void submitReview(long id) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ScaleStatus currentStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (currentStatus != ScaleStatus.NOT_EVALUATE && currentStatus != ScaleStatus.REVIEW_NOT_PASS) {
//            throw new ServiceException(ResultCode.STATUS_ERROR);
//        }
//
//        String result = record.getResult();
//        if (ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode()) == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS) {
//            LeiBoAllBodyTestResult leiBoAllBodyTestResult = JSON.parseObject(result, LeiBoAllBodyTestResult.class);
//            LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
//            GMsScaleEvaluationResult gmsResult = leiBoAllBodyTestResult.getGmsResult();
//            if (cerebralPalsyResult == null || gmsResult == null) {
//                throw new ServiceException(ResultCode.GMS_NOT_EVALUATED);
//            }
//
//            if (cerebralPalsyResult != null && (cerebralPalsyResult.getModifyCount() == null
//                    || cerebralPalsyResult.getModifyCount() == 0)) {
//                throw new ServiceException(ResultCode.GMS_NOT_EVALUATED);
//            }
//
//            if (gmsResult != null && StringUtils.isBlank(gmsResult.getStageResult())) {
//                throw new ServiceException(ResultCode.GMS_NOT_EVALUATED);
//            }
//        }
//
//        ScaleStatus nextStatus = ScaleStatus.EVALUATED_WAIT_REVIEW;
//        record.setProgressStatus(nextStatus.getStatus());
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleEvaluationRecordDao.updateById(record);
//
//        //增加操作记录
//        scaleEvaluateLogService.insertLog(record.getId(), AuthContext.getUserId(), nextStatus, "");
//    }
//
//    @Override
//    public ListClinicEvaluateResponse listClinicEvaluate(int pageNo, int pageSize, ScaleStatus scaleStatus, byte orderBy) {
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
//                .eq(ScaleEvaluationRecord::getOrganizationId, AuthContext.getUser().getOrganizationId())
////                .eq(ScaleEvaluationRecord::getReserveType, WorkScheduleType.CLINIC_EVALUATE.getType())
//                .eq(ScaleEvaluationRecord::getProgressStatus, scaleStatus.getStatus());
//        if (orderBy == 1) {
//            wrapper.lambda().orderByAsc(ScaleEvaluationRecord::getReserveStartTime);
//        } else if (orderBy == 2) {
//            wrapper.lambda().orderByAsc(ScaleEvaluationRecord::getUpdated);
//        }
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//
//        List<ClinicEvaluateListVo> collect = records.stream().map(record -> convert2ClinicEvaluateListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListClinicEvaluateResponse(pageVo, collect);
//    }
//
//    @Transactional
//    @Override
//    public void updateClinicSchedule(UpdateClinicScheduleRequest request) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(request.getRecordId());
//        ScaleStatus scaleStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (scaleStatus != ScaleStatus.WAIT_SCHEDULE && scaleStatus != ScaleStatus.SCHEDULED) {
//            throw new ServiceException(ResultCode.STATUS_ERROR);
//        }
//        WorkSchedule workSchedule = workScheduleService.updateClinicSchedule(request, record);
//        record.setProgressStatus(ScaleStatus.TRAINING.getStatus());
//        record.setWorkScheduleId(workSchedule.getId());
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleEvaluationRecordDao.updateById(record);
//
//        Reserve reserve = reserveService.getReserve(record.getReserveId());
//        reserve.setReserveStartTime(request.getStartTime());
//        reserve.setReserveEndTime(request.getEndTime());
//        reserve.setUpdated(DateUtil.getCurrentTimeStamp());
//        reserveService.updateReserveById(reserve);
//
//        WorkScheduleType type = WorkScheduleType.getType(reserve.getType());
//        if (type == WorkScheduleType.RECOVERY_GUIDE_OFFLINE || type == WorkScheduleType.RECOVERY_GUIDE_ONLINE) {
//
//            //修改首次记录时间
//            List<RecoveryTrainingRecordListVo> recoveryTrainingRecordListVos = recoveryTrainingRecordService.listRecord(record.getId());
//            if (CollectionUtils.isNotEmpty(recoveryTrainingRecordListVos)) {
//                RecoveryTrainingRecordListVo recoveryTrainingRecordListVo = recoveryTrainingRecordListVos.get(recoveryTrainingRecordListVos.size() - 1);
//                recoveryTrainingRecordService.updateTrainingRecordReserveTime(recoveryTrainingRecordListVo.getId(), reserve.getReserveStartTime());
//            }
//        }
//
//        try {
//            User user = userService.getUser(record.getUserId());
//            OrganizationVo organizationVo = organizationService.getOrganizationVo(reserve.getOrganizationId());
//            String name = organizationVo.getName();
//            String address = organizationVo.getName();
//            String time = DateUtil.getYMDHMDate(reserve.getReserveStartTime());
//            //发送短信
//            HashMap<String, String> map = new HashMap<>();
//            map.put("name", name);
//            map.put("type", type.getDesc());
////            map.put("type", "门诊评估");
//            map.put("time", time);
//            map.put("address", address);
//            smsSender.send("1610518443814043648", user.getPhone(), map);
//
//            //发送模板消息
//            sendUpdateReserveTemplateMsg(reserve, name);
//        } catch (Exception e) {
//
//        }
//
//    }
//
//    @Override
//    public void cancelClinicSchedule(long recordId) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(recordId);
//        ScaleStatus scaleStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (scaleStatus == ScaleStatus.SCHEDULED) {
//            if (record.getWorkScheduleId() > 0) {
//                workScheduleService.deleteClinicSchedule(record.getWorkScheduleId());
//            }
//            record.setWorkScheduleId(0l);
//            record.setProgressStatus(ScaleStatus.WAIT_SCHEDULE.getStatus());
//            record.setUpdated(DateUtil.getCurrentTimeStamp());
//            scaleEvaluationRecordDao.updateById(record);
//        }
//    }
//
//    @Override
//    public void refund(long recordId) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(recordId);
//        ScaleStatus scaleStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        if (scaleStatus == ScaleStatus.SCHEDULED) {
//            if (record.getWorkScheduleId() > 0) {
//                workScheduleService.deleteClinicSchedule(record.getWorkScheduleId());
//            }
//        }
//        record.setWorkScheduleId(0l);
//        record.setProgressStatus(ScaleStatus.REFUNDED.getStatus());
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleEvaluationRecordDao.updateById(record);
//
//
//        try {
//            Map<String, String> map = new HashMap<>();
//
//            User user = userService.getUser(record.getUserId());
//            OrganizationVo organizationVo = organizationService.getOrganizationVo(record.getOrganizationId());
//            WorkScheduleType type = WorkScheduleType.getType(record.getReserveType());
//            if (type == WorkScheduleType.CLINIC_EVALUATE) {
//                ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode());
//                map.put("scaleTable", scaleTable.getDesc());
//            } else if (type == WorkScheduleType.RECOVERY_GUIDE_OFFLINE){
//
//            }
//
//            map.put("organization", organizationVo.getName());
//            map.put("type", type.getDesc());
//            smsSender.send("1691347204666572800", user.getPhone(), map);
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public List<ClinicScheduleDayVo> listClinicSchedule(String startDay, String endDay) {
//        List<WorkSchedule> workSchedules = workScheduleService.listClinicScheduleByDays(startDay, endDay);
//        Map<Long, List<WorkSchedule>> map = workSchedules.stream().collect(Collectors.groupingBy(WorkSchedule::getDayTime));
//        ArrayList<ClinicScheduleDayVo> clinicScheduleDayVos = new ArrayList<>(map.size());
//        for (Map.Entry<Long, List<WorkSchedule>> entry : map.entrySet()) {
//            List<WorkSchedule> list = entry.getValue();
//            List<ClinicScheduleListVo> collect = list.stream()
//                    .map(s -> convert2ClinicScheduleListVo(s))
//                    .collect(Collectors.toList())
//                    .stream()
//                    .sorted(Comparator.comparing(ClinicScheduleListVo::getStartTime))
//                    .collect(Collectors.toList());
//            clinicScheduleDayVos.add(new ClinicScheduleDayVo(DateUtil.getYMD(entry.getKey()), entry.getKey(), collect));
//        }
//        addOtherDay(clinicScheduleDayVos, DateUtil.getDateTimeYMD(startDay), DateUtil.getDateTimeYMD(endDay));
//        return clinicScheduleDayVos.stream().sorted(Comparator.comparing(ClinicScheduleDayVo::getDayLong)).collect(Collectors.toList());
//    }
//
//    @Override
//    public ClinicRecordVo getClinicDetail(long id) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
//        if(record == null) {
//            return null;
//        }
//        User user = userService.getUser(record.getUserId());
//        ChildrenVo children = childrenService.getChildren(record.getChildrenId());
//        Reserve reserve = reserveService.getReserve(record.getReserveId());
//        ClinicRecordVo vo = new ClinicRecordVo();
//        vo.setPhone(StringUtils.isNotBlank(children.getContactPhone()) ? children.getContactPhone() : user.getPhone());
//        vo.setChildId(record.getChildrenId());
//        vo.setChildName(children.getName());
//        vo.setId(record.getId());
//        vo.setBirthday(children.getBirthday());
//        vo.setProgressStatus(record.getProgressStatus());
//        vo.setReserveStartTime(reserve.getReserveStartTime());
//        vo.setReserveEndTime(reserve.getReserveEndTime());
//
//        long scheduleStartTime = 0l;
//        long scheduleEndTime = 0l;
//        if (record.getProgressStatus() == ScaleStatus.SCHEDULED.getStatus()) {
//            WorkSchedule clinicSchedule = workScheduleService.getClinicSchedule(record.getWorkScheduleId());
//            if (clinicSchedule != null) {
//                scheduleStartTime = clinicSchedule.getStartTime();
//                scheduleEndTime = clinicSchedule.getEndTime();
//            }
//        }
//        vo.setScheduleEndTime(scheduleEndTime);
//        vo.setScheduleStartTime(scheduleStartTime);
//
//        WorkScheduleType type = WorkScheduleType.getType(record.getReserveType());
//        vo.setType(record.getReserveType());
//        if (type == WorkScheduleType.CLINIC_EVALUATE) {
//            ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode());
//            vo.setScaleTableCode(scaleTable.getCode());
//            vo.setScaleTableName(scaleTable.getDesc());
//        } else if (type == WorkScheduleType.RECOVERY_GUIDE_OFFLINE) {
//            vo.setScaleTableCode((byte)0);
//            vo.setScaleTableName("康复指导");
//        }
//        return vo;
//    }
//
//    @Override
//    public ListRecoveryGuideResponse listRecoveryGuide(int pageNo, int pageSize) {
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("organization_id", AuthContext.getUser().getOrganizationId());
//        wrapper.in("reserve_type", Arrays.asList(new Byte[]{WorkScheduleType.RECOVERY_GUIDE_OFFLINE.getType(), WorkScheduleType.RECOVERY_GUIDE_ONLINE.getType()}));
//        wrapper.notIn("progress_status", Arrays.asList(new Byte[]{ScaleStatus.WAIT_SCHEDULE.getStatus()}));
//        wrapper.orderByDesc("created");
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//        List<RecoveryGuideListVo> collect = records.stream().map(record -> convert2RecoveryGuideListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListRecoveryGuideResponse(pageVo, collect);
//    }
//
//    @Override
//    public ListScanCodeRegistrationResponse listScanCodeRegistration(int pageNo, int pageSize) {
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        LambdaQueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<ScaleEvaluationRecord>().lambda();
//        wrapper.eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
//                .eq(ScaleEvaluationRecord::getOrganizationId, AuthContext.getUser().getOrganizationId())
//                .eq(ScaleEvaluationRecord::getReserveType, WorkScheduleType.AI_EVALUATE.getType())
//                .eq(ScaleEvaluationRecord::getCategory, CategoryType.SCAN_CODE_REGISTER.getType())
//                .in(ScaleEvaluationRecord::getDataStatus, Arrays.asList(ScaleStatus.WAIT_UPLOAD.getStatus(), ScaleStatus.UPLOADED.getStatus()))
////                .(w -> w.and(q -> q.eq(ScaleEvaluationRecord::getDataStatus, ScaleStatus.WAIT_UPLOAD.getStatus()))
////                        .or(q -> q.eq(ScaleEvaluationRecord::getDataStatus, ScaleStatus.UPLOADED.getStatus()).eq(ScaleEvaluationRecord::getProgressStatus, ScaleStatus.WAIT_SCHEDULE)))
//                .orderByDesc(ScaleEvaluationRecord::getCreated);
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//        List<ScanCodeRegistrationListVo> list = records.stream().map(this::convert2ScanCodeRegistrationListVo).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListScanCodeRegistrationResponse(pageVo, list);
//    }
//
//    @Override
//    public ScanCodeRegistrationVo getScanCodeRegistration(long id) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
//        if (record == null) {
//            return null;
//        }
//        Children children = childrenService.getChildrenById(record.getChildrenId());
//        ScanCodeRegistrationVo vo = new ScanCodeRegistrationVo();
//        vo.setAge(DateUtil.getAge(children.getBirthday()));
//        vo.setBirthday(DateUtil.getYMD(children.getBirthday()));
//        vo.setBirthWeight(children.getBirthWeight());
//        vo.setGender(Gender.getGender(children.getGender()).getDesc());
//        vo.setName(children.getName());
//        String result = record.getResult();
//        vo.setChildRisks(Arrays.asList(children.getChildRisks().split(",")));
//        vo.setMotherRisks(Arrays.asList(children.getMotherRisks().split(",")));
//        vo.setChildrenId(children.getId());
//        vo.setId(record.getId());
//        vo.setStatus(record.getDataStatus());
//        vo.setUserId(record.getUserId());
//        vo.setGestationalWeek(children.getGestationalWeek() + "周" + children.getGestationalWeekDay() + "天");
//        String correctAge = "";
//        long week = 0;
//        long day = 0;
//        long days = DateUtil.getDaysBetweenTime(record.getCreated(), children.getBirthday());
//        if (children.getGestationalWeek() >= 37) {
//            week = days / 7;
//            day = days % 7;
//            correctAge = week + "周" + day + "天";
//        } else {
//            week = (days - (280 - children.getGestationalWeek() * 7 - children.getGestationalWeekDay())) / 7;
//            day = (days - (280 - children.getGestationalWeek() * 7 - children.getGestationalWeekDay())) % 7;
//            correctAge = week + "周" + day + "天";
//        }
//        vo.setCorrectAge(correctAge);
//
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
//        List<QuestionDto> questions = scaleTable.getQuestions();
//
//        ArrayList<ScanCodeRegistrationQuestionVo> questionVos = new ArrayList<>();
//        String answerWithRemark = record.getAnswerWithRemark();
//        if (StringUtils.isNotBlank(answerWithRemark)) {
//            List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//            for (AnswerWithRemarkDto answerWithRemarkDto : answerWithRemarkDtos) {
//                ScanCodeRegistrationQuestionVo vo1 = new ScanCodeRegistrationQuestionVo();
//                QuestionDto questionDto = getQuestionDto(questions, answerWithRemarkDto.getQuestionSn());
//                if (answerWithRemarkDto.getQuestionSn() == 1) {
//                    vo1.setName("仰卧位视频");
//                } else if (answerWithRemarkDto.getQuestionSn() == 2) {
//                    vo1.setName("扶持迈步视频");
//                } else if (answerWithRemarkDto.getQuestionSn() == 3) {
//                    vo1.setName("孩子不会的大运动(可多选)");
//                }
//                vo1.setId(answerWithRemarkDto.getQuestionSn());
//                //设置附件
//                List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
//                if (CollectionUtils.isNotEmpty(attachmentDtos)) {
//                    ArrayList<AttachmentVo> attachmentVos = new ArrayList<>(attachmentDtos.size());
//                    for (AttachmentDto attachmentDto : attachmentDtos) {
//                        AttachmentVo attachmentVo = new AttachmentVo();
//                        attachmentVo.setFileId(attachmentDto.getFileId());
//                        Files file = fileService.getFile(attachmentDto.getFileId());
//                        Byte fileType = file.getFileType();
//                        if (attachmentDto.getCoverFileId() != 0) {
//                            String coverUrl = fileService.getFileUrl(attachmentDto.getCoverFileId(), false);
//                            attachmentVo.setCoverUrl(coverUrl);
//                        } else {
//                            attachmentVo.setCoverUrl(null);
//                        }
//                        attachmentVo.setType(fileType);
//                        boolean origin = false;
//                        if (fileType == FileType.AUDIO.getCode()) {
//                            origin = true;
//                        }
//                        attachmentVo.setUrl(fileService.getFileUrl(file.getId(), origin));
//                        attachmentVos.add(attachmentVo);
//                    }
//                    vo1.setAttachments(attachmentVos);
//                } else {
//                    vo1.setAttachments(new ArrayList<>());
//                }
//                List<Option> options = questionDto.getOptions();
//                ArrayList<ScaleTableQuestionAnswerVo> answerVos = new ArrayList<>();
//                String optionSn = answerWithRemarkDto.getOptionSn();
//                List<String> strings = Arrays.asList(optionSn.split(","));
//                for (Option option : options) {
//                    ScaleTableQuestionAnswerVo answerVo = new ScaleTableQuestionAnswerVo();
//                    answerVo.setContent(option.getContent());
//                    answerVo.setSn(option.getSn());
//                    if (strings.contains(String.valueOf(option.getSn()))) {
//                        answerVo.setSelected(true);
//                    }
//                    answerVos.add(answerVo);
//                }
//                vo1.setAnswers(answerVos);
//                vo1.setType(questionDto.getType());
//                questionVos.add(vo1);
//            }
//        } else {
//            ScanCodeRegistrationQuestionVo vo1 = new ScanCodeRegistrationQuestionVo();
//            vo1.setAttachments(new ArrayList<>());
//            vo1.setAnswers(new ArrayList<>());
//            vo1.setType(getQuestionDto(questions, 1).getType());
//            vo1.setId(1);
//            vo1.setName("仰卧位视频");
//            questionVos.add(vo1);
//            ScanCodeRegistrationQuestionVo vo2 = new ScanCodeRegistrationQuestionVo();
//            vo2.setAttachments(new ArrayList<>());
//            vo2.setAnswers(new ArrayList<>());
//            vo2.setType(getQuestionDto(questions, 2).getType());
//            vo2.setId(2);
//            vo2.setName("扶持迈步视频");
//            questionVos.add(vo2);
//
//            ScanCodeRegistrationQuestionVo vo3 = new ScanCodeRegistrationQuestionVo();
//            vo3.setAttachments(new ArrayList<>());
//
//            List<Option> options = getQuestionDto(questions, 3).getOptions();
//            ArrayList<ScaleTableQuestionAnswerVo> answerVos = new ArrayList<>();
//            for (Option option : options) {
//                ScaleTableQuestionAnswerVo answerVo = new ScaleTableQuestionAnswerVo();
//                answerVo.setSelected(false);
//                answerVo.setSn(option.getSn());
//                answerVo.setContent(option.getContent());
//                answerVos.add(answerVo);
//            }
//            vo3.setAnswers(answerVos);
//            vo3.setType(getQuestionDto(questions, 3).getType());
//            vo3.setId(3);
//            vo3.setName("孩子不会的大运动(可多选)");
//            questionVos.add(vo3);
//        }
//        vo.setQuestions(questionVos);
//        return vo;
//    }
//
//    @Override
//    public void updateScanCodeRegistration(UpdateScanCodeRegistrationRequest request) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(request.getId());
//        DataStatus dataStatus = DataStatus.getStatus(record.getDataStatus());
//
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
//
//        if (dataStatus == DataStatus.WAIT_UPLOAD) {
//            List<com.fushuhealth.recovery.web.model.request.AnswerRequest> answers = request.getAnswers();
//            ArrayList<AnswerWithRemarkDto> answerWithRemarkDtos = new ArrayList<>();
//
//            int totalScore = 0;
//
//            List<QuestionDto> questions = scaleTable.getQuestions();
//
//            ArrayList<String> abnormalItems = new ArrayList<>();
//            for (com.fushuhealth.recovery.web.model.request.AnswerRequest answer : answers) {
//
//                QuestionDto questionDto = getQuestionDto(questions, answer.getQuestionSn());
//
//                AnswerWithRemarkDto answerWithRemarkDto = new AnswerWithRemarkDto();
//                answerWithRemarkDto.setOptionSn(StringUtils.join(answer.getAnswerSn().toArray(), ","));
//                answerWithRemarkDto.setQuestionSn(answer.getQuestionSn());
//                answerWithRemarkDto.setRemark("");
//
//                if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()
//                        || scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
//
//                    List<Integer> answerSns = answer.getAnswerSn();
//                    for (Integer answerSn : answerSns) {
//                        Option option = getOption(questionDto.getOptions(), answerSn);
//                        if (option.getScore() > 0) {
//                            answerWithRemarkDto.setDoctorScore(option.getScore());
//                            totalScore += option.getScore();
//                            QuestionSubjectEnum questionSubject = QuestionSubjectEnum.getQuestionSubject(questionDto.getSubject());
//                            abnormalItems.add(questionSubject.getDesc() + "-" + questionDto.getName());
//                        }
//                    }
//
//                }
//
//                List<Long> attachments = answer.getAttachmentIds();
//                if (CollectionUtils.isNotEmpty(attachments)) {
//                    ArrayList<AttachmentDto> attachmentDtos = new ArrayList<>();
//                    for (Long attachment : attachments) {
//                        long coverFileId = 0l;
//                        long fileId = attachment;
//
//                        Files videoFile = fileService.getFile(fileId);
//                        log.info("get file from qiniu: {}", JSON.toJSONString(videoFile));
//                        FileType type = FileType.getType(videoFile.getFileType());
//                        String key = type.getName() + "/" + videoFile.getFilePath();
//
//                        File localFile = fileService.downloadFile(videoFile.getId());
//                        fileStorage.convertToM3u8(FileType.VIDEO.getName(), key);
//
//                        try {
//                            String coverName = StringUtil.uuid() + ".jpg";
//                            VideoInfo videoInfo = VideoTool.getVideoInfo(localFile.getAbsolutePath(), coverName);
//                            String coverPath = fileService.saveFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//                            log.info("cover file name : {}", coverPath);
//                            Files coverFile = new Files();
//                            coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//                            coverFile.setExtension(FilenameUtils.getExtension(coverName));
//                            coverFile.setFilePath(coverPath);
//                            coverFile.setFileSize(videoInfo.getCover().length());
//                            coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//                            coverFile.setFileType(FileType.PICTURE.getCode());
//                            coverFile.setOriginalName(coverName);
//                            coverFile.setRawName(coverName);
//                            coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//                            fileService.insertFiles(coverFile);
//                            coverFileId = coverFile.getId();
//                        } catch (Exception e) {
//                            log.error("generate mp4 cover error:{}", e.getMessage());
//                        }
//                        attachmentDtos.add(new AttachmentDto(fileId, coverFileId, "", ""));
//                    }
//                    answerWithRemarkDto.setAttachmentDtos(attachmentDtos);
//                }
//                answerWithRemarkDtos.add(answerWithRemarkDto);
//            }
//            String result = "";
//            if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
//                LeiBoAllBodyTestResult leiBoAllBodyTestResult = new LeiBoAllBodyTestResult();
//                LeiBoCerebralPalsySelfTestResult cerebralPalsyScaleEvaluateResult = new LeiBoCerebralPalsySelfTestResult();
//                cerebralPalsyScaleEvaluateResult.setPositionAndSportAbnormal(getDefaultPositionAndSportIterms());
//                cerebralPalsyScaleEvaluateResult.setHint("以上建议是基于此次婴幼儿神经运动发育风险家庭自测影像和GMs影像，经过计算机自动测评，专业人员审核视频得出。由于家庭自测影像和GMs影像的拍摄角度、清晰度、光线、拍摄质量会存在很大差异，以及拍摄人员的专业程度和文化背景不同，计算机自动识别判断会有学习遗漏和判断差异，人工审核视频也不能同面诊相比。因此，此报告只对此次影像和家长填写的问卷负责。请及时预约线下服务，通过专业机构明确诊断。");
//                leiBoAllBodyTestResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);
//
//                result = JSON.toJSONString(leiBoAllBodyTestResult);
//            }
//            record.setDoctorScore(totalScore);
//            record.setAnswerWithRemark(JSON.toJSONString(answerWithRemarkDtos));
//            record.setResult(result);
//            record.setUpdated(DateUtil.getCurrentTimeStamp());
//            if (request.getCommit()) {
//                record.setProgressStatus(ScaleStatus.NOT_EVALUATE.getStatus());
//                record.setDataStatus(DataStatus.UPLOADED.getStatus());
//            }
//            record.setDataUploadSource(DataUploadSource.WEB.getCode());
//            scaleEvaluationRecordDao.updateById(record);
//        }
//    }
//
//    @Transactional
//    @Override
//    public void submitScanCodeRegistration(SubmitScanCodeRegistrationRequest request) {
//        //查询用户，如果不存在，则新建用户
//        Long organizationId = AuthContext.getUser().getOrganizationId();
//        User user = userService.saveUserIfNotExist(organizationId, request.getPhone(), request.getName());
//        long userId = user.getId();
//        SaveChildrenRequest saveChildrenRequest = new SaveChildrenRequest();
//        saveChildrenRequest.setBirthday(DateUtil.getStartTimeOfDay(request.getBirthday().getTime() / 1000));
//        saveChildrenRequest.setGender(request.getGender());
//        saveChildrenRequest.setName(request.getChildName());
//        saveChildrenRequest.setChildRisks(request.getChildRisks());
//        saveChildrenRequest.setContactPhone(request.getPhone());
//        saveChildrenRequest.setBirthdayWeight(request.getBirthdayWeight());
//        saveChildrenRequest.setGestationalWeek(request.getGestationalWeek());
//        saveChildrenRequest.setGestationalWeekDay(request.getGestationalWeekDay());
//        saveChildrenRequest.setMotherRisks(request.getMotherRisks());
//        saveChildrenRequest.setExtraRisks(request.getExtraRisks());
//        saveChildrenRequest.setMedicalCardNumber(request.getMedicalCardNumber());
//        //保存儿童信息
//        Long childrenId = childrenService.saveChildren(userId, saveChildrenRequest);
//
//        //保存记录
//        ScaleEvaluationRecord record = new ScaleEvaluationRecord();
//        record.setReserveType(WorkScheduleType.AI_EVALUATE.getType());
//        record.setReserveId(0L);
//        record.setProgressStatus(ScaleStatus.WAIT_UPLOAD.getStatus());
//        record.setDataStatus(DataStatus.WAIT_UPLOAD.getStatus());
//        record.setDataUploadSource(DataUploadSource.WEB.getCode());
//        record.setConclusion("");
//        record.setResult("");
//        record.setDoctorId(0L);
//        record.setDoctorScore(0);
//        record.setUserScore(0);
//        record.setAnswerWithRemark("");
//        record.setResultFileId(0L);
//        record.setUpdated(DateUtil.getCurrentTimeStamp());
//        record.setBirthday(0L);
//        record.setChildrenId(childrenId);
//        record.setCreated(DateUtil.getCurrentTimeStamp());
//        record.setGender(Gender.UNKNOWN.getCode());
//        record.setName("");
//        record.setOpenId("");
//        record.setResultPics("");
//        ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(request.getScaleTableCode());
//        record.setScaleTableCode(scaleTable.getCode());
//        record.setScaleTableName(scaleTable.getDesc());
//        record.setType(scaleTable.getClassification());
//        record.setStatus(BaseStatus.NORMAL.getStatus());
//        record.setUserId(userId);
//        record.setOrganizationId(organizationId);
//        record.setCategory(CategoryType.SCAN_CODE_REGISTER.getType());
//        record.setChannel("");
//        record.setOrgId("");
//        record.setAppId(record.getAppId());
//        scaleEvaluationRecordDao.insert(record);
//    }
//
//    @Override
//    public List<ScaleEvaluationRecord> listByChildrenId(long childrenId) {
//        LambdaQueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<ScaleEvaluationRecord>().lambda();
//        wrapper.eq(ScaleEvaluationRecord::getChildrenId, childrenId)
//                .eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
//                .eq(ScaleEvaluationRecord::getReserveType, WorkScheduleType.AI_EVALUATE.getType())
//                .eq(ScaleEvaluationRecord::getCategory, CategoryType.NORMAL.getType());
//        return scaleEvaluationRecordDao.selectList(wrapper);
//    }
//
    private List<PositionAndSportIterm> getDefaultPositionAndSportIterms() {
        ArrayList<PositionAndSportIterm> positionAndSportIterms = new ArrayList<>();
        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode());
        List<QuestionDto> questions = scaleTable.getQuestions();
        for (QuestionDto question : questions) {
            PositionAndSportIterm positionAndSportIterm = new PositionAndSportIterm();
            positionAndSportIterm.setName(question.getName());
            positionAndSportIterm.setStatus(0);
            positionAndSportIterm.setOptionSn(1);
            positionAndSportIterm.setQuestionSn(question.getSn());
            positionAndSportIterms.add(positionAndSportIterm);
        }
        return  positionAndSportIterms;
    }
//
//    private ScanCodeRegistrationListVo convert2ScanCodeRegistrationListVo(ScaleEvaluationRecord record) {
//        if (record == null) {
//            return null;
//        }
//        ScanCodeRegistrationListVo vo = new ScanCodeRegistrationListVo();
//        User user = userService.getUser(record.getUserId());
//        ChildrenVo children = childrenService.getChildren(record.getChildrenId());
//        vo.setPhone(user.getPhone());
//        vo.setChannel(record.getChannel());
//        vo.setOrgId(record.getOrgId());
//        vo.setStatus(record.getDataStatus());
//        vo.setChildrenName(children.getName());
//        vo.setBirthday(DateUtil.getYMD(children.getBirthdayDate()));
//        vo.setId(record.getId());
//
//        ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode());
//        if (scaleTable != null) {
//            vo.setScaleTableCode(scaleTable.getCode());
//            vo.setScaleTableName(scaleTable.getDesc());
//        } else {
//            vo.setScaleTableCode((byte)0);
//            vo.setScaleTableName("");
//        }
//        vo.setUserName(user.getName());
//        return vo;
//    }
//
//    private List<String> getBigMovement(AnswerWithRemarkDto answer, QuestionDto questionDto) {
//        if (answer == null || StringUtils.isBlank(answer.getOptionSn()) || questionDto == null) {
//            return Collections.EMPTY_LIST;
//        }
//        ArrayList<String> strings = new ArrayList<>();
//        String[] split = answer.getOptionSn().split(",");
//        List<Option> options = questionDto.getOptions();
//        for (String s : split) {
//            int i = Integer.parseInt(s);
//            for (Option option : options) {
//                if (i == option.getSn()) {
//                    strings.add(option.getContent());
//                }
//            }
//        }
//        return strings;
//    }
//
//    private RecoveryGuideListVo convert2RecoveryGuideListVo(ScaleEvaluationRecord record) {
//        RecoveryGuideListVo vo = new RecoveryGuideListVo();
//        ChildrenVo children = childrenService.getChildren(record.getChildrenId());
//        User user = userService.getUser(record.getUserId());
//        if (record.getReserveStartTime() == 0) {
//            Reserve reserve = reserveService.getReserve(record.getReserveId());
//            vo.setReserveTime(DateUtil.getYMDHMDate(reserve.getReserveStartTime()));
//        } else {
//            vo.setReserveTime(DateUtil.getYMDHMDate(record.getReserveStartTime()));
//        }
//        if (user != null) {
//            vo.setPhone(user.getPhone());
//            vo.setUserName(user.getName());
//        } else {
//            vo.setPhone("");
//            vo.setUserName("");
//        }
//        vo.setChildrenName(children.getName());
//        vo.setId(record.getId());
//
//        vo.setStatus(record.getProgressStatus());
//        return vo;
//    }
//
//    private void addOtherDay(List<ClinicScheduleDayVo> list, long startDay, long endDay) {
//        List<String> collect = list.stream().map(ClinicScheduleDayVo::getDay).collect(Collectors.toList());
//        while (startDay <= endDay) {
//            String ymd = DateUtil.getYMD(startDay);
//            if (!collect.contains(ymd)) {
//                list.add(new ClinicScheduleDayVo(ymd, startDay, new ArrayList<>()));
//                collect.add(ymd);
//            }
//            startDay += 24 * 60 * 60;
//        }
//    }
//
//    private ClinicScheduleListVo convert2ClinicScheduleListVo(WorkSchedule workSchedule) {
//        if (workSchedule == null) {
//            return null;
//        }
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(workSchedule.getRecordId());
//        Children children = childrenService.getChildrenById(record.getChildrenId());
//        DoctorVo doctorVo = doctorService.getDoctorVo(workSchedule.getDoctorId());
//        ClinicScheduleListVo vo = new ClinicScheduleListVo();
//        vo.setRecordId(workSchedule.getRecordId());
//        vo.setEndTime(DateUtil.getHM(workSchedule.getEndTime()));
//        vo.setStartTime(DateUtil.getHM(workSchedule.getStartTime()));
//        vo.setDoctorName(doctorVo != null ? doctorVo.getName() : "");
//        vo.setDoctorId(workSchedule.getDoctorId());
//        vo.setChildId(children.getId());
//        vo.setChildName(children.getName());
//        WorkScheduleType type = WorkScheduleType.getType(record.getReserveType());
//        vo.setType(type.getType());
//        if (type == WorkScheduleType.CLINIC_EVALUATE || type == WorkScheduleType.AI_EVALUATE) {
//            ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode());
//            vo.setScaleTableName(scaleTable.getDesc());
//            vo.setScaleTableCode(scaleTable.getCode());
//        } else if (type == WorkScheduleType.RECOVERY_GUIDE_OFFLINE) {
//            vo.setScaleTableName("康复指导");
//            vo.setScaleTableCode((byte)0);
//        }
//        return vo;
//    }
//
//    private ScaleOrderListVo convert2ScaleOrderListVo(ScaleOrder scaleOrder) {
//        User user = userService.getUser(scaleOrder.getUserId());
//        ScaleOrderListVo vo = new ScaleOrderListVo();
//        vo.setCreated(DateUtil.getYMDHMSDate(scaleOrder.getCreated()));
//        vo.setId(scaleOrder.getId());
//        String invoiceFileId = scaleOrder.getInvoiceFileId();
//        if (invoiceFileId != null && !invoiceFileId.equalsIgnoreCase("null")) {
//            if (invoiceFileId.contains("[")) {
//                List<String> invoiceFileIds = JSON.parseArray(invoiceFileId, String.class);
//                vo.setInvoiceUrl(CollectionUtils.isEmpty(invoiceFileIds) ? new ArrayList<>() : invoiceFileIds.stream().map(id -> fileService.getFileUrl(Long.parseLong(id), true)).collect(Collectors.toList()));
//            } else {
//                vo.setInvoiceUrl(Arrays.asList(new String[]{fileService.getFileUrl(Long.parseLong(invoiceFileId), true)}));
//            }
//        }
//
//        vo.setPhone(user.getPhone());
//        ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(scaleOrder.getScaleTableCode());
//        ScaleTableConstant.ScaleTableClassification scaleTableClassification = ScaleTableConstant.ScaleTableClassification.getScaleTableClassification(scaleTable.getClassification());
//        vo.setScaleTableName(scaleTable.getDesc());
//        vo.setScaleTableClassification(scaleTableClassification.getDesc());
//        vo.setStatus(scaleOrder.getOrderStatus());
//        vo.setStatusStr(ScaleOrderStatus.getScaleOrderStatus(scaleOrder.getOrderStatus()).getDesc());
//        vo.setUserId(scaleOrder.getUserId());
//        vo.setUserName(user.getName());
//        return vo;
//    }
//
//    private ClinicEvaluateListVo convert2ClinicEvaluateListVo(ScaleEvaluationRecord record) {
//        ClinicEvaluateListVo vo = new ClinicEvaluateListVo();
//        ChildrenVo children = childrenService.getChildren(record.getChildrenId());
//        User user = userService.getUser(record.getUserId());
//        WorkScheduleType type = WorkScheduleType.getType(record.getReserveType());
//        //预约类型
//        vo.setScaleTableClassification(type.getDesc());
//        if (type == WorkScheduleType.CLINIC_EVALUATE || type == WorkScheduleType.AI_EVALUATE) {
//            ScaleTableConstant.ScaleTableCode scaleTable = ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode());
//            vo.setScaleTableName(scaleTable.getDesc());
//            vo.setScaleTableCode(scaleTable.getCode());
//        } else if (type == WorkScheduleType.RECOVERY_GUIDE_OFFLINE) {
//            vo.setScaleTableName("康复指导");
//            vo.setScaleTableCode((byte)0);
//        }
//        vo.setId(record.getId());
//        vo.setPhone(user.getPhone());
//        Reserve reserve = reserveService.getReserve(record.getReserveId());
//        if (record.getReserveStartTime() == 0) {
//            vo.setReserveTime(DateUtil.getYMDHMDate(reserve.getReserveStartTime()));
//        } else {
//            vo.setReserveTime(DateUtil.getYMDHMDate(record.getReserveStartTime()));
//        }
//        vo.setReviewTime(DateUtil.getYMDHMDate(reserve.getReviewTime()));
//
//        vo.setStatus(record.getProgressStatus());
//        vo.setUserName(children.getName());
//
//        CategoryType categoryType = CategoryType.getType(record.getCategory());
//        vo.setCategory(categoryType.getDesc());
//        String answerWithRemark = record.getAnswerWithRemark();
//        ArrayList<AttachmentVo> videos = new ArrayList<>();
//        if (StringUtils.isNotBlank(answerWithRemark)) {
//            List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//            if (CollectionUtils.isNotEmpty(answerWithRemarkDtos)) {
//                AnswerWithRemarkDto answerWithRemarkDto = answerWithRemarkDtos.get(0);
//                List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
//                for (AttachmentDto attachmentDto : attachmentDtos) {
//                    AttachmentVo attachmentVo = new AttachmentVo();
//                    attachmentVo.setCoverUrl(attachmentDto.getCoverFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getCoverFileId(), false));
//                    attachmentVo.setUrl(attachmentDto.getFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getFileId(), false));
//                    attachmentVo.setFileId(attachmentDto.getFileId());
//                    attachmentVo.setType(attachmentVo.getType());
//                    videos.add(attachmentVo);
//                }
//            }
//        }
//        vo.setVideos(videos);
//        return vo;
//    }
//
//    private List<PositionAndSportIterm> answerOption2PositionAndSportIterm(byte scaleTableCode, List<AnswerAndOption> answerAndOptions) {
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(scaleTableCode);
//        if (scaleTable == null) {
//            return null;
//        }
//        ArrayList<PositionAndSportIterm> list = new ArrayList<>(answerAndOptions.size());
//        List<QuestionDto> questions = scaleTable.getQuestions();
//        for (AnswerAndOption answerAndOption : answerAndOptions) {
//            QuestionDto questionDto = getQuestionDto(questions, answerAndOption.getQuestionSn());
//            List<Option> options = questionDto.getOptions();
//            Option option = getOption(options, answerAndOption.getOptionSn());
//            if (option != null) {
//                String content = option.getContent();
//                Integer score = option.getScore();
//                PositionAndSportIterm positionAndSportIterm = new PositionAndSportIterm();
//                positionAndSportIterm.setQuestionSn(questionDto.getSn());
//                positionAndSportIterm.setOptionSn(option.getSn());
//                positionAndSportIterm.setStatus(option.getScore());
//                positionAndSportIterm.setName(questionDto.getName());
//                list.add(positionAndSportIterm);
//            }
//        }
//        return list;
//    }
//
    private Map<String, Set<String>> addAbnormalVideo(List<String> abnormalIterm, ScaleEvaluationRecord record) {

        HashMap<String, Set<String>> map = new HashMap<>();

        //增加异常项
        for (String s : abnormalIterm) {
            //获取每个异常项对应的视频
            AbnormalActionVideo abnormalActionVideo = abnormalActionVideoMap.get(s);
            if (abnormalActionVideo != null) {
                Set<String> videoVos = new HashSet<>();
                List<VideoDto> videos = abnormalActionVideo.getVideos();
                for (VideoDto video : videos) {
                    videoVos.add(video.getName());
                }
                map.put(s, videoVos);
            }
        }
        return map;
    }

    private Map<String, Set<String>> addBigMoveVideo(ScaleEvaluationRecord record) {

        HashMap<String, Set<String>> map = new HashMap<>();

        //增加功能训练
        List<String> trainMethod = getTrainMethod(record);
        for (String s : trainMethod) {
            //获取每个异常项对应的视频
            AbnormalActionVideo abnormalActionVideo = abnormalActionVideoMap.get(s);
            if (abnormalActionVideo != null) {
                Set<String> videoVos = new HashSet<>();
                List<VideoDto> videos = abnormalActionVideo.getVideos();
                for (VideoDto video : videos) {
                    videoVos.add(video.getName());
                }
                map.put(s, videoVos);
            }
        }
        return map;
    }

    private List<String> getTrainMethod(ScaleEvaluationRecord record) {
        ArrayList<String> list = new ArrayList<>();

        String answerWithRemark = record.getAnswerWithRemark();
        List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
        if (CollectionUtils.isEmpty(answerWithRemarkDtos)) {
            return list;
        }
        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
        if (scaleTable == null) {
            return list;
        }
        List<QuestionDto> questions = scaleTable.getQuestions();

        if (answerWithRemarkDtos.size() == 3) {
            AnswerWithRemarkDto answerWithRemarkDto = answerWithRemarkDtos.get(2);
            QuestionDto questionDto = getQuestionDto(questions, answerWithRemarkDto.getQuestionSn());

            String[] options = answerWithRemarkDto.getOptionSn().split(",");

            for (String optionSn : options) {
                if (StringUtils.isNotBlank(optionSn)) {
                    Option option = getOption(questionDto.getOptions(), Integer.parseInt(optionSn));
                    if (option != null) {
                        list.add(option.getContent());
                    }
                }
            }
        }
        return list;
    }

//    private ScaleRecordListVo convertToScaleRecordListVo(ScaleEvaluationRecord record) {
//        ScaleRecordListVo vo = new ScaleRecordListVo();
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
//        if (scaleTable == null) {
//            return null;
//        }
//        User user = userService.getUser(record.getUserId());
//        if (record.getChildrenId() > 0) {
//            ChildrenVo children = childrenService.getChildren(record.getChildrenId());
//            vo.setUserName(children.getName());
//            vo.setBirthday(children.getBirthday());
//        } else {
//            vo.setUserName(user.getName());
//        }
//
//        vo.setClassification(ScaleTableConstant.ScaleTableClassification
//                .getScaleTableClassification(scaleTable.getClassification()).getDesc());
//        vo.setCreated(DateUtil.getYMDHMSDate(record.getCreated()));
//        vo.setDoctorScore(record.getDoctorScore());
//        vo.setId(record.getId());
//        vo.setPhone(user.getPhone());
//        ScaleStatus scaleStatus = ScaleStatus.getStatus(record.getProgressStatus());
//        vo.setProgressStatus(scaleStatus.getDesc());
//        vo.setScaleTableName(scaleTable.getName());
//        vo.setUserId(user.getId());
//        vo.setUserScore(record.getUserScore());
//        vo.setProgressStatusByte(record.getProgressStatus());
//        vo.setScaleTableCode(scaleTable.getCode());
//        String answerWithRemark = record.getAnswerWithRemark();
//        ArrayList<AttachmentVo> videos = new ArrayList<>();
//        if (StringUtils.isNotBlank(answerWithRemark)) {
//            List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//            if (CollectionUtils.isNotEmpty(answerWithRemarkDtos)) {
//                AnswerWithRemarkDto answerWithRemarkDto = answerWithRemarkDtos.get(0);
//                List<AttachmentDto> attachmentDtos = answerWithRemarkDto.getAttachmentDtos();
//                if (CollectionUtils.isNotEmpty(attachmentDtos)) {
//                    for (AttachmentDto attachmentDto : attachmentDtos) {
//                        if (attachmentDto != null) {
//                            AttachmentVo attachmentVo = new AttachmentVo();
//                            attachmentVo.setCoverUrl(attachmentDto.getCoverFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getCoverFileId(), false));
//                            attachmentVo.setUrl(attachmentDto.getFileId() == 0 ? "" : fileService.getFileUrl(attachmentDto.getFileId(), false));
//                            videos.add(attachmentVo);
//                        }
//                    }
//                }
//            }
//        }
//        vo.setVideos(videos);
//
//        if (scaleTable.getCode() != ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
//            vo.setHaveAbnormalIterm(0);
//            vo.setGmsResult("");
//        } else {
//            if (StringUtils.isBlank(record.getResult())) {
//                vo.setHaveAbnormalIterm(0);
//                vo.setGmsResult("");
//            } else {
//                Children children = childrenService.getChildrenById(record.getChildrenId());
//                LeiBoAllBodyTestResult leiBoAllBodyTestResult = (LeiBoAllBodyTestResult) getReport(record, children);
//                LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
//
//                if (scaleStatus == ScaleStatus.NOT_EVALUATE) {
//                    if (cerebralPalsyResult == null || cerebralPalsyResult.getModifyCount() == null || cerebralPalsyResult.getModifyCount() == 0) {
//                        vo.setHaveAbnormalIterm(0);
//                    } else {
//                        Integer videoStatus = cerebralPalsyResult.getVideoStatus();
//                        if (videoStatus != null && videoStatus == 1) {
//                            vo.setHaveAbnormalIterm(3);
//                        } else if (cerebralPalsyResult.getHaveAbnormalIterm()) {
//                            vo.setHaveAbnormalIterm(1);
//                        } else {
//                            vo.setHaveAbnormalIterm(2);
//                        }
//                    }
//                } else {
//                    Integer videoStatus = cerebralPalsyResult.getVideoStatus();
//                    if (videoStatus != null && videoStatus == 1) {
//                        vo.setHaveAbnormalIterm(3);
//                    } else if (cerebralPalsyResult.getHaveAbnormalIterm()) {
//                        vo.setHaveAbnormalIterm(1);
//                    } else {
//                        vo.setHaveAbnormalIterm(2);
//                    }
//                }
////                if (cerebralPalsyResult == null || cerebralPalsyResult.getModifyCount() == null || cerebralPalsyResult.getModifyCount() == 0) {
////                    vo.setHaveAbnormalIterm(0);
////                } else {
////                    Integer videoStatus = cerebralPalsyResult.getVideoStatus();
////                    if (videoStatus != null && videoStatus == 1) {
////                        vo.setHaveAbnormalIterm(3);
////                    } else if (cerebralPalsyResult.getHaveAbnormalIterm()) {
////                        vo.setHaveAbnormalIterm(1);
////                    } else {
////                        vo.setHaveAbnormalIterm(2);
////                    }
////                }
////                if (cerebralPalsyResult != null && cerebralPalsyResult.getModifyCount() != null && cerebralPalsyResult.getModifyCount() > 0) {
////
////                } else {
////                    vo.setHaveAbnormalIterm(0);
////                }
//
//                GMsScaleEvaluationResult gmsResult = leiBoAllBodyTestResult.getGmsResult();
//                if (gmsResult != null) {
//                    vo.setGmsResult(gmsResult.getStageResult());
//                } else {
//                    vo.setGmsResult("");
//                }
//            }
//        }
//
//        if (record.getOrderId() != null && record.getOrderId() > 0) {
//            ScaleOrder scaleOrder = scaleOrderService.getById(record.getOrderId());
//            if (scaleOrder != null && scaleOrder.getDoctorId() != null && scaleOrder.getDoctorId() > 0) {
//                DoctorVo doctorVo = doctorService.getDoctorVo(scaleOrder.getDoctorId());
//                vo.setDoctorId(doctorVo.getId());
//                vo.setDoctorName(doctorVo.getName());
//            } else {
//                vo.setDoctorName("");
//                vo.setDoctorId(0l);
//            }
//        } else {
//            vo.setDoctorName("");
//            vo.setDoctorId(0l);
//        }
//        return vo;
//    }
//
//    private AnswerWithRemarkDto getAnswerWithRemarkDto(List<AnswerWithRemarkDto> answers, int answerId) {
//        for (AnswerWithRemarkDto answer : answers) {
//            if (answer.getQuestionSn() == answerId) {
//                return answer;
//            }
//        }
//        return null;
//    }
//
    private QuestionDto getQuestionDto(List<QuestionDto> questionDtos, int questionSn) {

        for (QuestionDto questionDto : questionDtos) {
            if (questionDto.getSn() == questionSn) {
                return questionDto;
            }
        }
        return null;
    }

    private Option getOption(List<Option> options, int optionSn) {
        for (Option option : options) {
            if (option.getSn() == optionSn) {
                return option;
            }
        }
        return null;
    }

    private BaseScaleEvaluationResult getReport(ScaleEvaluationRecord record, Children children) {
        BaseScaleEvaluationResult vo;
        String result = record.getResult();
        if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
            LeiBoCerebralPalsySelfTestResult leiBoCerebralPalsySelfTestResult = JSON.parseObject(result, LeiBoCerebralPalsySelfTestResult.class);
            if (leiBoCerebralPalsySelfTestResult == null) {
                leiBoCerebralPalsySelfTestResult = new LeiBoCerebralPalsySelfTestResult();
            }

            List<String> motherRisks = StringUtils.isNotBlank(risksService.RisksExChanged(children.getDangerOfMother())) ?
                    Arrays.asList(risksService.RisksExChanged(children.getDangerOfMother()).split(",")) : new ArrayList<>();
            List<String> risks = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(motherRisks)) {
                risks.addAll(motherRisks);
            }
            List<String> childRisks = StringUtils.isNotBlank(risksService.RisksExChanged(children.getDangerOfChild())) ?
                    Arrays.asList(risksService.RisksExChanged(children.getDangerOfChild()).split(",")) : new ArrayList<>();
            if (CollectionUtils.isNotEmpty(childRisks)) {
                risks.addAll(childRisks);
            }
            leiBoCerebralPalsySelfTestResult.setHighRisk(risks);
            leiBoCerebralPalsySelfTestResult.setHaveHighRisk(!CollectionUtils.isEmpty(risks));

            List<PositionAndSportIterm> positionAndSportAbnormal = leiBoCerebralPalsySelfTestResult.getPositionAndSportAbnormal();

            //计算得分
            leiBoCerebralPalsySelfTestResult.setCerebralPalsyScore(positionAndSportAbnormal.stream().mapToInt(PositionAndSportIterm::getStatus).sum());
            //计算异常项
            ArrayList<String> abnormalIterm = new ArrayList<>();
            if (positionAndSportAbnormal != null) {
                for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
                    if (iterm.getStatus() > 0) {
                        abnormalIterm.add(iterm.getName());
                    }
                }
            }
            leiBoCerebralPalsySelfTestResult.setAbnormalIterm(abnormalIterm);
            leiBoCerebralPalsySelfTestResult.setHaveAbnormalIterm(!CollectionUtils.isEmpty(abnormalIterm));

            List<SuggestVo> suggestVo = getSuggestVo(calScaleRecordResult(risks, abnormalIterm));
            leiBoCerebralPalsySelfTestResult.setLeiboSuggest(suggestVo);
            leiBoCerebralPalsySelfTestResult.setAbnormalVideoNames(addAbnormalVideo(abnormalIterm, record));
            leiBoCerebralPalsySelfTestResult.setBigMoveVideoNames(addBigMoveVideo(record));
            return leiBoCerebralPalsySelfTestResult;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            LeiBoAllBodyTestResult leiBoAllBodyTestResult = JSON.parseObject(result, LeiBoAllBodyTestResult.class);

            //脑瘫
            LeiBoCerebralPalsySelfTestResult leiBoCerebralPalsySelfTestResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
            if (leiBoCerebralPalsySelfTestResult == null) {
                leiBoCerebralPalsySelfTestResult = new LeiBoCerebralPalsySelfTestResult();
            }
            List<String> motherRisks = StringUtils.isNotBlank(risksService.RisksExChanged(children.getDangerOfMother())) ?
                    Arrays.asList(risksService.RisksExChanged(children.getDangerOfMother()).split(",")) : new ArrayList<>();
            List<String> risks = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(motherRisks)) {
                risks.addAll(motherRisks);
            }
            List<String> childRisks = StringUtils.isNotBlank(risksService.RisksExChanged(children.getDangerOfChild())) ?
                    Arrays.asList(risksService.RisksExChanged(children.getDangerOfChild()).split(",")) : new ArrayList<>();
            if (CollectionUtils.isNotEmpty(childRisks)) {
                risks.addAll(childRisks);
            }
            leiBoCerebralPalsySelfTestResult.setHighRisk(risks);
            leiBoCerebralPalsySelfTestResult.setHaveHighRisk(!CollectionUtils.isEmpty(risks));

            List<PositionAndSportIterm> positionAndSportAbnormal = leiBoCerebralPalsySelfTestResult.getPositionAndSportAbnormal();

            //计算异常项
            ArrayList<String> abnormalIterm = new ArrayList<>();
            if (positionAndSportAbnormal != null) {
                //计算得分
                leiBoCerebralPalsySelfTestResult.setCerebralPalsyScore(positionAndSportAbnormal.stream().mapToInt(PositionAndSportIterm::getStatus).sum());
                for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
                    if (iterm.getStatus() > 0) {
                        abnormalIterm.add(iterm.getName());
                    }
                }
            }
            leiBoCerebralPalsySelfTestResult.setAbnormalIterm(abnormalIterm);
            leiBoCerebralPalsySelfTestResult.setHaveAbnormalIterm(!CollectionUtils.isEmpty(abnormalIterm));

            List<SuggestVo> suggestVo = getSuggestVo(calScaleRecordResult(risks, abnormalIterm));
            leiBoCerebralPalsySelfTestResult.setLeiboSuggest(suggestVo);

            //处理 gms
            String gmsCopyWriting = "您的宝宝本次测评GMs正常";
            GMsScaleEvaluationResult gmsResult = leiBoAllBodyTestResult.getGmsResult();
            if (gmsResult != null) {
//                List<GMsResultIterm> itermList = gmsResult.getResult();
//                if (CollectionUtils.isNotEmpty(itermList)) {
//                    GMsResultIterm gMsResultIterm = itermList.get(0);
//                    String content = gMsResultIterm.getContent();
//                    if (!content.equals("正常（N）") && !content.equals("正常 (F+)")) {
//                        gmsCopyWriting = "您的宝宝本次测评GMs出现异常：" + content;
//                    }
//                }
                String stageResult = gmsResult.getStageResult();
                if (stageResult != null) {
                    if (!stageResult.equals("正常（N）") && !stageResult.equals("正常 (F+)")) {
                        gmsCopyWriting = "您的宝宝本次测评GMs出现异常：" + stageResult;
                    }
                } else {
                    List<GMsResultIterm> list = gmsResult.getResult();
                    if (CollectionUtils.isNotEmpty(list)) {
                        String name = list.get(0).getName();
                        if (name.equals("扭动阶段")) {
                            gmsResult.setStage("1");
                        } else {
                            gmsResult.setStage("2");
                        }
                        String content = list.get(0).getContent();
                        gmsResult.setStageResult(content);
                        if (!content.equals("正常（N）") && !content.equals("正常 (F+)")) {
                            gmsCopyWriting = "您的宝宝本次测评GMs出现异常：" + content;
                        }
                        gmsResult.setGmsCopyrighting("您的宝宝本次测评GMs正常");
                    }
                }
            } else {
                gmsResult = new GMsScaleEvaluationResult();
            }
            gmsResult.setGmsCopyrighting(gmsCopyWriting);

            leiBoAllBodyTestResult.setCerebralPalsyResult(leiBoCerebralPalsySelfTestResult);
            leiBoAllBodyTestResult.setGmsResult(gmsResult);
            leiBoAllBodyTestResult.setAbnormalVideoNames(addAbnormalVideo(abnormalIterm, record));
            leiBoAllBodyTestResult.setBigMoveVideoNames(addBigMoveVideo(record));
            return leiBoAllBodyTestResult;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode()) {
            GriffithsResult griffithsResult = JSON.parseObject(result, GriffithsResult.class);
            if (griffithsResult == null) {
                griffithsResult = getDefaultGriffithsResult();
            }
            List<Long> scoringBookFileIds = griffithsResult.getScoringBookFileIds();
            if (CollectionUtils.isNotEmpty(scoringBookFileIds)) {
                List<String> urls = scoringBookFileIds.stream().map(id -> fileService.getFileUrl(id, false)).collect(Collectors.toList());
                griffithsResult.setScoringBookUrls(urls);

                List<ScoringBookVo> collect = scoringBookFileIds.stream().map(id -> {
                    Files files = fileService.getFile(id);
                    String fileUrl = fileService.getFileUrl(id, true);
                    return new ScoringBookVo(id, files.getOriginalName(), fileUrl);
                }).collect(Collectors.toList());
                griffithsResult.setScoringBookVos(collect);
            }
            return griffithsResult;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.WEISHI_CHILD_INTELLIGENCE.getCode()) {
            WeishiChildIntelligenceResult childIntelligenceResult = JSON.parseObject(result, WeishiChildIntelligenceResult.class);
            if (childIntelligenceResult == null) {
                //返回默认结果
                childIntelligenceResult = getDefaultWeishiChildIntelligenceResult();
            }
            List<Long> scoringBookFileIds = childIntelligenceResult.getScoringBookFileIds();
            if (CollectionUtils.isNotEmpty(scoringBookFileIds)) {
                List<String> urls = scoringBookFileIds.stream().map(id -> fileService.getFileUrl(id, false)).collect(Collectors.toList());
                childIntelligenceResult.setScoringBookUrls(urls);

                List<ScoringBookVo> collect = scoringBookFileIds.stream().map(id -> {
                    Files files = fileService.getFile(id);
                    String fileUrl = fileService.getFileUrl(id, true);
                    return new ScoringBookVo(id, files.getOriginalName(), fileUrl);
                }).collect(Collectors.toList());
                childIntelligenceResult.setScoringBookVos(collect);
            }
            return childIntelligenceResult;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.WEISHI_TODDLER_INTELLIGENCE.getCode()) {
            WeishiToddlerIntelligenceResult toddlerIntelligenceResult = JSON.parseObject(result, WeishiToddlerIntelligenceResult.class);
            if (toddlerIntelligenceResult == null) {
                //返回默认结果
                toddlerIntelligenceResult = getDefaultWeishiToddlerIntelligenceResult();
            }
            List<Long> scoringBookFileIds = toddlerIntelligenceResult.getScoringBookFileIds();
            if (CollectionUtils.isNotEmpty(scoringBookFileIds)) {
                List<String> urls = scoringBookFileIds.stream().map(id -> fileService.getFileUrl(id, false)).collect(Collectors.toList());
                toddlerIntelligenceResult.setScoringBookUrls(urls);

                List<ScoringBookVo> collect = scoringBookFileIds.stream().map(id -> {
                    Files files = fileService.getFile(id);
                    String fileUrl = fileService.getFileUrl(id, true);
                    return new ScoringBookVo(id, files.getOriginalName(), fileUrl);
                }).collect(Collectors.toList());
                toddlerIntelligenceResult.setScoringBookVos(collect);
            }
            return toddlerIntelligenceResult;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_8.getCode()) {
            GMs8Result gMs8Result = JSON.parseObject(result, GMs8Result.class);
            return gMs8Result;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS.getCode()) {
            GMs8Result gMs8Result = JSON.parseObject(result, GMs8Result.class);
            return gMs8Result;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMFM88.getCode()) {
            GMFM88Result gmfm88Result = JSON.parseObject(result, GMFM88Result.class);
            return gmfm88Result;
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.CAREGIVER_BURDEN.getCode()) {
            CaregiverBurdenResult caregiverBurdenResult = JSON.parseObject(result, CaregiverBurdenResult.class);
            return caregiverBurdenResult;
        } else {
            List<ScaleTableConstant.ScaleTableCode> scaleTables = ScaleTableConstant.ScaleTableCode.getScaleTableByClassification(ScaleTableConstant.ScaleTableClassification.DDST);
            Set<Byte> scaleTableCodesSet = scaleTables.stream().map(ScaleTableConstant.ScaleTableCode::getCode).collect(Collectors.toSet());
            if (scaleTableCodesSet.contains(record.getScaleTableCode())) {
                DDSTResult ddstResult = JSON.parseObject(result, DDSTResult.class);
                return ddstResult;
            }
        }
        return null;
    }

    private GriffithsResult getDefaultGriffithsResult() {
        GriffithsResult griffithsResult = new GriffithsResult();
        griffithsResult.setScoringBookFileIds(new ArrayList<Long>());
        griffithsResult.setScoringBookUrls(new ArrayList<String>());
        griffithsResult.setRemark("");

        List<List<BaseTableResultCell>> table = new ArrayList<>();

        ArrayList<BaseTableResultCell> griffithsResultCells0 = new ArrayList<>();
        BaseTableResultCell griffithsResultCell00 = new BaseTableResultCell("label", "", "", "label");
        BaseTableResultCell griffithsResultCell01 = new BaseTableResultCell("A", "粗大运动", "粗大运动", "input");
        BaseTableResultCell griffithsResultCell02 = new BaseTableResultCell("B", "个人社会", "个人社会", "input");
        BaseTableResultCell griffithsResultCell03 = new BaseTableResultCell("C", "听力语言", "听力语言", "input");
        BaseTableResultCell griffithsResultCell04 = new BaseTableResultCell("D", "手眼协调", "手眼协调", "input");
        BaseTableResultCell griffithsResultCell05 = new BaseTableResultCell("E", "视觉表现", "视觉表现", "input");
        BaseTableResultCell griffithsResultCell06 = new BaseTableResultCell("F", "实际推理", "实际推理", "input");
        BaseTableResultCell griffithsResultCell07 = new BaseTableResultCell("G", "总商", "总商", "input");
        griffithsResultCells0.add(griffithsResultCell00);
        griffithsResultCells0.add(griffithsResultCell01);
        griffithsResultCells0.add(griffithsResultCell02);
        griffithsResultCells0.add(griffithsResultCell03);
        griffithsResultCells0.add(griffithsResultCell04);
        griffithsResultCells0.add(griffithsResultCell05);
        griffithsResultCells0.add(griffithsResultCell06);
        griffithsResultCells0.add(griffithsResultCell07);

        ArrayList<BaseTableResultCell> griffithsResultCells1 = new ArrayList<>();
        BaseTableResultCell griffithsResultCell10 = new BaseTableResultCell("", "裸值", "裸值", "");
        BaseTableResultCell griffithsResultCell11 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell12 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell13 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell14 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell15 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell16 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell17 = new BaseTableResultCell("", "", "", "");
        griffithsResultCells1.add(griffithsResultCell10);
        griffithsResultCells1.add(griffithsResultCell11);
        griffithsResultCells1.add(griffithsResultCell12);
        griffithsResultCells1.add(griffithsResultCell13);
        griffithsResultCells1.add(griffithsResultCell14);
        griffithsResultCells1.add(griffithsResultCell15);
        griffithsResultCells1.add(griffithsResultCell16);
        griffithsResultCells1.add(griffithsResultCell17);

        ArrayList<BaseTableResultCell> griffithsResultCells2 = new ArrayList<>();
        BaseTableResultCell griffithsResultCell20 = new BaseTableResultCell("", "百分位数", "百分位数", "");
        BaseTableResultCell griffithsResultCell21 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell22 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell23 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell24 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell25 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell26 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell27 = new BaseTableResultCell("", "", "", "");
        griffithsResultCells2.add(griffithsResultCell20);
        griffithsResultCells2.add(griffithsResultCell21);
        griffithsResultCells2.add(griffithsResultCell22);
        griffithsResultCells2.add(griffithsResultCell23);
        griffithsResultCells2.add(griffithsResultCell24);
        griffithsResultCells2.add(griffithsResultCell25);
        griffithsResultCells2.add(griffithsResultCell26);
        griffithsResultCells2.add(griffithsResultCell27);

        ArrayList<BaseTableResultCell> griffithsResultCells3 = new ArrayList<>();
        BaseTableResultCell griffithsResultCell30 = new BaseTableResultCell("", "与发育相当的月龄", "与发育相当的月龄", "");
        BaseTableResultCell griffithsResultCell31 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell32 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell33 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell34 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell35 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell36 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell griffithsResultCell37 = new BaseTableResultCell("", "", "", "");
        griffithsResultCells3.add(griffithsResultCell30);
        griffithsResultCells3.add(griffithsResultCell31);
        griffithsResultCells3.add(griffithsResultCell32);
        griffithsResultCells3.add(griffithsResultCell33);
        griffithsResultCells3.add(griffithsResultCell34);
        griffithsResultCells3.add(griffithsResultCell35);
        griffithsResultCells3.add(griffithsResultCell36);
        griffithsResultCells3.add(griffithsResultCell37);

        table.add(griffithsResultCells0);
        table.add(griffithsResultCells1);
        table.add(griffithsResultCells2);
        table.add(griffithsResultCells3);
        griffithsResult.setTable(table);
        return griffithsResult;
    }

    private WeishiToddlerIntelligenceResult getDefaultWeishiToddlerIntelligenceResult() {
        WeishiToddlerIntelligenceResult result = new WeishiToddlerIntelligenceResult();
        result.setScoringBookFileIds(new ArrayList<Long>());
        result.setScoringBookUrls(new ArrayList<String>());
        result.setRemark(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}));

        List<String> scaleScoreTableTitles = Arrays.asList(new String[]{"积木", "常识", "矩阵推理", "找虫", "图片记忆", "类同", "画图概念",
                "划消", "动物家园", "拼图", "动物译码", "常识", "指认图片", "图片命名", "随机划消", "有序划消", "量表分数总和"});
        List<List<BaseTableResultCell>> scaleScoreTable = new ArrayList<>();
        List<BaseTableResultCell> scaleScoreTable0 = new ArrayList<>();
        BaseTableResultCell scaleScoreTable00 = new BaseTableResultCell("label", "分测验", "分测验", "label");
        BaseTableResultCell scaleScoreTable01 = new BaseTableResultCell("A", "原始分数", "原始分数", "input");
        BaseTableResultCell scaleScoreTable02 = new BaseTableResultCell("B", "言语理解", "言语理解", "input");
        BaseTableResultCell scaleScoreTable03 = new BaseTableResultCell("C", "视觉空间", "视觉空间", "input");
        BaseTableResultCell scaleScoreTable04 = new BaseTableResultCell("D", "流体推理", "流体推理", "input");
        BaseTableResultCell scaleScoreTable05 = new BaseTableResultCell("E", "工作记忆", "工作记忆", "input");
        BaseTableResultCell scaleScoreTable06 = new BaseTableResultCell("F", "加工速度", "加工速度", "input");
        BaseTableResultCell scaleScoreTable07 = new BaseTableResultCell("G", "全量表", "全量表", "input");
        BaseTableResultCell scaleScoreTable08 = new BaseTableResultCell("H", "强弱项", "强弱项", "input");
        scaleScoreTable0.add(scaleScoreTable00);
        scaleScoreTable0.add(scaleScoreTable01);
        scaleScoreTable0.add(scaleScoreTable02);
        scaleScoreTable0.add(scaleScoreTable03);
        scaleScoreTable0.add(scaleScoreTable04);
        scaleScoreTable0.add(scaleScoreTable05);
        scaleScoreTable0.add(scaleScoreTable06);
        scaleScoreTable0.add(scaleScoreTable07);
        scaleScoreTable0.add(scaleScoreTable08);
        scaleScoreTable.add(scaleScoreTable0);

        for (String title : scaleScoreTableTitles) {
            ArrayList<BaseTableResultCell> sst = new ArrayList<>();
            BaseTableResultCell sst0 = new BaseTableResultCell("", title, title, "");
            sst.add(sst0);
            for (int i = 1; i < 9; i++) {
                sst.add(new BaseTableResultCell("", "", "", ""));
            }
            scaleScoreTable.add(sst);
        }
        result.setScaleScoreTable(scaleScoreTable);

        List<List<BaseTableResultCell>> scaleScoreTransferTable = new ArrayList<>();
        List<BaseTableResultCell> scaleScoreTransferTable0 = new ArrayList<>();
        BaseTableResultCell scaleScoreTransferTable001 = new BaseTableResultCell("label", "", "", "label");
        BaseTableResultCell scaleScoreTransferTable00 = new BaseTableResultCell("A", "量表", "量表", "input");
        BaseTableResultCell scaleScoreTransferTable01 = new BaseTableResultCell("B", "量表分数总和", "量表分数总和", "input");
        BaseTableResultCell scaleScoreTransferTable02 = new BaseTableResultCell("C", "", "", "input");
        BaseTableResultCell scaleScoreTransferTable03 = new BaseTableResultCell("D", "合成分数", "合成分数", "input");
        BaseTableResultCell scaleScoreTransferTable04 = new BaseTableResultCell("E", "百分等级", "百分等级", "input");
        BaseTableResultCell scaleScoreTransferTable05 = new BaseTableResultCell("F", "95%置信区间", "95%置信区间", "input");
        BaseTableResultCell scaleScoreTransferTable06 = new BaseTableResultCell("G", "智力水平", "智力水平", "input");
        scaleScoreTransferTable0.add(scaleScoreTransferTable001);
        scaleScoreTransferTable0.add(scaleScoreTransferTable00);
        scaleScoreTransferTable0.add(scaleScoreTransferTable01);
        scaleScoreTransferTable0.add(scaleScoreTransferTable02);
        scaleScoreTransferTable0.add(scaleScoreTransferTable03);
        scaleScoreTransferTable0.add(scaleScoreTransferTable04);
        scaleScoreTransferTable0.add(scaleScoreTransferTable05);
        scaleScoreTransferTable0.add(scaleScoreTransferTable06);
        scaleScoreTransferTable.add(scaleScoreTransferTable0);

        List<String> scaleScoreTransferTableTitles = Arrays.asList(new String[]{"言语理解", "视觉空间", "流体推理", "工作记忆", "加工速度"});
        for (String title : scaleScoreTransferTableTitles) {
            List<BaseTableResultCell> sstt = new ArrayList<>();
            BaseTableResultCell scaleScoreTransferTable10 = new BaseTableResultCell("", title, title, "");
            BaseTableResultCell scaleScoreTransferTable11 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable111 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable12 = new BaseTableResultCell("", title + "指数", title + "指数", "");
            BaseTableResultCell scaleScoreTransferTable13 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable14 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable15 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable16 = new BaseTableResultCell("", "", "", "");
            sstt.add(scaleScoreTransferTable10);
            sstt.add(scaleScoreTransferTable11);
            sstt.add(scaleScoreTransferTable111);
            sstt.add(scaleScoreTransferTable12);
            sstt.add(scaleScoreTransferTable13);
            sstt.add(scaleScoreTransferTable14);
            sstt.add(scaleScoreTransferTable15);
            sstt.add(scaleScoreTransferTable16);

            scaleScoreTransferTable.add(sstt);
        }
        List<BaseTableResultCell> scaleScoreTransferTable6 = new ArrayList<>();
        BaseTableResultCell scaleScoreTransferTable60 = new BaseTableResultCell("", "全量表", "全量表", "");
        BaseTableResultCell scaleScoreTransferTable61 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable611 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable62 = new BaseTableResultCell("", "总智商", "总智商", "");
        BaseTableResultCell scaleScoreTransferTable63 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable64 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable65 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable66 = new BaseTableResultCell("", "", "", "");
        scaleScoreTransferTable6.add(scaleScoreTransferTable60);
        scaleScoreTransferTable6.add(scaleScoreTransferTable61);
        scaleScoreTransferTable6.add(scaleScoreTransferTable611);
        scaleScoreTransferTable6.add(scaleScoreTransferTable62);
        scaleScoreTransferTable6.add(scaleScoreTransferTable63);
        scaleScoreTransferTable6.add(scaleScoreTransferTable64);
        scaleScoreTransferTable6.add(scaleScoreTransferTable65);
        scaleScoreTransferTable6.add(scaleScoreTransferTable66);

        scaleScoreTransferTable.add(scaleScoreTransferTable6);
        result.setScaleScoreTransferTable(scaleScoreTransferTable);
        return result;
    }

    private WeishiChildIntelligenceResult getDefaultWeishiChildIntelligenceResult() {
        WeishiChildIntelligenceResult result = new WeishiChildIntelligenceResult();
        result.setScoringBookFileIds(new ArrayList<Long>());
        result.setScoringBookUrls(new ArrayList<String>());
        result.setRemark(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}));

        List<String> scaleScoreTableTitles = Arrays.asList(new String[]{"积木", "类同", "背数", "找虫", "画图概念",
                "译码", "词汇", "字母－数字", "矩阵推理", "理解", "符号检索", "填图", "划消", "常识", "算术", "量表分数总和"});
        List<List<BaseTableResultCell>> scaleScoreTable = new ArrayList<>();
        List<BaseTableResultCell> scaleScoreTable0 = new ArrayList<>();
        BaseTableResultCell scaleScoreTable00 = new BaseTableResultCell("label", "", "", "label");
        BaseTableResultCell scaleScoreTable01 = new BaseTableResultCell("A", "原始分数", "原始分数", "input");
        BaseTableResultCell scaleScoreTable02 = new BaseTableResultCell("B", "言语理解", "言语理解", "input");
        BaseTableResultCell scaleScoreTable03 = new BaseTableResultCell("C", "知觉推理", "知觉推理", "input");
        BaseTableResultCell scaleScoreTable04 = new BaseTableResultCell("D", "工作记忆", "工作记忆", "input");
        BaseTableResultCell scaleScoreTable05 = new BaseTableResultCell("E", "加工速度", "加工速度", "input");
        BaseTableResultCell scaleScoreTable06 = new BaseTableResultCell("F", "全量表", "全量表", "input");
        scaleScoreTable0.add(scaleScoreTable00);
        scaleScoreTable0.add(scaleScoreTable01);
        scaleScoreTable0.add(scaleScoreTable02);
        scaleScoreTable0.add(scaleScoreTable03);
        scaleScoreTable0.add(scaleScoreTable04);
        scaleScoreTable0.add(scaleScoreTable05);
        scaleScoreTable0.add(scaleScoreTable06);
        scaleScoreTable.add(scaleScoreTable0);

        for (String title : scaleScoreTableTitles) {
            ArrayList<BaseTableResultCell> sst = new ArrayList<>();
            BaseTableResultCell sst0 = new BaseTableResultCell("", title, title, "");
            sst.add(sst0);
            for (int i = 1; i < 7; i++) {
                sst.add(new BaseTableResultCell("", "", "", ""));
            }
            scaleScoreTable.add(sst);
        }
        result.setScaleScoreTable(scaleScoreTable);

        List<List<BaseTableResultCell>> scaleScoreTransferTable = new ArrayList<>();
        List<BaseTableResultCell> scaleScoreTransferTable0 = new ArrayList<>();
        BaseTableResultCell scaleScoreTransferTable001 = new BaseTableResultCell("label", "", "", "label");
        BaseTableResultCell scaleScoreTransferTable00 = new BaseTableResultCell("A", "量表", "量表", "input");
        BaseTableResultCell scaleScoreTransferTable01 = new BaseTableResultCell("B", "量表分数总和", "量表分数总和", "input");
        BaseTableResultCell scaleScoreTransferTable02 = new BaseTableResultCell("C", "", "", "input");
        BaseTableResultCell scaleScoreTransferTable03 = new BaseTableResultCell("D", "合成分数", "合成分数", "input");
        BaseTableResultCell scaleScoreTransferTable04 = new BaseTableResultCell("E", "百分等级", "百分等级", "input");
        BaseTableResultCell scaleScoreTransferTable05 = new BaseTableResultCell("F", "95%置信区间", "95%置信区间", "input");
        BaseTableResultCell scaleScoreTransferTable06 = new BaseTableResultCell("G", "智力水平", "智力水平", "input");
        scaleScoreTransferTable0.add(scaleScoreTransferTable001);
        scaleScoreTransferTable0.add(scaleScoreTransferTable00);
        scaleScoreTransferTable0.add(scaleScoreTransferTable01);
        scaleScoreTransferTable0.add(scaleScoreTransferTable02);
        scaleScoreTransferTable0.add(scaleScoreTransferTable03);
        scaleScoreTransferTable0.add(scaleScoreTransferTable04);
        scaleScoreTransferTable0.add(scaleScoreTransferTable05);
        scaleScoreTransferTable0.add(scaleScoreTransferTable06);
        scaleScoreTransferTable.add(scaleScoreTransferTable0);

        List<String> scaleScoreTransferTableTitles = Arrays.asList(new String[]{"言语理解", "知觉推理", "工作记忆", "加工速度"});
        for (String title : scaleScoreTransferTableTitles) {
            List<BaseTableResultCell> sstt = new ArrayList<>();
            BaseTableResultCell scaleScoreTransferTable10 = new BaseTableResultCell("", title, title, "");
            BaseTableResultCell scaleScoreTransferTable11 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable111 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable12 = new BaseTableResultCell("", title + "指数", title + "指数", "");
            BaseTableResultCell scaleScoreTransferTable13 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable14 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable15 = new BaseTableResultCell("", "", "", "");
            BaseTableResultCell scaleScoreTransferTable16 = new BaseTableResultCell("", "", "", "");
            sstt.add(scaleScoreTransferTable10);
            sstt.add(scaleScoreTransferTable11);
            sstt.add(scaleScoreTransferTable111);
            sstt.add(scaleScoreTransferTable12);
            sstt.add(scaleScoreTransferTable13);
            sstt.add(scaleScoreTransferTable14);
            sstt.add(scaleScoreTransferTable15);
            sstt.add(scaleScoreTransferTable16);

            scaleScoreTransferTable.add(sstt);
        }
        List<BaseTableResultCell> scaleScoreTransferTable6 = new ArrayList<>();
        BaseTableResultCell scaleScoreTransferTable60 = new BaseTableResultCell("", "全量表", "全量表", "");
        BaseTableResultCell scaleScoreTransferTable61 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable611 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable62 = new BaseTableResultCell("", "总智商", "总智商", "");
        BaseTableResultCell scaleScoreTransferTable63 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable64 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable65 = new BaseTableResultCell("", "", "", "");
        BaseTableResultCell scaleScoreTransferTable66 = new BaseTableResultCell("", "", "", "");
        scaleScoreTransferTable6.add(scaleScoreTransferTable60);
        scaleScoreTransferTable6.add(scaleScoreTransferTable61);
        scaleScoreTransferTable6.add(scaleScoreTransferTable611);
        scaleScoreTransferTable6.add(scaleScoreTransferTable62);
        scaleScoreTransferTable6.add(scaleScoreTransferTable63);
        scaleScoreTransferTable6.add(scaleScoreTransferTable64);
        scaleScoreTransferTable6.add(scaleScoreTransferTable65);
        scaleScoreTransferTable6.add(scaleScoreTransferTable66);

        scaleScoreTransferTable.add(scaleScoreTransferTable6);
        result.setScaleScoreTransferTable(scaleScoreTransferTable);
        return result;
    }

    private SuggestVo getSuggestVo(List<SuggestVo> suggestVos, String suggest) {
        for (SuggestVo suggestVo : suggestVos) {
            if (suggestVo.getContent().equals(suggest)) {
                return suggestVo;
            }
        }
        return new SuggestVo(suggest, null);
    }

    private List<SuggestVo> getSuggestVo(String result) {
        return suggestMap.get(result).getSuggest();
    }
//
//    private void sendWxTemplateMsg(ScaleEvaluationRecord record, ScaleTable scaleTable, String conclusion) {
//        LoginVo user = userService.getUserLogin(record.getUserId());
//        WxUser wxUser = wxUserService.getWxUser(user.getId(), PlatformEnum.WX_MINI_APP);
//
//        String appId = user.getAppId();
//        if (StringUtils.isNotBlank(appId)) {
//            String page = "";
//            if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS.getCode()) {
//                page = "pages/evaluate/gmsDetail" + "?id=" + record.getId();
//            } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
//                page = "pages/evaluate/brainDetail" + "?id=" + record.getId();
//            } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
//                page = "/pages/evaluate/brainGmsDetail" + "?id=" + record.getId();
//            } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
//                page = "/evaluatePackage/pages/stepDetail" + "?id=" + record.getId();
//            } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
//                page = "/evaluatePackage/pages/stepDetail" + "?id=" + record.getId();
//            } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GRIFFITHS.getCode()) {
//                page = "/pages/evaluate/previewReport?id=" + record.getId() + "&name=" + "格里菲斯发育评估（Griffiths）";
//            }
//
//            if (StringUtils.isEmpty(conclusion)) {
//                conclusion = "评估结果不代表专业诊断，建议您到相关机构做进一步诊断";
//            }
//
//            if (appId.equals(AppConstant.YOUBAOSHANYU)) {
//                String templateId = "WazgebdTA_-hWtIvKM1b3c___7qjF6qRs-YkhJqfhGI";
//                ScaleOrder scaleOrder = scaleOrderService.getScaleOrderByEvId(record.getId());
//                String amount = scaleOrder == null ? "0" : StringUtil.fenToYuan(scaleOrder.getPaidFee());
//
//                WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
//                        .templateId(templateId)
//                        .data(Lists.newArrayList(
//                                new WxMaSubscribeMessage.MsgData("thing1", scaleTable.getName()),
//                                new WxMaSubscribeMessage.MsgData("amount2", amount),
//                                new WxMaSubscribeMessage.MsgData("date3", DateUtil.getYMDHMDate(record.getCreated())),
//                                new WxMaSubscribeMessage.MsgData("thing4", "恩泽医疗"),
//                                new WxMaSubscribeMessage.MsgData("phrase5", "报告已完成")))
//                        .toUser(wxUser.getOpenId())
//                        .page(page)
//                        .miniprogramState(env.equalsIgnoreCase("test") ? "trial" : "formal")
//                        .build();
//
//                try {
//                    wxMaService.switchover(appId);
//                    wxMaService.getMsgService().sendSubscribeMsg(message);
//                } catch (WxErrorException e) {
//                    log.error("发送生成报告微信微信小程序消息失败,code:{}, msg{}", e.getError().getErrorCode(), e.getError().getErrorMsg());
//                }
//            } else {
//                String templateId = "0uUpTebwJQRY49Lcq6IysK3apBtJvKZphwCaccuLCX8";
//                if (appId.equals(AppConstant.ERTONGYIYUAN)) {
//                    templateId = "wX7XL2cIwkQdd839m0r0uSlIPXYtW0Z-4eB_TtGoe4s";
//                } else if (appId.equals(AppConstant.LEIBO)) {
//                    templateId = "6AMD3K0mD6tvHZtgu0CDaEycoURravDElAZSZlQwss8";
//                } else if (appId.equals(AppConstant.YINUOYUNJIAN)) {
//                    templateId = "gp-AX-aGiqPzlzymQWV-PbR2r1IZqY88paFiuKACudk";
//                } else if (appId.equals(AppConstant.YUJINGPING)) {
//                    templateId = "qI8ugBGmFiUOWlhWks7vP5v9PUNWTEJ-meFiyLOexPE";
//                } else if (appId.equals(AppConstant.DONGFANGTONGFAYUZHIDAO)) {
//                    templateId = "lkBNOe5cshhPEx_OVXb_B5QX34YZ-YsKHZdPVOqEmS8";
//                } else if (appId.equals(AppConstant.DONGFANGTONG)) {
//                    templateId = "33_30dEdQpJv9P8WZy9tUJMlAtfAjyhHnXJ0dknFltg";
//                } else if (appId.equals(AppConstant.TEYANGXINXI)) {
//                    templateId = "FnFpEHdWq0_wKZ70PMAy_Fbuu2YbYPNFKUZ9vf7FLAI";
//                } else if (appId.equals(AppConstant.ERTONGSHENJINGFAYU)) {
//                    templateId = "4Sh3mhpfFVYApiovRXvlomBY8p7zNDg4Sdhk1DaFtUc";
//                }
//                //小程序订阅消息
//                WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
//                        .templateId(templateId)
//                        .data(Lists.newArrayList(
//                                new WxMaSubscribeMessage.MsgData("thing1", scaleTable.getName()),
//                                new WxMaSubscribeMessage.MsgData("time2", DateUtil.getYMDHMSDate(record.getUpdated())),
//                                new WxMaSubscribeMessage.MsgData("thing3", conclusion)))
//                        .toUser(wxUser.getOpenId())
//                        .page(page)
//                        .miniprogramState(env.equalsIgnoreCase("test") ? "trial" : "formal")
//                        .build();
//
//                try {
//                    wxMaService.switchover(appId);
//                    wxMaService.getMsgService().sendSubscribeMsg(message);
//                } catch (WxErrorException e) {
//                    log.error("发送生成报告微信微信小程序消息失败,code:{}, msg{}", e.getError().getErrorCode(), e.getError().getErrorMsg());
//                }
//            }
//
//
////
//
//
//            //公众号
//            //发送模板消息
////        if (wxUser != null && StringUtils.isNotBlank(wxUser.getOpenId())) {
////
////            ArrayList<WxMpTemplateData> list = new ArrayList<>();
////            list.add(new WxMpTemplateData("first", "您的筛查报告已生成", "#173177"));
////            list.add(new WxMpTemplateData("keyword1", scaleTable.getName(), "#173177"));
////            list.add(new WxMpTemplateData("keyword2", record.getConclusion(), "#173177"));
////            list.add(new WxMpTemplateData("remark", "复数健康，脑科学数字化精准康复变革者！", "#173177"));
////
////            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
////                    .toUser(wxUser.getOpenId()).templateId(scaleFeedbackTemplateId).data(list).url(scaleFeedbackUrl + record.getId()).build();
////            try {
////                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
////            } catch (WxErrorException e) {
////                log.error("发送生成报告微信模板消息失败,code:{}, msg{}", e.getError().getErrorCode(), e.getError().getErrorMsg());
////            }
////        }
//        }
//    }
//
//    private void pdfAddWaterMark(File originFile, File outputFile, String waterMark) throws Exception {
//        // 原PDF文件
//        PdfReader reader = new PdfReader(originFile.getAbsolutePath());
//        // 输出的PDF文件内容
//        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile.getAbsolutePath()));
//
//        // 字体 来源于 itext-asian JAR包
//        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
//
//        PdfGState gs = new PdfGState();
//        // 设置透明度
//        gs.setFillOpacity(0.2f);
//        gs.setStrokeOpacity(0.3f);
//
//        int totalPage = reader.getNumberOfPages() + 1;
//        for (int i = 1; i < totalPage; i++) {
//            // 内容上层
//            PdfContentByte content = stamper.getOverContent(i);
//            // 内容下层
////            PdfContentByte content = stamper.getUnderContent(i);
//
//            content.beginText();
//            // 字体添加透明度
//            content.setGState(gs);
//            // 添加字体大小等
//            content.setFontAndSize(baseFont, 50);
//            // 添加范围
//            content.setTextMatrix(70, 200);
//
//            // 具体位置 内容 旋转多少度 共360度
//            content.showTextAligned(Element.ALIGN_CENTER, waterMark, 300, 200, 45);
//            content.showTextAligned(Element.ALIGN_CENTER, waterMark, 300, 600, 45);
////            content.showTextAligned(Element.ALIGN_CENTER, waterMark, 300, 350, 45);
////                content.showTextAligned(Element.ALIGN_TOP, waterMark, 100, 100, 5);
////                content.showTextAligned(Element.ALIGN_BOTTOM, waterMark, 400, 400, 75);
//
//            content.endText();
//        }
//
//        // 关闭
//        stamper.close();
//        reader.close();
//    }
//
//    private void sendUpdateReserveTemplateMsg(Reserve reserve, String orgName) {
//        LoginVo user = userService.getUserLogin(reserve.getUserId());
//        WxUser wxUser = wxUserService.getWxUser(user.getId(), PlatformEnum.WX_MINI_APP);
//        ChildrenVo children = childrenService.getChildren(reserve.getChildrenId());
//        String appId = user.getAppId();
//        if (StringUtils.isNotBlank(appId)) {
//            String page = "/orderPackage/pages/book/records";
//
//            String templateId = "i753aJ7iEhmSayKg5WGmSjWWZQcVwQZIB5JGA1FTSf4";
//            if (appId.equalsIgnoreCase(AppConstant.ERTONGYIYUAN)) {
//                templateId = "tBp3rkaPAE9MBsy5oB40MqFMfySUrh83u0vHYhZoUIM";
//            } else if (appId.equalsIgnoreCase(AppConstant.LEIBO)) {
//                templateId = "WqGASWLsCR0EM2B40-pzt9-5Oj8PH5nps2yOkYTB0aQ";
//            }
//            //小程序订阅消息
//            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
//                    .templateId(templateId)
//                    .data(Lists.newArrayList(
//                            new WxMaSubscribeMessage.MsgData("name1", children.getName()),
//                            new WxMaSubscribeMessage.MsgData("thing7", orgName),
//                            new WxMaSubscribeMessage.MsgData("thing3", WorkScheduleType.getType(reserve.getType()).getDesc()),
//                            new WxMaSubscribeMessage.MsgData("date2", DateUtil.getYMDHMDate(reserve.getReserveStartTime())),
//                            new WxMaSubscribeMessage.MsgData("thing4", ReserveStatus.getStatus(reserve.getReserveStatus()).getDesc())))
//                    .toUser(wxUser.getOpenId())
//                    .page(page)
//                    .miniprogramState(env.equalsIgnoreCase("test") ? "trial" : "formal")
//                    .build();
//            try {
//                wxMaService.switchover(appId);
//                wxMaService.getMsgService().sendSubscribeMsg(message);
//            } catch (WxErrorException e) {
//                log.error("发送预约完成微信小程序消息失败,code:{}, msg{}", e.getError().getErrorCode(), e.getError().getErrorMsg());
//            }
//        }
//    }
}
