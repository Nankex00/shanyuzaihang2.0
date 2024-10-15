package com.fushuhealth.recovery.h5.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.*;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.storage.FileStorage;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.NumberUtil;
import com.fushuhealth.recovery.common.util.PdfUtil;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ScaleEvaluationRecordDao;
import com.fushuhealth.recovery.dal.dto.*;
import com.fushuhealth.recovery.dal.entity.*;
import com.fushuhealth.recovery.dal.scale.ScaleTableResolver;
import com.fushuhealth.recovery.dal.vo.AbnormalItermInterventionMethodVo;
import com.fushuhealth.recovery.dal.vo.InterventionMethodVo;
import com.fushuhealth.recovery.dal.vo.SuggestVo;
import com.fushuhealth.recovery.dal.vo.VideoVo;
import com.fushuhealth.recovery.dal.vo.h5.CheckVideoOrderVo;
import com.fushuhealth.recovery.dal.vo.h5.LoginVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleEvaluationRecordListVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleOrderVo;
import com.fushuhealth.recovery.h5.model.request.AnswerRequest;
import com.fushuhealth.recovery.h5.model.request.AttachmentRequest;
import com.fushuhealth.recovery.h5.model.request.CreateScaleEvaluationRecordRequest;
import com.fushuhealth.recovery.h5.service.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class ScaleEvaluationRecordServiceImpl extends ServiceImpl<ScaleEvaluationRecordDao, ScaleEvaluationRecord> implements ScaleEvaluationRecordService {

    private final static Logger log = LoggerFactory.getLogger(ScaleEvaluationRecordServiceImpl.class);

    @Autowired
    private ScaleEvaluationRecordDao scaleEvaluationRecordDao;

    @Autowired
    private ScaleTableService scaleTableService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private ISysDeptService deptService;

//    @Autowired
//    private WxMpService wxMpService;

    @Autowired
    private IChildrenService childrenService;

    @Autowired
    private ScaleOrderService scaleOrderService;

    @Autowired
    private IRisksService iRisksService;

//    @Autowired
//    private XiaoeService xiaoeService;

//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private OrganizationConfigService organizationConfigService;
//
//    @Autowired
//    private OrganizationService organizationService;
//
//    @Autowired
//    private ScaleEvaluateLogService scaleEvaluateLogService;

    private static String resultFilePath;

    @Value("${scale-table.result.cerebralPalsyPath:}")
    public void setResultFilePath(String filePath) {
        resultFilePath = filePath;
    }

    private static String abnormalMethodFilePath;

    @Value("${scale-table.abnormalMethodFilePath:}")
    public void setAbnormalMethodFilePath(String filePath) {
        abnormalMethodFilePath = filePath;
    }

    private static String trainMethodFilePath;

    @Value("${scale-table.trainMethodFilePath:}")
    public void setTrainMethodFilePath(String filePath) {
        trainMethodFilePath = filePath;
    }

    private static String abnormalVideoFilePath;

    @Value("${scale-table.actionVideoFilePath:}")
    public void setAbnormalVideoFilePath(String filePath) {
        abnormalVideoFilePath = filePath;
    }

    private static Map<String, CerebralPalsyScaleEvaluateResult> suggestMap;

    private static Map<String, AbnormalItermInterventionMethodVo> abnormalMethodMap;

    private static Map<String, AbnormalItermInterventionMethodVo> trainMethodMap;

    private static Map<String, AbnormalActionVideo> abnormalActionVideoMap;

    //    private final static String smsPhone = "18280274169";
    private final static List<String> smsPhone = Arrays.asList(new String[]{"18611517372", "15810179400"});

//    @Autowired
//    private SmsSender smsSender;

//    @Value(value = "${sms.code.submit-evaluation-template-id:''}")
//    private String templateId;
//
//    @Value("${file.domain}")
//    private String fileDomain;

    @PostConstruct
    public void init() {
        suggestMap = new HashMap<>();
        abnormalMethodMap = new HashMap<>();
        trainMethodMap = new HashMap<>();
        abnormalActionVideoMap = new HashMap<>();

        if (StringUtils.isNotBlank(resultFilePath)) {
            try {
                log.info("resultFilePath : {}", resultFilePath);
                File file = new File(resultFilePath);
                final String content = FileUtils.readFileToString(file, "UTF-8");
                List<CerebralPalsyScaleEvaluateResult> list = JSON.parseArray(content, CerebralPalsyScaleEvaluateResult.class);
                for (CerebralPalsyScaleEvaluateResult result : list) {
                    suggestMap.put(result.getResult(), result);
                }
                log.info("结果文件 suggestMap 加载完成，共有{}条数据", suggestMap.size());
            } catch (Exception e) {
                log.error("加载结果文件失败", e);
            }
        }

        if (StringUtils.isNotBlank(abnormalMethodFilePath)) {
            try {
                log.info("abnormalMethodFilePath:{}", abnormalMethodFilePath);
                final String abnormalItermContent = FileUtils.readFileToString(new File(abnormalMethodFilePath), "UTF-8");
                List<AbnormalItermInterventionMethodVo> methodVos = JSON.parseArray(abnormalItermContent, AbnormalItermInterventionMethodVo.class);
                for (AbnormalItermInterventionMethodVo methodVo : methodVos) {
                    abnormalMethodMap.put(methodVo.getAbnormalIterm(), methodVo);
                }
                log.info("异常方法文件 abnormalMethodMap 加载完成，共有{}条数据", abnormalMethodMap.size());
            } catch (Exception e) {
                log.error("加载异常方法文件失败", e);
            }
        }

        if (StringUtils.isNotBlank(trainMethodFilePath)) {
            try {
                log.info("trainMethodFilePath:{}", trainMethodFilePath);
                final String trainContent = FileUtils.readFileToString(new File(trainMethodFilePath), "UTF-8");
                List<AbnormalItermInterventionMethodVo> trainMethodVos = JSON.parseArray(trainContent, AbnormalItermInterventionMethodVo.class);
                for (AbnormalItermInterventionMethodVo methodVo : trainMethodVos) {
                    trainMethodMap.put(methodVo.getAbnormalIterm(), methodVo);
                }
                log.info("训练方法文件 trainMethodMap 加载完成，共有{}条数据", trainMethodMap.size());
            } catch (Exception e) {
                log.error("加载训练方法文件失败", e);
            }
        }

        if (StringUtils.isNotBlank(abnormalVideoFilePath)) {
            try {
                log.info("abnormalVideoFilePath :{}", abnormalVideoFilePath);
                final String videoContent = FileUtils.readFileToString(new File(abnormalVideoFilePath), "UTF-8");
                List<AbnormalActionVideo> abnormalActionVideos = JSON.parseArray(videoContent, AbnormalActionVideo.class);
                for (AbnormalActionVideo abnormalActionVideo : abnormalActionVideos) {
                    abnormalActionVideoMap.put(abnormalActionVideo.getName(), abnormalActionVideo);
                }
                log.info("异常动作文件 abnormalActionVideoMap 加载完成，共有{}条数据", abnormalActionVideoMap.size());
            } catch (Exception e) {
                log.error("加载异常动作文件失败", e);
            }
        }

    }

    @Override
    @Transactional
    public long saveRecord(LoginVo loginVo, CreateScaleEvaluationRecordRequest request) {

        log.info("save request : {}", JSON.toJSONString(request));

        ScaleTable scaleTable = scaleTableService.getScaleTable(request.getScaleTableCode());
        if (scaleTable == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }

        boolean isGms = false;
        if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS.getCode()
                || scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()
                || scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            isGms = true;
            if (request.getOrderId() != null && request.getOrderId() > 0) {
                ScaleOrderVo scaleOrderVo = scaleOrderService.getScaleOrderVo(request.getOrderId());
                if (scaleOrderVo.getStatus() != ScaleOrderStatus.PAID.getCode()) {
                    throw new OldServiceException(ResultCode.SCALE_ORDER_ERROR);
                }
            } else {
                throw new OldServiceException(ResultCode.SCALE_ORDER_ERROR);
            }
        }

//        LambdaQueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<ScaleEvaluationRecord>().lambda();
//        wrapper.eq(ScaleEvaluationRecord::getUserId, loginVo.getId())
//                .eq(ScaleEvaluationRecord::getScaleTableCode, request.getScaleTableCode())
//                .eq(ScaleEvaluationRecord::getProgressStatus, ScaleStatus.NOT_EVALUATE.getStatus())
//                .eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
//                .eq(ScaleEvaluationRecord::getDataStatus, DataStatus.WAIT_UPLOAD.getStatus())
//                .orderByDesc(ScaleEvaluationRecord::getId).last("limit 1");
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectOne(wrapper);
//        if (record == null) {
//            record = new ScaleEvaluationRecord();
//        }
        ScaleEvaluationRecord record = new ScaleEvaluationRecord();
        record.setBirthday(request.getBirthday());
        record.setConclusion("");
        record.setCreated(DateUtil.getCurrentTimeStamp());
        record.setGender(request.getGender());
        record.setName(request.getName());
        record.setProgressStatus(ScaleStatus.NOT_EVALUATE.getStatus());
        record.setResultFileId(0l);
        record.setScaleTableCode(request.getScaleTableCode());
        record.setScaleTableName("");
        record.setResult("");
        record.setStatus(BaseStatus.NORMAL.getStatus());
        record.setUpdated(DateUtil.getCurrentTimeStamp());
        record.setUserId(loginVo.getId());
        record.setOpenId(loginVo.getOpenId());
        record.setType(scaleTable.getType());
        record.setDoctorId(0l);
        record.setOrganizationId(loginVo.getOrganizationId());
        record.setChildrenId(request.getChildrenId() == null ? 0 : request.getChildrenId());
        record.setCategory(request.getCategory());
        record.setDataStatus(DataStatus.UPLOADED.getStatus());
        record.setDataUploadSource(DataUploadSource.MINI_APP.getCode());

        List<QuestionDto> questions = scaleTable.getQuestions();

        List<AnswerRequest> answers = request.getAnswers();
        List<AnswerWithRemarkDto> answerWithRemarkDtos = new ArrayList<>();
        int totalScore = 0;
        ArrayList<String> abnormalItems = new ArrayList<>();

        for (AnswerRequest answer : answers) {

            QuestionDto questionDto = getQuestionDto(questions, answer.getQuestionSn());

            AnswerWithRemarkDto answerWithRemarkDto = new AnswerWithRemarkDto();
            answerWithRemarkDto.setOptionSn(StringUtils.join(answer.getAnswerSn().toArray(), ","));
            answerWithRemarkDto.setQuestionSn(answer.getQuestionSn());
            answerWithRemarkDto.setRemark(answer.getRemark());

            List<AttachmentRequest> attachments = answer.getAttachments();
            ArrayList<AttachmentDto> attachmentDtos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(attachments)) {
                for (AttachmentRequest attachment : attachments) {
                    attachmentDtos.add(new AttachmentDto(Long.parseLong(attachment.getServerId()), 0L, "", ""));
                }
            }
            answerWithRemarkDto.setAttachmentDtos(attachmentDtos);

            if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()
                    || scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {

                List<Integer> answerSns = answer.getAnswerSn();
                for (Integer answerSn : answerSns) {
                    Option option = getOption(questionDto.getOptions(), answerSn);
                    if (option.getScore() > 0) {
                        answerWithRemarkDto.setDoctorScore(option.getScore());
                        totalScore += option.getScore();
                        QuestionSubjectEnum questionSubject = QuestionSubjectEnum.getQuestionSubject(questionDto.getSubject());
                        abnormalItems.add(questionSubject.getDesc() + "-" + questionDto.getName());
                    }
                }

            }
            answerWithRemarkDtos.add(answerWithRemarkDto);
        }

        record.setDoctorScore(totalScore);
        record.setUserScore(totalScore);
        record.setAnswerWithRemark(JSON.toJSONString(answerWithRemarkDtos));

        Children children = childrenService.getChildrenById(request.getChildrenId());
        List<String> childrenRisks = Arrays.asList(iRisksService.RisksExChanged(request.getChildrenId(),RiskType.CHILD_RISK.getType()).split(","));
        String result = "";

        if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
            CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = new CerebralPalsyScaleEvaluateResult();
            cerebralPalsyScaleEvaluateResult.setAbnormalIterm(abnormalItems);
            cerebralPalsyScaleEvaluateResult.setHighRisk(childrenRisks);
            cerebralPalsyScaleEvaluateResult.setRemark("");
            cerebralPalsyScaleEvaluateResult.setResult(calScaleRecordResult(childrenRisks, abnormalItems));
            result = JSON.toJSONString(cerebralPalsyScaleEvaluateResult);

        } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
            GMsAndCerebralPalsyResult gMsAndCerebralPalsyResult = new GMsAndCerebralPalsyResult();
            CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = new CerebralPalsyScaleEvaluateResult();
            cerebralPalsyScaleEvaluateResult.setAbnormalIterm(abnormalItems);
            cerebralPalsyScaleEvaluateResult.setHighRisk(childrenRisks);
            cerebralPalsyScaleEvaluateResult.setRemark("");
            cerebralPalsyScaleEvaluateResult.setResult(calScaleRecordResult(childrenRisks, abnormalItems));

            gMsAndCerebralPalsyResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);

            result = JSON.toJSONString(gMsAndCerebralPalsyResult);
        } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
            LeiBoCerebralPalsySelfTestResult cerebralPalsyScaleEvaluateResult = new LeiBoCerebralPalsySelfTestResult();
            cerebralPalsyScaleEvaluateResult.setHint("以上建议是基于此次婴幼儿神经运动发育风险家庭自测影像，经过计算机自动测评，专业人员审核视频得出。由于家庭自测影像的拍摄角度、清晰度、光线、拍摄质量会存在很大差异，以及拍摄人员的专业程度和文化背景不同，计算机自动识别判断会有学习遗漏和判断差异，人工审核视频也不能同面诊相比。因此，此报告只对此次影像和家长填写的问卷负责。不能作为任何医学诊断、评估、治疗的法律依据。若有高危因素或存在异常姿势和运动，请及时预约线下服务，通过专业机构明确诊断。");
            cerebralPalsyScaleEvaluateResult.setPositionAndSportAbnormal(getDefaultPositionAndSportIterms());
            result = JSON.toJSONString(cerebralPalsyScaleEvaluateResult);

        } else if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            LeiBoAllBodyTestResult leiBoAllBodyTestResult = new LeiBoAllBodyTestResult();
            LeiBoCerebralPalsySelfTestResult cerebralPalsyScaleEvaluateResult = new LeiBoCerebralPalsySelfTestResult();
            cerebralPalsyScaleEvaluateResult.setPositionAndSportAbnormal(getDefaultPositionAndSportIterms());
            cerebralPalsyScaleEvaluateResult.setHint("以上建议是基于此次婴幼儿神经运动发育风险家庭自测影像和GMs影像，经过计算机自动测评，专业人员审核视频得出。由于家庭自测影像和GMs影像的拍摄角度、清晰度、光线、拍摄质量会存在很大差异，以及拍摄人员的专业程度和文化背景不同，计算机自动识别判断会有学习遗漏和判断差异，人工审核视频也不能同面诊相比。因此，此报告只对此次影像和家长填写的问卷负责。请及时预约线下服务，通过专业机构明确诊断。");
            leiBoAllBodyTestResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);

            result = JSON.toJSONString(leiBoAllBodyTestResult);
        }

        record.setResult(result);
        //add 增加预约信息
        record.setReserveId(0l);
        record.setReserveType(WorkScheduleType.AI_EVALUATE.getType());
        record.setOrderId(request.getOrderId());

        scaleEvaluationRecordDao.insert(record);

        //修改订单状态
        scaleOrderService.useScaleOrder(request.getOrderId(), record.getId(), request.getChildrenId());


        try {
//            Organization organization = organizationService.getOrganization(loginVo.getOrganizationId());
            String organization = deptService.selectDeptNameById(SecurityUtils.getLoginUser().getDeptId());
            if (organization != null) {
                //发送通知短信
                HashMap<String, String> map = new HashMap<>();
                map.put("phone", loginVo.getPhone().substring(7, 11));
                map.put("time", DateUtil.getYMDHMSDate(record.getCreated()));
                map.put("organization", organization);
                map.put("name", scaleTable.getName());
                for (String s : smsPhone) {
                    //todo:接入sms
//                    smsSender.send(templateId, s, map);
                }
            }
        } catch (Exception e) {
            log.error("发送通知短信失败:{}", e);
        }

        log.info("提交异步保存附件接口");
//        todo:异步保存提交接口
//        SaveScaleAttachmentAsyncTask task = new SaveScaleAttachmentAsyncTask(record.getId(), answers);
//        TaskExecutor.getExecutor().submit(task);
        return record.getId();
    }

    @Override
    public long saveRecordWithOutAnswer(LoginVo loginVo, CreateScaleEvaluationRecordRequest request) {
        log.info("save request : {}", JSON.toJSONString(request));

        ScaleTable scaleTable = scaleTableService.getScaleTable(request.getScaleTableCode());
        if (scaleTable == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }
        boolean isGms = false;
        if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            if (request.getOrderId() != null && request.getOrderId() > 0) {
                ScaleOrderVo scaleOrderVo = scaleOrderService.getScaleOrderVo(request.getOrderId());
                if (scaleOrderVo.getStatus() != ScaleOrderStatus.PAID.getCode()) {
                    throw new OldServiceException(ResultCode.SCALE_ORDER_ERROR);
                }
            } else {
                throw new OldServiceException(ResultCode.SCALE_ORDER_ERROR);
            }
        }

//        LambdaQueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<ScaleEvaluationRecord>().lambda();
//        wrapper.eq(ScaleEvaluationRecord::getUserId, loginVo.getId())
//                .eq(ScaleEvaluationRecord::getScaleTableCode, request.getScaleTableCode())
//                .eq(ScaleEvaluationRecord::getProgressStatus, ScaleStatus.NOT_EVALUATE.getStatus())
//                .eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
//                .eq(ScaleEvaluationRecord::getDataStatus, DataStatus.WAIT_UPLOAD.getStatus())
//                .orderByDesc(ScaleEvaluationRecord::getId).last("limit 1");
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectOne(wrapper);
//        if (record == null) {
//            record = new ScaleEvaluationRecord();
//        }
        ScaleEvaluationRecord record = new ScaleEvaluationRecord();
        record.setBirthday(request.getBirthday());
        record.setConclusion("");
        record.setCreated(DateUtil.getCurrentTimeStamp());
        record.setGender(request.getGender());
        record.setName(request.getName());
        record.setProgressStatus(ScaleStatus.NOT_EVALUATE.getStatus());
        record.setResultFileId(0l);
        record.setScaleTableCode(request.getScaleTableCode());
        record.setScaleTableName("");
        record.setResult("");
        record.setStatus(BaseStatus.NORMAL.getStatus());
        record.setUpdated(DateUtil.getCurrentTimeStamp());
        record.setUserId(loginVo.getId());
        record.setOpenId(loginVo.getOpenId());
        record.setType(scaleTable.getType());
        record.setDoctorId(0l);
        record.setOrganizationId(loginVo.getOrganizationId());
        record.setChildrenId(request.getChildrenId() == null ? 0 : request.getChildrenId());
        record.setCategory(request.getCategory());
        record.setDataStatus(DataStatus.WAIT_UPLOAD.getStatus());
        record.setDataUploadSource(DataUploadSource.MECHANISM.getCode());
        record.setOrderId(request.getOrderId());

        List<AnswerWithRemarkDto> answerWithRemarkDtos = new ArrayList<>();
        int totalScore = 0;

        record.setDoctorScore(totalScore);
        record.setUserScore(totalScore);
        record.setAnswerWithRemark(JSON.toJSONString(answerWithRemarkDtos));

        String result = "";
        if (scaleTable.getCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            LeiBoAllBodyTestResult leiBoAllBodyTestResult = new LeiBoAllBodyTestResult();
            LeiBoCerebralPalsySelfTestResult cerebralPalsyScaleEvaluateResult = new LeiBoCerebralPalsySelfTestResult();
            cerebralPalsyScaleEvaluateResult.setPositionAndSportAbnormal(getDefaultPositionAndSportIterms());
            cerebralPalsyScaleEvaluateResult.setHint("以上建议是基于此次婴幼儿神经运动发育风险家庭自测影像和GMs影像，经过计算机自动测评，专业人员审核视频得出。由于家庭自测影像和GMs影像的拍摄角度、清晰度、光线、拍摄质量会存在很大差异，以及拍摄人员的专业程度和文化背景不同，计算机自动识别判断会有学习遗漏和判断差异，人工审核视频也不能同面诊相比。因此，此报告只对此次影像和家长填写的问卷负责。请及时预约线下服务，通过专业机构明确诊断。");
            leiBoAllBodyTestResult.setCerebralPalsyResult(cerebralPalsyScaleEvaluateResult);

            result = JSON.toJSONString(leiBoAllBodyTestResult);
        }

        record.setResult(result);
        //add 增加预约信息
        record.setReserveId(0l);
        record.setReserveType(WorkScheduleType.AI_EVALUATE.getType());
        scaleEvaluationRecordDao.insert(record);

        //修改订单状态
        scaleOrderService.useScaleOrder(request.getOrderId(), record.getId(), request.getChildrenId());

//        try {
//            Organization organization = organizationService.getOrganization(loginVo.getOrganizationId());
//            if (organization != null) {
//                //发送通知短信
//                HashMap<String, String> map = new HashMap<>();
//                map.put("phone", loginVo.getPhone().substring(7, 11));
//                map.put("time", DateUtil.getYMDHMSDate(record.getCreated()));
//                map.put("organization", organization.getName());
//                map.put("name", scaleTable.getName());
//                for (String s : smsPhone) {
//                    smsSender.send(templateId, s, map);
//                }
//            }
//        } catch (Exception e) {
//            log.error("发送通知短信失败:{}", e);
//        }
        return record.getId();
    }

    private List<PositionAndSportIterm> getDefaultPositionAndSportIterms() {
        ArrayList<PositionAndSportIterm> positionAndSportIterms = new ArrayList<>();
        ScaleTable scaleTable = scaleTableService.getScaleTable(ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode());
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

//    @Override
//    public ListScaleEvaluationRecordResponse listRecord(int pageNo, int pageSize, LoginVo user, CategoryType categoryType) {
//        Page<ScaleEvaluationRecord> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<>();
//        wrapper.in("reserve_type", Arrays.asList(new Byte[]{WorkScheduleType.AI_EVALUATE.getType(), WorkScheduleType.CLINIC_EVALUATE.getType(), WorkScheduleType.LEARNING_DISABILITY.getType()}));
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.eq("user_id", user.getId());
//        if (categoryType != CategoryType.ALL) {
//            wrapper.eq("category", categoryType.getType());
//        }
//        wrapper.orderByDesc("created");
//
//        page = scaleEvaluationRecordDao.selectPage(page, wrapper);
//        List<ScaleEvaluationRecord> records = page.getRecords();
//
//        List<ScaleEvaluationRecordListVo> list = records.stream().map(record -> convertToScaleEvaluationRecordListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListScaleEvaluationRecordResponse(pageVo, list);
//    }

//    @Override
//    public ScaleTableRecordVo getRecordResult(long recordId) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(recordId);
//        if (scaleEvaluationRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ScaleTable scaleTable = scaleTableService.getScaleTable(scaleEvaluationRecord.getScaleTableCode());
//
//        ScaleTableConstant.ScaleTableClassification scaleTableClassification = ScaleTableConstant.ScaleTableClassification.getScaleTableClassification(scaleTable.getClassification());
//        if (scaleTableClassification == ScaleTableConstant.ScaleTableClassification.DEVELOPMENT_MILESTONES) {
//            return new DevelopmentMilestoneScaleTable(scaleTable, scaleEvaluationRecord).getRecord();
//        } else if (scaleTableClassification == ScaleTableConstant.ScaleTableClassification.AUTISM) {
//            return new AutismScaleTable(scaleTable, scaleEvaluationRecord).getRecord();
//        } else if (scaleTableClassification == ScaleTableConstant.ScaleTableClassification.SENSORY_INTEGRATION) {
//            return new SensoryIntegrationScaleTable(scaleTable, scaleEvaluationRecord).getRecord();
//        } else if (scaleTableClassification == ScaleTableConstant.ScaleTableClassification.CEREBRAL_PALSY) {
//            return new CerebralPalsyScaleTable(scaleTable, scaleEvaluationRecord).getRecord();
//        } else if (scaleTableClassification == ScaleTableConstant.ScaleTableClassification.LEIBO_CEREBRAL_PALSY_GMS) {
//            return new LeiBoCerebralPalsyScaleTable(scaleTable, scaleEvaluationRecord).getRecord();
//        }
//        return null;
//    }
//
//    @Override
//    public ScaleTableRecordVo getRecordResultDetail(long recordId) {
//        ScaleEvaluationRecord scaleEvaluationRecord = scaleEvaluationRecordDao.selectById(recordId);
//        if (scaleEvaluationRecord == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ScaleTable scaleTable = scaleTableService.getScaleTable(scaleEvaluationRecord.getScaleTableCode());
//
//        return new ScaleTableDetail(scaleTable, scaleEvaluationRecord).getRecord();
//    }

//    @Override
//    public ScaleRecordVo getRecordReport(long recordId) {
//        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(recordId);
//        if (record == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
//        if (scaleTable == null) {
//            return null;
//        }
//
//        ScaleRecordVo vo = new ScaleRecordVo();
//        vo.setWeWorkQrCode(fileDomain + "youbaoshanyu/weWorkQrCode.png");
//        vo.setUserId(record.getUserId());
//
//        Children children = null;
//        if (record.getChildrenId() != 0) {
//            children = childrenService.getChildren(record.getChildrenId());
//            vo.setAge(DateUtil.getAge(children.getBirthday()));
//            vo.setBirthday(DateUtil.getYMD(children.getBirthday()));
//            vo.setName(children.getName());
//            vo.setGender(Gender.getGender(children.getGender()).getDesc());
//            vo.setBirthdayWeight(children.getBirthWeight());
//            vo.setGestationalWeek(children.getGestationalWeek() + "周" + children.getGestationalWeekDay() + "天");
//            vo.setMedicalCardNumber(children.getMedicalCardNumber());
//        } else {
//            vo.setAge(DateUtil.getAge(record.getBirthday()));
//            vo.setBirthday(DateUtil.getYMD(record.getBirthday()));
//            vo.setName(record.getName());
//            vo.setGender(Gender.getGender(record.getGender()).getDesc());
//            vo.setBirthdayWeight(0);
//            vo.setGestationalWeek("");
//            vo.setMedicalCardNumber("");
//        }
//
//        OrganizationConfig organizationConfig = organizationConfigService.getByAppIdAndChannelAndOrgId(AuthContext.getAppId(), AuthContext.getChannel(), AuthContext.getOrgId());
//        if (organizationConfig != null && organizationConfig.getAskDoctor() == 1) {
//            vo.setAskDoctor(true);
//        } else {
//            vo.setAskDoctor(false);
//        }
//        vo.setCreated(DateUtil.getYMDHMSDate(record.getCreated()));
//        vo.setDoctorScore(record.getDoctorScore());
//        vo.setId(record.getId());
//        vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
//        vo.setProgressStatusCode(record.getProgressStatus());
//        vo.setScaleTableName(scaleTable.getName());
//        vo.setScaleTableCode(scaleTable.getCode());
//        vo.setUserScore(record.getUserScore());
//        vo.setConclusion(record.getConclusion());
//        vo.setEvaluateDate(DateUtil.getYMDHMDate(record.getUpdated()));
//
//        //插入结果
//        if (record.getProgressStatus() == ScaleStatus.EVALUATED.getStatus()) {
//
//            DoctorVo doctorVo = doctorService.getDoctorVo(record.getDoctorId());
//            vo.setDoctorName(doctorVo.getName());
//
//            //审核者
//            String reviewDoctorName = "";
//            List<ScaleEvaluateLogListVo> scaleEvaluateLogListVos = scaleEvaluateLogService.listScaleEvaluateLogByScaleRecordId(record.getId());
//            for (ScaleEvaluateLogListVo scaleEvaluateLogListVo : scaleEvaluateLogListVos) {
//                if (scaleEvaluateLogListVo.getStatusByte() == ScaleStatus.REVIEWED_WAIT_SEND.getStatus()) {
//                    reviewDoctorName = scaleEvaluateLogListVo.getUserName();
//                }
//            }
//            vo.setReviewDoctorName(reviewDoctorName);
//
//
//            String result = record.getResult();
//            if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
//                CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = JSON.parseObject(result, CerebralPalsyScaleEvaluateResult.class);
//                if (cerebralPalsyScaleEvaluateResult != null) {
//                    cerebralPalsyScaleEvaluateResult.setRemark(getRemark(cerebralPalsyScaleEvaluateResult.getResult()));
//                    cerebralPalsyScaleEvaluateResult.setSuggest(getSuggestVo(cerebralPalsyScaleEvaluateResult.getResult()));
//                    addProduct(cerebralPalsyScaleEvaluateResult);
//                }
//                vo.setScaleResult(cerebralPalsyScaleEvaluateResult);
//            } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS.getCode()) {
//                GMsScaleEvaluationResult gMsScaleEvaluationResult = JSON.parseObject(result, GMsScaleEvaluationResult.class);
//                vo.setScaleResult(gMsScaleEvaluationResult);
//            } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
//                GMsAndCerebralPalsyResult gMsAndCerebralPalsyResult = JSON.parseObject(result, GMsAndCerebralPalsyResult.class);
//                CerebralPalsyScaleEvaluateResult cerebralPalsyResult = gMsAndCerebralPalsyResult.getCerebralPalsyResult();
//                if (cerebralPalsyResult != null) {
//                    cerebralPalsyResult.setRemark(getRemark(cerebralPalsyResult.getResult()));
//                    cerebralPalsyResult.setSuggest(getSuggestVo(cerebralPalsyResult.getResult()));
//                    addProduct(cerebralPalsyResult);
//                }
//                vo.setScaleResult(gMsAndCerebralPalsyResult);
//            } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
//                LeiBoCerebralPalsySelfTestResult leiBoCerebralPalsySelfTestResult = JSON.parseObject(result, LeiBoCerebralPalsySelfTestResult.class);
//                if (leiBoCerebralPalsySelfTestResult != null) {
//                    if (children != null) {
//                        String motherRisks = children.getMotherRisks();
//                        List<String> risks = new ArrayList<>();
//                        if (StringUtils.isNotBlank(motherRisks)) {
//                            risks.addAll(Arrays.asList(motherRisks.split(",")));
//                        }
//                        String childRisks = children.getChildRisks();
//                        if (StringUtils.isNotBlank(childRisks)) {
//                            risks.addAll(Arrays.asList(childRisks.split(",")));
//                        }
//                        leiBoCerebralPalsySelfTestResult.setHighRisk(risks);
//                        leiBoCerebralPalsySelfTestResult.setHaveHighRisk(!CollectionUtils.isEmpty(risks));
//
//                        List<PositionAndSportIterm> positionAndSportAbnormal = leiBoCerebralPalsySelfTestResult.getPositionAndSportAbnormal();
//                        positionAndSportAbnormal = positionAndSportAbnormal.stream().sorted(comparing(PositionAndSportIterm::getOptionSn).reversed()).collect(Collectors.toList());
//                        leiBoCerebralPalsySelfTestResult.setPositionAndSportAbnormal(positionAndSportAbnormal);
//
//                        //计算得分
//                        leiBoCerebralPalsySelfTestResult.setCerebralPalsyScore(positionAndSportAbnormal.stream().mapToInt(PositionAndSportIterm::getStatus).sum());
//                        //计算异常项
//                        ArrayList<String> abnormalIterm = new ArrayList<>();
//                        if (positionAndSportAbnormal != null) {
//                            for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
//                                if (iterm.getStatus() > 0) {
//                                    abnormalIterm.add(iterm.getName());
//                                }
//                            }
//                        }
//                        leiBoCerebralPalsySelfTestResult.setAbnormalIterm(abnormalIterm);
//                        leiBoCerebralPalsySelfTestResult.setHaveAbnormalIterm(!CollectionUtils.isEmpty(abnormalIterm));
//
//                        List<SuggestVo> suggests = new ArrayList<>();
//                        boolean useLeibo = false;
//                        if (leiBoCerebralPalsySelfTestResult.getUseLeiboSuggest() == null) {
//                            useLeibo = true;
//                        } else {
//                            useLeibo = leiBoCerebralPalsySelfTestResult.getUseLeiboSuggest();
//                        }
//
//                        if (useLeibo) {
//                            suggests.addAll(getSuggestVo(calScaleRecordResult(risks, abnormalIterm)));
//                        }
//                        if (CollectionUtils.isNotEmpty(leiBoCerebralPalsySelfTestResult.getSuggests())) {
//                            for (String suggest : leiBoCerebralPalsySelfTestResult.getSuggests()) {
//                                suggests.add(new SuggestVo(suggest, null));
//                            }
//                        }
//                        leiBoCerebralPalsySelfTestResult.setSuggest(suggests);
//
//                        leiBoCerebralPalsySelfTestResult.setShowVideo(true);
//                        addVideo(leiBoCerebralPalsySelfTestResult, record, DateUtil.getDateTimeYMD(vo.getBirthday()));
//                    }
//                    vo.setConclusion(leiBoCerebralPalsySelfTestResult.getRemark());
//                    vo.setScaleResult(leiBoCerebralPalsySelfTestResult);
//                } else {
//                    vo.setScaleResult(null);
//                }
//            } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
//                LeiBoAllBodyTestResult leiBoAllBodyTestResult = JSON.parseObject(result, LeiBoAllBodyTestResult.class);
//
//                //1：正常，2：异常，3：无法评估
//                Byte developmentRisk = (byte)1;
//                //处理脑瘫
//                LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
//                if (cerebralPalsyResult != null) {
//                    if (children != null) {
//                        String motherRisks = children.getMotherRisks();
//                        List<String> risks = new ArrayList<>();
//                        if (StringUtils.isNotBlank(motherRisks)) {
//                            risks.addAll(Arrays.asList(motherRisks.split(",")));
//                        }
//                        String childRisks = children.getChildRisks();
//                        if (StringUtils.isNotBlank(childRisks)) {
//                            risks.addAll(Arrays.asList(childRisks.split(",")));
//                        }
//                        cerebralPalsyResult.setHighRisk(risks);
//                        cerebralPalsyResult.setHaveHighRisk(!CollectionUtils.isEmpty(risks));
//
//                        List<PositionAndSportIterm> positionAndSportAbnormal = cerebralPalsyResult.getPositionAndSportAbnormal();
//                        positionAndSportAbnormal = positionAndSportAbnormal.stream().sorted(comparing(PositionAndSportIterm::getOptionSn).reversed()).collect(Collectors.toList());
//                        cerebralPalsyResult.setPositionAndSportAbnormal(positionAndSportAbnormal);
//
//                        //计算得分
//                        cerebralPalsyResult.setCerebralPalsyScore(positionAndSportAbnormal.stream().mapToInt(PositionAndSportIterm::getStatus).sum());
//                        //计算异常项
//                        ArrayList<String> abnormalIterm = new ArrayList<>();
//                        if (positionAndSportAbnormal != null) {
//                            for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
//                                if (iterm.getStatus() > 0) {
//                                    abnormalIterm.add(iterm.getName());
//                                }
//                            }
//                        }
//                        cerebralPalsyResult.setAbnormalIterm(abnormalIterm);
//                        cerebralPalsyResult.setHaveAbnormalIterm(!CollectionUtils.isEmpty(abnormalIterm));
////                        if (cerebralPalsyResult.getHaveAbnormalIterm()) {
////                            developmentRisk = (byte)2;
////                        }
//                        //处理建议
//                        List<SuggestVo> suggests = new ArrayList<>();
//                        boolean useLeibo = false;
//                        if (cerebralPalsyResult.getUseLeiboSuggest() == null) {
//                            useLeibo = true;
//                        } else {
//                            useLeibo = cerebralPalsyResult.getUseLeiboSuggest();
//                        }
//
//                        if (useLeibo) {
//                            suggests.addAll(getSuggestVo(calScaleRecordResult(risks, abnormalIterm)));
//                        }
//                        if (CollectionUtils.isNotEmpty(cerebralPalsyResult.getSuggests())) {
//                            for (String suggest : cerebralPalsyResult.getSuggests()) {
//                                suggests.add(new SuggestVo(suggest, null));
//                            }
//                        }
//
//                        List<SuggestVo> suggestVos = getSuggestVo(record, leiBoAllBodyTestResult, DateUtil.getDateTimeYMD(vo.getBirthday()));
//
//                        int index = suggestVos.size() / 2 + 1;
//                        for (SuggestVo suggest : suggests) {
//                            suggestVos.add(new SuggestVo("建议" + index + ":", null));
//                            suggestVos.add(suggest);
//                        }
//                        cerebralPalsyResult.setSuggest(suggestVos);
//
//                        if (organizationConfig != null && organizationConfig.getAskDoctor() == 1) {
//                            cerebralPalsyResult.setShowVideo(true);
//                        } else {
//                            cerebralPalsyResult.setShowVideo(false);
//                        }
//
//                        log.info("处理视频");
//                        addVideo(cerebralPalsyResult, record, DateUtil.getDateTimeYMD(vo.getBirthday()));
//                    }
//                    vo.setConclusion(cerebralPalsyResult.getRemark());
//                }
//
//                //处理 gms
//                String gmsCopyWriting = "您的宝宝本次测评GMs正常";
//                GMsScaleEvaluationResult gmsResult = leiBoAllBodyTestResult.getGmsResult();
////                List<GMsResultIterm> itermList = gmsResult.getResult();
////                if (CollectionUtils.isNotEmpty(itermList)) {
////                    GMsResultIterm gMsResultIterm = itermList.get(0);
////                    String content = gMsResultIterm.getContent();
////                    if (!content.equals("正常（N）") && !content.equals("正常 (F+)")) {
////                        gmsCopyWriting = "您的宝宝本次测评GMs出现异常：" + content;
////                    }
////                }
//                if (gmsResult != null) {
//                    String stageResult = gmsResult.getStageResult();
//                    String stage = gmsResult.getStage();
//                    if (StringUtils.isBlank(stage) || stage.contains("3")) {
//                        developmentRisk = (byte)3;
//                    } else {
//                        if (stageResult != null) {
//                            if (!stageResult.contains("正常（N）") && !stageResult.contains("正常 (F+)")) {
//                                gmsCopyWriting = "您的宝宝本次测评GMs出现异常：" + stageResult;
//                                developmentRisk = (byte)2;
//                            }
//                            gmsResult.setGmsCopyrighting(gmsCopyWriting);
//                        } else {
//                            List<GMsResultIterm> list = gmsResult.getResult();
//                            if (CollectionUtils.isNotEmpty(list)) {
//                                String content = list.get(0).getContent();
//                                gmsResult.setStageResult(content);
//                                if (!content.contains("正常（N）") && !content.contains("正常 (F+)")) {
//                                    gmsCopyWriting = "您的宝宝本次测评GMs出现异常：" + content;
//                                    developmentRisk = (byte)2;
//                                }
//                                gmsResult.setGmsCopyrighting(gmsCopyWriting);
//                            }
//                        }
//                    }
//                }
//                leiBoAllBodyTestResult.setDevelopmentRisk(developmentRisk);
//                vo.setScaleResult(leiBoAllBodyTestResult);
//            }
//        } else {
//            vo.setScaleResult(null);
//        }
//
//        if (record.getScaleTableCode() != ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode() &&
//                record.getScaleTableCode() != ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
//            List<QuestionDto> questions = scaleTable.getQuestions();
//            String answerWithRemark = record.getAnswerWithRemark();
//            List<AnswerWithRemarkDto> answers = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//
//            ArrayList<ScaleRecordQuestionVo> scaleRecordQuestionVos = new ArrayList<>(answers.size());
//            for (AnswerWithRemarkDto answer : answers) {
//                ScaleRecordQuestionVo scaleRecordQuestionVo = new ScaleRecordQuestionVo();
//                QuestionDto questionDto = getQuestionDto(questions, answer.getQuestionSn());
//
//                String[] options = answer.getOptionSn().split(",");
//
//                int userScore = 0;
//                ArrayList<String> contents = new ArrayList<>();
//                for (String optionSn : options) {
//                    Option option = getOption(questionDto.getOptions(), Integer.parseInt(optionSn));
//                    if (option != null) {
//                        userScore = userScore + option.getScore();
//                        contents.add(option.getContent());
//                    }
//                }
//
//                scaleRecordQuestionVo.setAnswer(contents);
//                scaleRecordQuestionVo.setDoctorScore(answer.getDoctorScore() == null ? 0 : answer.getDoctorScore());
//                scaleRecordQuestionVo.setId(questionDto.getSn());
//                scaleRecordQuestionVo.setName(questionDto.getName());
//                scaleRecordQuestionVo.setType(QuestionSubjectEnum.getQuestionSubject(questionDto.getSubject()).getDesc());
//                scaleRecordQuestionVo.setUserScore(userScore);
//
//                //设置附件
////            List<Long> attachmentFileIds = answer.getAttachmentFileIds();
////            if (CollectionUtils.isNotEmpty(attachmentFileIds)) {
////                ArrayList<AttachmentVo> attachmentVos = new ArrayList<>(attachmentFileIds.size());
////                List<Files> fileList = attachmentFileIds.stream().map(fileId -> fileService.getFile(fileId)).collect(Collectors.toList());
////                for (Files files : fileList) {
////                    AttachmentVo attachmentVo = new AttachmentVo();
////                    Byte fileType = files.getFileType();
////                    attachmentVo.setType(fileType);
////                    boolean origin = false;
////                    if (fileType == FileType.AUDIO.getCode()) {
////                        origin = true;
////                    }
////                    attachmentVo.setUrl(fileService.getFileUrl(files.getId(), origin));
////                    attachmentVos.add(attachmentVo);
////                }
////                scaleRecordQuestionVo.setAttachments(attachmentVos);
////            } else {
////                scaleRecordQuestionVo.setAttachments(new ArrayList<>());
////            }
//
//                scaleRecordQuestionVos.add(scaleRecordQuestionVo);
//            }
//            List<ScaleRecordQuestionVo> collect = scaleRecordQuestionVos.stream().sorted(Comparator.comparing(ScaleRecordQuestionVo::getId)).collect(Collectors.toList());
//            vo.setAnswers(collect);
//        }
//        return vo;
//    }

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

    private Map<String, Set<String>> getBigMoveVideo(ScaleEvaluationRecord record) {

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

    private List<String> getBigMovement(AnswerWithRemarkDto answer, QuestionDto questionDto) {
        if (answer == null || StringUtils.isBlank(answer.getOptionSn())) {
            return Collections.EMPTY_LIST;
        }
        ArrayList<String> strings = new ArrayList<>();
        String[] split = answer.getOptionSn().split(",");
        List<Option> options = questionDto.getOptions();
        for (String s : split) {
            int i = Integer.parseInt(s);
            for (Option option : options) {
                if (i == option.getSn()) {
                    strings.add(option.getContent());
                }
            }
        }
        return strings;
    }

    private Map<String, Set<String>> getAbnormalVideo(List<String> abnormalIterm, ScaleEvaluationRecord record) {

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

    private List<SuggestVo> getSuggestVo(ScaleEvaluationRecord record, LeiBoAllBodyTestResult leiBoAllBodyTestResult, long birthday) {
        GMsScaleEvaluationResult gmsResult = leiBoAllBodyTestResult.getGmsResult();
        String gms = "";
        if (gmsResult != null) {
            gms = gmsResult.getStageResult();
        }

        LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
        Integer videoStatus = cerebralPalsyResult.getVideoStatus();
        if (videoStatus == null) {
            videoStatus = 0;
        }
        String obviouslyBehind = cerebralPalsyResult.getObviouslyBehind();
        String tendencyBehind = cerebralPalsyResult.getTendencyBehind();

        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());

        String answerWithRemark = record.getAnswerWithRemark();
        List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//        List<String> bigMovement = getBigMovement(answerWithRemarkDtos.get(2), scaleTable.getQuestions().get(2));

        int month = (int)(record.getCreated() - birthday) / 60 / 60 / 24 / 30;
        List<String> bigMovement = getTrainMethod(record, month);

        List<PositionAndSportIterm> positionAndSportAbnormal = cerebralPalsyResult.getPositionAndSportAbnormal();

        List<String> existAbnormal = positionAndSportAbnormal.stream().
                filter(position -> position.getOptionSn() == 3).map(PositionAndSportIterm::getName)
                .collect(Collectors.toList());
        List<String> suspiciousAbnormal = positionAndSportAbnormal.stream()
                .filter(position -> position.getOptionSn() == 2).map(PositionAndSportIterm::getName)
                .collect(Collectors.toList());

        ArrayList<String> abnormalIterm = new ArrayList<>();
        if (positionAndSportAbnormal != null) {
            for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
                if (iterm.getStatus() > 0) {
                    abnormalIterm.add(iterm.getName());
                }
            }
        }
        Map<String, Set<String>> abnormalVideos = getAbnormalVideo(abnormalIterm, record);

        ArrayList<SuggestVo> suggestVos = new ArrayList<>();
        ReportType reportType = deptService.getReportType(SecurityUtils.getLoginUser().getDeptId());
        if (reportType == ReportType.SIMPLE) {
            int index = 1;
            suggestVos.add(new SuggestVo("建议" + index + ":", null));
            suggestVos.add(new SuggestVo("  " + getGmsSuggest(gms), null));
            index++;

            if (videoStatus == 1) {
                suggestVos.add(new SuggestVo("建议" + index + ":", null));
                suggestVos.add(new SuggestVo("  尊敬的家长，激发姿势视频，您此次拍摄的视频没能按照要求（示范视频）完成，本着科学负责的精神，请认真观看指导视频后按规定要求重新拍摄上传。给您带来的不便请谅解，谢谢配合。", null));
                index++;
            } else {
                if (CollectionUtils.isNotEmpty(existAbnormal)) {
                    StringBuilder sb = new StringBuilder("  此次姿势运动评估存在");
                    for (String s : existAbnormal) {
                        if (StringUtils.isNotBlank(s)) {
                            sb.append("【" + s + "】");
                        }
                    }
                    sb.append("明显的异常，建议针对性的");
                    for (String s : existAbnormal) {
                        if (StringUtils.isNotBlank(s)) {
                            sb.append("【" + s + "】");
                        }
                    }
                    sb.append("干预训练。");
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  " + sb.toString(), null));
                    index++;
                }
                if (CollectionUtils.isNotEmpty(suspiciousAbnormal)) {
                    StringBuilder sb = new StringBuilder("  此次姿势运动评估疑似出现");
                    for (String s : suspiciousAbnormal) {
                        if (StringUtils.isNotBlank(s)) {
                            sb.append("【" + s + "】");
                        }
                    }
                    sb.append("的异常，建议重点观察和定期复查。");
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  " + sb.toString(), null));
                    index++;
                }
                if (StringUtils.isNotBlank(obviouslyBehind)) {
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  视频中表现出来有姿势运动（" + obviouslyBehind + "）明显落后，建议进行早期干预。", null));
                    index++;
                }
                if (StringUtils.isNotBlank(tendencyBehind)) {
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  视频中表现出来有姿势运动（" + tendencyBehind + "）落后倾向，建议生活中增加针对性的训练。", null));
                    index++;
                }
                if (CollectionUtils.isNotEmpty(bigMovement)) {
                    StringBuilder sb = new StringBuilder("此次家长填写的粗大运动能力，");
                    for (String s : bigMovement) {
                        sb.append("【" + s + "】");
                    }
                    sb.append("有落后。建议进行");
                    for (String s : bigMovement) {
                        sb.append("【" + s + "】");
                    }
                    sb.append("功能促进训练。");
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  " + sb.toString(), null));
                    index++;
                }
            }
        } else if (reportType == ReportType.STANDARD){
            int index = 1;
            suggestVos.add(new SuggestVo("建议" + index + ":", null));
            suggestVos.add(new SuggestVo("  " + getGmsSuggest(gms), null));
            index++;

            if (videoStatus == 1) {
                suggestVos.add(new SuggestVo("建议" + index + ":", null));
                suggestVos.add(new SuggestVo("  尊敬的家长，激发姿势视频，您此次拍摄的视频没能按照要求（示范视频）完成，本着科学负责的精神，请认真观看指导视频后按规定要求重新拍摄上传。给您带来的不便请谅解，谢谢配合。", null));
                index++;
            } else {
                if (CollectionUtils.isNotEmpty(existAbnormal)) {
                    StringBuilder sb = new StringBuilder("  此次姿势运动评估存在");
                    for (String s : existAbnormal) {
                        if (StringUtils.isNotBlank(s)) {
                            sb.append("【" + s + "】");
                        }
                    }
                    sb.append("明显的异常，建议针对性的");
                    for (String s : existAbnormal) {
                        if (StringUtils.isNotBlank(s)) {
                            sb.append("【" + s + "】");
                        }
                    }
                    sb.append("干预训练。。系统提供了如下干预视频，请认真学习并按照要求在家进行干预训练。每天2-4次，每次30分钟。\n");
                    //根据 existAbnormal 对视频进行筛选
                    HashSet<String> videoNameSet = new HashSet<>();
                    for (String s : existAbnormal) {
                        Set<String> set = abnormalVideos.get(s);
                        if (set != null) {
                            videoNameSet.addAll(set);
                        }
                    }

                    List<String> videos = new ArrayList<>(videoNameSet);
                    for (int i = 0; i < videos.size(); i++) {
                        sb.append("  (");
                        sb.append(NumberUtil.int2chineseNum(i + 1));
                        sb.append(") ");
                        sb.append(videos.get(i));
                        sb.append("\n");
                    }
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  " + sb.toString(), null));
                    index++;
                }
                if (CollectionUtils.isNotEmpty(suspiciousAbnormal)) {
                    StringBuilder sb = new StringBuilder("  此次姿势运动评估疑似出现");
                    for (String s : suspiciousAbnormal) {
                        if (StringUtils.isNotBlank(s)) {
                            sb.append("【" + s + "】");
                        }
                    }
                    sb.append("的异常，建议重点观察和定期复查。");
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  " + sb.toString(), null));
                    index++;
                }
                if (StringUtils.isNotBlank(obviouslyBehind)) {
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  视频中表现出来有姿势运动（" + obviouslyBehind + "）明显落后，建议进行早期干预。", null));
                    index++;
                }
                if (StringUtils.isNotBlank(tendencyBehind)) {
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  视频中表现出来有姿势运动（" + tendencyBehind + "）落后倾向，建议生活中增加针对性的训练。", null));
                    index++;
                }
                if (CollectionUtils.isNotEmpty(bigMovement)) {
                    StringBuilder sb = new StringBuilder("此次家长填写的粗大运动能力，");
                    for (String s : bigMovement) {
                        sb.append("【" + s + "】");
                    }
                    sb.append("有落后。建议进行");
                    for (String s : bigMovement) {
                        sb.append("【" + s + "】");
                    }
                    sb.append("功能促进训练。系统提供了如下功能训练家庭干预视频，认真学习并按照序号顺序逐级练习。每天2-4次，每次30分钟。\n");
                    Map<String, Set<String>> bigMoveVideos = getBigMoveVideo(record);

                    Set<String> bigMoveVideoSet = new HashSet<>();
                    for (String s : bigMovement) {
                        Set<String> set = bigMoveVideos.get(s);
                        if (set != null) {
                            bigMoveVideoSet.addAll(set);
                        }
                    }

                    List<String> videos = new ArrayList<>(bigMoveVideoSet);
                    for (int i = 0; i < videos.size(); i++) {
                        sb.append("  (");
                        sb.append(NumberUtil.int2chineseNum(i + 1));
                        sb.append(") ");
                        sb.append(videos.get(i));
                        sb.append("\n");
                    }
                    suggestVos.add(new SuggestVo("建议" + index + ":", null));
                    suggestVos.add(new SuggestVo("  " + sb.toString(), null));
                    index++;
                }
            }
        } else {

        }
        return suggestVos;
    }

    private void addVideo(LeiBoCerebralPalsySelfTestResult result, ScaleEvaluationRecord record, long birthday) {
        ArrayList<VideoVo> videoVos = new ArrayList<>();
        //TODO
        //添加抚触操
        AbnormalActionVideo fuchuActionVideo = abnormalActionVideoMap.get("婴儿抚触统合操");
        log.info("婴儿抚触统合操共有 :{} 条数据", fuchuActionVideo.getVideos().size());
        if (fuchuActionVideo != null) {
            List<VideoDto> videos = fuchuActionVideo.getVideos();
            for (VideoDto video : videos) {
                VideoVo fuchuVideo = new VideoVo();
                fuchuVideo.setCoverUrl(fileService.getFileUrl(OldFileType.PICTURE, video.getCover(), true));
                fuchuVideo.setName(video.getName());
                fuchuVideo.setRemark(video.getName());
                fuchuVideo.setUrl(fileService.getFileUrl(OldFileType.VIDEO, video.getVideo(), false));
                videoVos.add(fuchuVideo);
            }

        }

        //增加异常项
        List<String> abnormalIterm = result.getAbnormalIterm();
        for (String s : abnormalIterm) {
            //获取每个异常项对应的视频
            AbnormalActionVideo abnormalActionVideo = abnormalActionVideoMap.get(s);
            log.info("异常项 {} 共有 :{} 条数据", s, abnormalActionVideo.getVideos().size());
            if (abnormalActionVideo != null) {
                List<VideoDto> videos = abnormalActionVideo.getVideos();
                for (VideoDto video : videos) {
                    VideoVo fuchuVideo = new VideoVo();
                    fuchuVideo.setCoverUrl(fileService.getFileUrl(OldFileType.PICTURE, video.getCover(), true));
                    fuchuVideo.setName(video.getName());
                    fuchuVideo.setRemark(video.getName());
                    fuchuVideo.setUrl(fileService.getFileUrl(OldFileType.VIDEO, video.getVideo(), false));
                    videoVos.add(fuchuVideo);
                }
            }
        }

        int month = (int)(record.getCreated() - birthday) / 60 / 60 / 24 / 30;
        //增加功能训练
        List<String> trainMethod = getTrainMethod(record, month);
        for (String s : trainMethod) {
            //获取每个异常项对应的视频
            AbnormalActionVideo abnormalActionVideo = abnormalActionVideoMap.get(s);
            log.info("功能训练 {} 共有 :{} 条数据", s, abnormalActionVideo.getVideos().size());
            if (abnormalActionVideo != null) {
                List<VideoDto> videos = abnormalActionVideo.getVideos();
                for (VideoDto video : videos) {
                    VideoVo fuchuVideo = new VideoVo();
                    fuchuVideo.setCoverUrl(fileService.getFileUrl(OldFileType.PICTURE, video.getCover(), true));
                    fuchuVideo.setName(video.getName());
                    fuchuVideo.setRemark(video.getName());
                    fuchuVideo.setUrl(fileService.getFileUrl(OldFileType.VIDEO, video.getVideo(), false));
                    videoVos.add(fuchuVideo);
                }
            }
        }
        ArrayList<VideoVo> collect = videoVos.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(comparing(VideoVo::getName))), ArrayList::new));
        result.setVideos(collect);
    }

    private SuggestVo getSuggestVo(List<SuggestVo> suggestVos, String suggest) {
        for (SuggestVo suggestVo : suggestVos) {
            if (suggestVo.getContent().equals(suggest)) {
                return suggestVo;
            }
        }
        return new SuggestVo(suggest, null);
    }

    private void addProduct(CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult) {
        List<String> abnormalIterm = cerebralPalsyScaleEvaluateResult.getAbnormalIterm();
        if (CollectionUtils.isNotEmpty(abnormalIterm)) {
            String resourceId = "v_62f0758be4b0c942648499c5";
            String productId = "p_62d775b1e4b0a51fef018eab";
            AbnormalItermInterventionMethodVo vo = abnormalMethodMap.get(abnormalIterm.get(0));
            if (vo != null) {
                List<GetGoodsParam> resources = vo.getResources();
                if (CollectionUtils.isNotEmpty(resources)) {
                    GetGoodsParam getGoodsParam = resources.get(0);
                    if (getGoodsParam != null) {
                        String[] ids = getGoodsParam.getIds();
                        if (ids.length > 0) {
                            resourceId = ids[0];
                        }
                    }
                }
            }
            cerebralPalsyScaleEvaluateResult.setResourceId(resourceId);
            cerebralPalsyScaleEvaluateResult.setProductId(productId);
        }
    }

    @Override
    public int countByUserIdAndScaleCodeAndChildrenIdAndTime(long userId, byte code, long childrenId, long time) {
        QueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("children_id", childrenId);
        wrapper.eq("scale_table_code", code);
        wrapper.ge("created", time);
        return Math.toIntExact(scaleEvaluationRecordDao.selectCount(wrapper));
    }

    @Override
    public List<AbnormalItermInterventionMethodVo> getAbnormalMethods(long recordId, String productId) {
        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(recordId);
        if (record == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }
        if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode()) {
            CerebralPalsyScaleEvaluateResult cerebralPalsyScaleEvaluateResult = JSON.parseObject(record.getResult(), CerebralPalsyScaleEvaluateResult.class);
            if (cerebralPalsyScaleEvaluateResult != null) {
                return getMethods(cerebralPalsyScaleEvaluateResult.getAbnormalIterm(), record);

            }
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.GMS_LEIBO.getCode()) {
            GMsAndCerebralPalsyResult gMsAndCerebralPalsyResult = JSON.parseObject(record.getResult(), GMsAndCerebralPalsyResult.class);
            CerebralPalsyScaleEvaluateResult cerebralPalsyResult = gMsAndCerebralPalsyResult.getCerebralPalsyResult();
            if (cerebralPalsyResult != null) {
                return getMethods(cerebralPalsyResult.getAbnormalIterm(), record);
            }
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_BODY_GMS.getCode()) {
            LeiBoAllBodyTestResult leiBoAllBodyTestResult = JSON.parseObject(record.getResult(), LeiBoAllBodyTestResult.class);
            LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = leiBoAllBodyTestResult.getCerebralPalsyResult();
            if (cerebralPalsyResult != null) {
                List<PositionAndSportIterm> positionAndSportAbnormal = cerebralPalsyResult.getPositionAndSportAbnormal();
                //计算异常项
                ArrayList<String> abnormalIterm = new ArrayList<>();
                if (positionAndSportAbnormal != null) {
                    for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
                        if (iterm.getStatus() > 0) {
                            abnormalIterm.add(iterm.getName());
                        }
                    }
                }
                List<AbnormalItermInterventionMethodVo> methods = new ArrayList<>();
                //默认是从异常项进来
                if (StringUtils.isBlank(productId) || "p_62d775b1e4b0a51fef018eab".equalsIgnoreCase(productId)) {
                    //增加异常项
                    methods.addAll(getMethods(abnormalIterm, record));
                    //增加抚触操
                    methods.addAll(getFuchuMethodVo(record));
                    //增加功能训练
                    methods.addAll(getTrainMethodVo(record));
                } else if ("p_63423bb0e4b0a51fef25b0a7".equalsIgnoreCase(productId)){
                    //从功能训练进来
                    //增加功能训练
                    methods.addAll(getTrainMethodVo(record));
                    //增加抚触操
                    methods.addAll(getFuchuMethodVo(record));
                    //增加异常项
                    methods.addAll(getMethods(abnormalIterm, record));
                }
                return methods;
            }
        } else if (record.getScaleTableCode() == ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_SELF.getCode()) {
            LeiBoCerebralPalsySelfTestResult cerebralPalsyResult = JSON.parseObject(record.getResult(), LeiBoCerebralPalsySelfTestResult.class);
            if (cerebralPalsyResult != null) {
                List<PositionAndSportIterm> positionAndSportAbnormal = cerebralPalsyResult.getPositionAndSportAbnormal();
                //计算异常项
                ArrayList<String> abnormalIterm = new ArrayList<>();
                if (positionAndSportAbnormal != null) {
                    for (PositionAndSportIterm iterm : positionAndSportAbnormal) {
                        if (iterm.getStatus() > 0) {
                            abnormalIterm.add(iterm.getName());
                        }
                    }
                }
                //要根据入口添加异常项，抚触操，功能训练
                List<AbnormalItermInterventionMethodVo> methods = new ArrayList<>();
                //默认是从异常项进来
                if (StringUtils.isBlank(productId) || "p_62d775b1e4b0a51fef018eab".equalsIgnoreCase(productId)) {
                    //增加异常项
                    methods.addAll(getMethods(abnormalIterm, record));
                    //增加抚触操
                    methods.addAll(getFuchuMethodVo(record));
                    //增加功能训练
                    methods.addAll(getTrainMethodVo(record));
                } else if ("p_63423bb0e4b0a51fef25b0a7".equalsIgnoreCase(productId)){
                    //从功能训练进来
                    //增加功能训练
                    methods.addAll(getTrainMethodVo(record));
                    //增加抚触操
                    methods.addAll(getFuchuMethodVo(record));
                    //增加异常项
                    methods.addAll(getMethods(abnormalIterm, record));
                }
                return methods;
            }
        }
        return new ArrayList<>();
    }

    private String getGmsExplain(String gmsResult) {
        String gmsExplain = "";
        switch (gmsResult) {
            case "正常（N）":
                gmsExplain = "自发性全身运动符合该年龄正常表现。即在足月至足月后6～9周龄内。正常扭动运动，其特征为小至中等幅度，速度缓慢至中等，运动轨迹在形式上呈现为椭圆体，给人留下扭动的印象。";
                break;
            case "单调性GMs(PR)":
                gmsExplain = "这代表孩子在扭动运动阶段表现出了轻度的异常。在各个连续性运动的顺序单调，不同身体部位的运动失去了复杂性。就好像我们看着宝宝的运动，总是简单的重复的几个动作，甚至我们可以预想到TA下一个动作是什么。";
                break;
            case "痉挛-同步性GMs (CS)":
                gmsExplain = "这代表孩子在扭动运动阶段表现出了比较明显的异常。它表现为运动僵硬，失去正常的流畅性，所有肢体和躯干肌肉几乎同时收缩和放松，比如双腿同时抬高并同时放下等。";
                break;
            case "混乱性GMs (CH)":
                gmsExplain = "所有肢体运动幅度大，顺序混乱，失去流畅性。动作突然,不连贯。发生在早产阶段、足月阶段和足月后早期。";
                break;
            case "正常 (F+)":
                gmsExplain = "自发性全身运动符合该年龄正常表现。即在矫正年龄足月后6周龄左右出现。正常不安运动，其特征为一种小幅度中速运动，遍布颈、躯干和四肢，发生在各个方向，运动加速度可变，在清醒婴儿中该运动持续存在(哭闹时除外)，不安运动出现的频度随年龄而发生改变。";
                break;
            case "不安运动缺乏（F-）":
                gmsExplain = "不安运动是一种小幅度、中速度的细微运动，在9-20周龄的宝宝身上会如星辰般闪烁在各个身体的部位上。如果没有这样的细微运动出现，便是不安运动缺乏了。通常不安运动缺乏的宝宝是仍然可以观察到其他运动的存在的，也就是说没有不安运动，不代表宝宝不会动。";
                break;
            case "异常性不安运动(AF)":
                gmsExplain = "异常的不安运动很少见，其预测价值较低。出现异常不安运动，宝宝可能会正常发育，但也可能发展为脑瘫。例如相关研究表明在早产儿中，出现不安运动可能会表现出吸吮不协调的情况；也可能表现出协调障碍和/或精细运动障碍等。";
                break;
        }
        return gmsExplain;
    }

    private String getGmsSuggest(String gmsResult) {
        String gmsSuggest = "";
        switch (gmsResult) {
            case "正常（N）":
                gmsSuggest = "正常（N）: 后续应遵循国家基本公共卫生服务规范，坚持在3岁以内定期进行发育评估，以排除发育落后的可能，并确保及早发现轻微偏离并采用预防性的干预措施，提高儿童的大运动、精细运动、语言、认知和社会活动等能力全面发展。可在超早期促进正常婴儿感觉、运动、认知及母婴依恋发育。";
                break;
            case "单调性GMs(PR)":
                gmsSuggest = "单调性（PR）: 家长应当配合好医院的指导给宝宝提供早期的家庭干预，并且在不安运动阶段按时随访。家长可学习一些新生儿和婴幼儿神经心理发育规律、亲子互动技巧、居家早期干预方法及婴儿护理基本技能等。宝宝在不安运动阶段的表现可能会转为正常，且比例不低哦。";
                break;
            case "痉挛-同步性GMs (CS)":
                gmsSuggest = "痉挛同步性GMs（CS）: 家长应当配合医院在指定时间前来随访，并尽量安排宝宝参与专业的康复治疗。宝宝在积极的参与康复治疗并及时随访的前提下，在家长和专业人员的共同努力下，还是可以向好的方向发展的，所以家长也切莫就此灰心哟！";
                break;
            case "混乱性GMs (CH)":
                gmsSuggest = "混乱型GMs（CH）: 混乱性GMs相当少见，常在数周后发展为痉挛-同步性GMs。家长应引起足够的重视，配合医院在指定时间前来随访，并尽量安排宝宝参与专业的康复治疗。宝宝在积极的参与康复治疗并及时随访的前提下，在家长和专业人员的共同努力下，还是可以向好的方向发展的，所以家长也切莫就此灰心哟！";
                break;
            case "正常 (F+)":
                gmsSuggest = "正常不安运动（F+）: 后续应遵循国家基本公共卫生服务规范，坚持在3岁以内定期进行发育评估，以排除发育落后的可能，并确保及早发现轻微偏离并采用预防性的干预措施，提高儿童的大运动、精细运动、语言、认知和社会活动等能力全面发展。可在超早期促进正常婴儿感觉、运动、认知及母婴依恋发育。";
                break;
            case "不安运动缺乏（F-）":
                gmsSuggest = "不安运动缺乏（F-）: 家长首先要调节好自身的心态，切莫被预测结果先打败了。要知道积极的康复治疗对孩子的成长有非常关键的影响。应当乐观的陪同宝宝参与专业的康复治疗，并且按时随访和参与后续评估以便于了解宝宝的近期发展，定制新的训练治疗方案，力求将宝宝因脑损伤而对日后生活造成的影响减到最低。";
                break;
            case "异常性不安运动(AF)":
                gmsSuggest = "异常不安运动 （AF）: 建议家长引起重视，持续动态跟踪和随访，并尽快到线下医疗机构进行全面评估、鉴别诊断。";
                break;
            case "儿童超龄":
                gmsSuggest = "尊敬的家长，全身运动质量评估只适合6月龄（矫正月龄）以下的婴儿。但您上传的视频可以进行姿势运动的发育风险评估。谢谢理解与配合。";
                break;
            case "视频拍摄不合格":
                gmsSuggest = "尊敬的家长，自发姿势视频，您此次拍摄的视频没能按照要求（示范视频）完成，本着科学负责的精神，请认真观看指导视频后按规定要求重新拍摄上传。给您带来的不便请谅解，谢谢配合。";
                break;
        }
        return gmsSuggest;
    }

    private String getAbnormalExplain(String s) {
        String explainContent = "";
        switch (s) {
            case "拇指内收发紧" :
                explainContent = "婴儿拇指内收发紧，又称拇指内扣，最轻的仅半握拳时拇指压在食指下，严重的拇指内收达掌心，此时称“皮层拇指征”。";
                break;
            case "紧张时头偏向一侧" :
                explainContent = "孩子紧张时，头和面部转向一侧或伴有头颈向一侧屈，有的孩子固定的总是转向左侧或右侧，有的孩子有时左、有时右。";
                break;
            case "头后仰" :
                explainContent = "由于颈背肌肉张力增高，婴儿侧卧时，头向后仰大于20°。";
                break;
            case "自发拉弓射箭样姿势" :
                explainContent = "婴儿紧张或检查时出现的一种面偏向一侧、枕部侧上肢上举、面朝向侧上肢伸直的拉弓射箭样的姿势。6个月后出现或之前上述姿势明显为异常。";
                break;
            case "自发足拇指上翘" :
                explainContent = "自发出现的足踇趾上跷，其余各趾呈扇形张开表现。各月龄自发强阳性为异常。";
                break;
            case "徐动" :
                explainContent = "常发现某些动作夹杂着许多多余的动作，四肢及头部在不停地晃动，而自己没有一定的自控能力。常伴有语言障碍，吐词不清。";
                break;
            case "过度松软" :
                explainContent = "孩子常控头不好，四肢松软，呈仰卧四肢外展外旋的蛙状异常姿势；过于安静,手脚少动，喂养困难，孩子常不能按照发育规律进行正常的运动。";
                break;
            case "一侧或一个肢体活动明显减少或异常" :
                explainContent = "主要表现是一侧或一个肢体活动明显减少或伴有姿势和运动异常。";
                break;
            case "婴儿仰卧位时双下肢僵直" :
                explainContent = "主要表现是双下肢僵硬和足尖绷直。";
                break;
            case "飞机手" :
                explainContent = "双臂后伸、旋内（前），呈现喷气式飞机机翼样的异常姿势。可伴有肩内收、肘后伸、拇指内收、手握拳等异常。";
                break;
            case "前臂旋前" :
                explainContent = "前臂旋前是以中立位是手掌和肘关节向正前方，这时将拇指向内的旋转动作称旋前。";
                break;
            case "肘屈曲" :
                explainContent = "肘关节正常屈曲角度为130-150度，伸直为-10-0度。肘屈曲是肘伸展角减小、被动肘伸展时有阻力。";
                break;
            case "肩内收" :
                explainContent = "由手臂自然下垂到手臂向内侧平举的角度超过40度。肩内收时检查肩外展角减小、被动肩外展时有阻力。";
                break;
            case "肩内旋" :
                explainContent = "肩内旋是肱骨的相对内旋，表现出来圆肩的体态。肩关节屈曲内旋，会导致患者的前臂多数时间，位于身体的前侧，如果患者要拿侧面的一些东西，也就是向侧面伸展前臂会受到非常大的限制。严重的情况下，甚至还会导致患者的前臂持续的背在身后。";
                break;
            case "足跖屈致足背屈角>100°" :
                explainContent = "足尖下垂，足背向小腿前面远离踝关节的屈称足跖屈，足跖屈致足背屈角>100°亦称尖足。";
                break;
            case "剪刀步" :
                explainContent = "表现为髋关节内收、内旋，屈曲、足下垂及内翻，行走时双膝互相摩擦，甚至两腿完全交叉，呈典型的\"剪刀式\"步态。";
                break;
            case "肌性足内翻/肌性足外翻" :
                explainContent = "足内翻是胫骨后肌痉挛引起跟舟骰关节呈半脱位状态,使足固定于一种内收、旋后内翻姿势。肌性足外翻反之。";
                break;
            case "足掌负重足跟抬起≥30°站立" :
                explainContent = "扶站时足掌负重，足跟抬起较高≥30°，扶走时足跟不着地。同时伴有足背屈角异常及触摸小腿后侧肌肉张力增高。";
                break;
            case "快速踏步状" :
                explainContent = "迈步时足跟抬起较高，足跟不着地，脚尖着地持重能力差（或不持重），呈现出快速的踏步状。同时伴有足背屈角异常和小腿后侧肌肉张力增高。";
                break;
            case "足跟未着地迈第二步" :
                explainContent = "小腿后侧肌肉痉挛、小腿前侧肌肉力弱，足向足底方向痉挛发紧，扶站时足跟抬起较高，扶走时足跟不着地迈步，同时伴有足背屈角异常和小腿后侧肌肉张力增高。";
                break;
        }
        return explainContent;
    }

    private List<String> getTrainMethod(ScaleEvaluationRecord record, int month) {
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
                        if (option.getContent().contains("抬头") && month >= 6) {
                            list.add(option.getContent());
                        }
                        if (option.getContent().contains("翻身") && month >= 9) {
                            list.add(option.getContent());
                        }
                        if (option.getContent().contains("坐") && month >= 9) {
                            list.add(option.getContent());
                        }
                        if (option.getContent().contains("爬") && month >= 12) {
                            list.add(option.getContent());
                        }
                        if (option.getContent().contains("站") && month >= 15) {
                            list.add(option.getContent());
                        }
                        if (option.getContent().contains("走") && month >= 15) {
                            list.add(option.getContent());
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<AbnormalItermInterventionMethodVo> getTrainMethodVo(ScaleEvaluationRecord record) {

        ArrayList<AbnormalItermInterventionMethodVo> list = new ArrayList<>();

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

        ArrayList<String> contents = new ArrayList<>();

        if (answerWithRemarkDtos.size() == 3) {
            AnswerWithRemarkDto answerWithRemarkDto = answerWithRemarkDtos.get(2);
            QuestionDto questionDto = getQuestionDto(questions, answerWithRemarkDto.getQuestionSn());

            String[] options = answerWithRemarkDto.getOptionSn().split(",");

            for (String optionSn : options) {
                Option option = getOption(questionDto.getOptions(), Integer.parseInt(optionSn));
                if (option != null) {
                    contents.add(option.getContent());
                }
            }
        }
        if (CollectionUtils.isEmpty(contents)) {
            return list;
        }
//        String productId = "p_63423bb0e4b0a51fef25b0a7";
//        CheckVideoOrderVo fuchuVideoOrderVo = scaleOrderService.checkVideoOrder(record.getUserId(), productId);
        //没有权益，返回空
//        if (fuchuVideoOrderVo == null || !fuchuVideoOrderVo.getHasPaidOrder()) {
//            return list;
//        }
//        //有权益
//        for (String content : contents) {
//            //获取有异常的
//            AbnormalItermInterventionMethodVo vo = trainMethodMap.get(content);
//            if (vo != null) {
//                //查询小鹅通商品信息
////                List<GetGoodsData> goods = xiaoeService.getGoods(vo.getResources());
//                for (GetGoodsData good : goods) {
//                    AbnormalItermInterventionMethodVo methodVo = new AbnormalItermInterventionMethodVo();
//                    methodVo.setRecordId(record.getId());
//                    methodVo.setProductId(productId);
//                    methodVo.setAbnormalIterm(good.getGoodsName());
//                    methodVo.setPage("page/home/content/content_video/content_video?id=" + good.getResourceId());
//                    methodVo.setAppId(vo.getAppId());
//                    methodVo.setCoverUrl(good.getGoodsImg()[0]);
//                    methodVo.setName(good.getGoodsName());
//                    methodVo.setResourceId(good.getResourceId());
//                    methodVo.setType(good.getResourceType());
//                    list.add(methodVo);
//                }
//            }
//        }

        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(
                    collectingAndThen(
                            toCollection(() -> new TreeSet<>(comparing(AbnormalItermInterventionMethodVo::getResourceId))), ArrayList::new));
        }
        return list;
    }


    private List<AbnormalItermInterventionMethodVo> getFuchuMethodVo(ScaleEvaluationRecord record) {

        ArrayList<AbnormalItermInterventionMethodVo> list = new ArrayList<>();

        String resourceId = "v_62f07b48e4b0a51fef09d376";
        CheckVideoOrderVo fuchuVideoOrderVo = scaleOrderService.checkVideoOrder(record.getUserId(), resourceId);
        //没有权益，返回空
        if (fuchuVideoOrderVo == null || !fuchuVideoOrderVo.getHasPaidOrder()) {
            return list;
        }
        //有权益，返回视频信息
        String[] ids = {resourceId};
        GetGoodsParam getGoodsParam = new GetGoodsParam();
        getGoodsParam.setIds(ids);
        getGoodsParam.setType(3);
        List<GetGoodsParam> resources = new ArrayList<>();
        resources.add(getGoodsParam);

//        List<GetGoodsData> goods = xiaoeService.getGoods(resources);
//        for (GetGoodsData good : goods) {
//            AbnormalItermInterventionMethodVo methodVo = new AbnormalItermInterventionMethodVo();
//            methodVo.setRecordId(record.getId());
//            methodVo.setProductId("p_62d775b1e4b0a51fef018eab");
//            methodVo.setAbnormalIterm(good.getGoodsName());
//            methodVo.setPage("page/home/content/content_video/content_video?id=" + good.getResourceId());
//            methodVo.setAppId("wx98dc9b974915de77");
//            methodVo.setCoverUrl(good.getGoodsImg()[0]);
//            methodVo.setName(good.getGoodsName());
//            methodVo.setResourceId(good.getResourceId());
//            methodVo.setType(good.getResourceType());
//            list.add(methodVo);
//        }
        return list;
    }

    private List<AbnormalItermInterventionMethodVo> getMethods(List<String> abnormalIterm, ScaleEvaluationRecord record) {

        ArrayList<AbnormalItermInterventionMethodVo> list = new ArrayList<>();

        String productId = "p_62d775b1e4b0a51fef018eab";
        CheckVideoOrderVo fuchuVideoOrderVo = scaleOrderService.checkVideoOrder(record.getUserId(), productId);
        //没有权益，返回空
        if (fuchuVideoOrderVo == null || !fuchuVideoOrderVo.getHasPaidOrder()) {
            return list;
        }

        //有权益
        for (String s : abnormalIterm) {
//                    String iterm = s.split("-")[1];
            AbnormalItermInterventionMethodVo vo = abnormalMethodMap.get(s);
            if (vo != null) {
                //查询小鹅通商品信息
//                List<GetGoodsData> goods = xiaoeService.getGoods(vo.getResources());
//                for (GetGoodsData good : goods) {
//                    AbnormalItermInterventionMethodVo methodVo = new AbnormalItermInterventionMethodVo();
//                    methodVo.setRecordId(record.getId());
//                    methodVo.setProductId(productId);
//                    methodVo.setAbnormalIterm(good.getGoodsName());
//                    methodVo.setPage("page/home/content/content_video/content_video?id=" + good.getResourceId());
//                    methodVo.setAppId(vo.getAppId());
//                    methodVo.setCoverUrl(good.getGoodsImg()[0]);
//                    methodVo.setName(good.getGoodsName());
//                    methodVo.setResourceId(good.getResourceId());
//                    methodVo.setType(good.getResourceType());
//                    list.add(methodVo);
//                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(
                    collectingAndThen(
                            toCollection(() -> new TreeSet<>(comparing(AbnormalItermInterventionMethodVo::getResourceId))), ArrayList::new));
        }
        return list;
    }

    @Override
    public InterventionMethodVo getAbnormalMethodDetail(String abnormalIterm) {
        AbnormalItermInterventionMethodVo vo = abnormalMethodMap.get(abnormalIterm);
        if (vo != null) {
            return vo.getMethodDetail();
        }
        return null;
    }

    @Override
    public List<String> getReportPic(Long id) {
        ScaleEvaluationRecord record = scaleEvaluationRecordDao.selectById(id);
        if (record == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }

        //如果已存在，直接返回
        if (StringUtils.isNotBlank(record.getResultPics())) {
            String[] strings = record.getResultPics().split(",");
            List<String> collect = Arrays.stream(strings).map(str -> {
                long aLong = Long.parseLong(str);
                return fileService.getFileUrl(aLong, true);
            }).collect(Collectors.toList());

            return collect;
        }

        Long resultFileId = record.getResultFileId();
        if (resultFileId != null && resultFileId > 0) {
            File file = fileService.downloadFile(resultFileId);
            try {
                List<File> files = PdfUtil.pdfToImg(file);
                ArrayList<Long> imgFileIds = new ArrayList<>();
                for (File imgFile : files) {
                    String imgFileKey = fileService.saveFile(OldFileType.PICTURE, imgFile, imgFile.getName());
                    Files img = new Files();
                    img.setStatus(BaseStatus.NORMAL.getStatus());
                    img.setRawName(imgFile.getName());
                    img.setOriginalName(imgFile.getName());
                    img.setFileType(OldFileType.PICTURE.getCode());
                    img.setCreated(DateUtil.getCurrentTimeStamp());
                    img.setFileSize(imgFile.getTotalSpace());
                    img.setFilePath(imgFileKey);
                    img.setExtension(FilenameUtils.getExtension(imgFile.getName()));
                    img.setUpdated(DateUtil.getCurrentTimeStamp());
                    fileService.insertFiles(img);
                    imgFileIds.add(img.getId());
                }

                //保存图片 id
                record.setResultPics(StringUtils.join(imgFileIds.toArray(), ","));
                scaleEvaluationRecordDao.updateById(record);
                List<String> collect = imgFileIds.stream().map(imgId -> fileService.getFileUrl(imgId, true)).collect(Collectors.toList());
                return collect;
            } catch (IOException e) {
                throw new OldServiceException(ResultCode.INTERNAL_SERVER_ERROR);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<ScaleEvaluationRecord> listByChildrenAndScaleTableCodes(long childrenId, List<Byte> scaleTableCodes, List<ScaleStatus> status) {
        if (childrenId <= 0 || CollectionUtils.isEmpty(scaleTableCodes)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ScaleEvaluationRecord> wrapper = new QueryWrapper<ScaleEvaluationRecord>().lambda();
        wrapper.eq(ScaleEvaluationRecord::getStatus, BaseStatus.NORMAL.getStatus())
                .eq(ScaleEvaluationRecord::getChildrenId, childrenId)
                .in(ScaleEvaluationRecord::getProgressStatus, status.stream().map(ScaleStatus::getStatus).collect(Collectors.toList()))
                .in(ScaleEvaluationRecord::getScaleTableCode, scaleTableCodes)
                .orderByDesc(ScaleEvaluationRecord::getId);
        return scaleEvaluationRecordDao.selectList(wrapper);
    }

    @Override
    public void saveOrUpdateDDSTRecord(CreateScaleEvaluationRecordRequest request) {
        ScaleEvaluationRecord record;
        List<ScaleEvaluationRecord> scaleEvaluationRecords =
                listByChildrenAndScaleTableCodes(request.getChildrenId(), Collections.singletonList(request.getScaleTableCode()), Collections.singletonList(ScaleStatus.NOT_EVALUATE));

        ScaleTable scaleTable = scaleTableService.getScaleTable(request.getScaleTableCode());
        if (scaleTable == null) {
            throw new OldServiceException(ResultCode.UNSUPPORTED_SCALE_CODE);
        }

        if (CollectionUtils.isNotEmpty(scaleEvaluationRecords)) {

            //有记录，用已经存在的记录
            record = scaleEvaluationRecords.get(0);
            log.info("有记录，用已经存在的记录,record:{}", JSON.toJSONString(record));
            List<AnswerWithRemarkDto> newAnswerWithRemarkDtos = new ArrayList<>();
            String answerWithRemark = record.getAnswerWithRemark();
            if (StringUtils.isNotBlank(answerWithRemark)) {
                //获取已有的答案,替换原答案
                List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
                HashMap<String, AnswerWithRemarkDto> map = new HashMap<>();
                for (AnswerWithRemarkDto answerWithRemarkDto : answerWithRemarkDtos) {
                    map.put(answerWithRemarkDto.getQuestionSn() + "", answerWithRemarkDto);
                }
                List<AnswerRequest> answers = request.getAnswers();
                for (AnswerRequest answer : answers) {
                    AnswerWithRemarkDto answerWithRemarkDto = null;
                    if (map.containsKey(answer.getQuestionSn() + "")) {
                        answerWithRemarkDto = map.get(answer.getQuestionSn() + "");
                    } else {
                        answerWithRemarkDto = new AnswerWithRemarkDto();
                    }
                    if (CollectionUtils.isNotEmpty(answer.getAnswerSn())) {
                        answerWithRemarkDto.setOptionSn(StringUtils.join(answer.getAnswerSn().toArray(), ","));
                    } else {
                        answerWithRemarkDto.setOptionSn("");
                    }
                    answerWithRemarkDto.setQuestionSn(answer.getQuestionSn());
                    answerWithRemarkDto.setRemark(answer.getRemark());

                    List<AttachmentRequest> attachments = answer.getAttachments();
                    ArrayList<AttachmentDto> attachmentDtos = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(attachments)) {
                        for (AttachmentRequest attachment : attachments) {
                            attachmentDtos.add(new AttachmentDto(Long.parseLong(attachment.getServerId()), 0L, "", ""));
                        }
                    }
                    answerWithRemarkDto.setAttachmentDtos(attachmentDtos);
                    newAnswerWithRemarkDtos.add(answerWithRemarkDto);
                }
            } else {
                //原来没有答案，新建答案
                List<AnswerRequest> answers = request.getAnswers();
                for (AnswerRequest answer : answers) {
                    AnswerWithRemarkDto answerWithRemarkDto = new AnswerWithRemarkDto();
                    answerWithRemarkDto.setOptionSn(StringUtils.join(answer.getAnswerSn().toArray(), ","));
                    answerWithRemarkDto.setQuestionSn(answer.getQuestionSn());
                    answerWithRemarkDto.setRemark(answer.getRemark());

                    List<AttachmentRequest> attachments = answer.getAttachments();
                    ArrayList<AttachmentDto> attachmentDtos = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(attachments)) {
                        for (AttachmentRequest attachment : attachments) {
                            attachmentDtos.add(new AttachmentDto(Long.parseLong(attachment.getServerId()), 0L, "", ""));
                        }
                    }
                    answerWithRemarkDto.setAttachmentDtos(attachmentDtos);
                    newAnswerWithRemarkDtos.add(answerWithRemarkDto);
                }
            }
            record.setAnswerWithRemark(JSON.toJSONString(newAnswerWithRemarkDtos));
            record.setUpdated(DateUtil.getCurrentTimeStamp());
            scaleEvaluationRecordDao.updateById(record);
        } else {
            log.info("不存在记录，新建");
            //不存在记录，新建
            List<AnswerWithRemarkDto> answerWithRemarkDtos = new ArrayList<>();
            List<AnswerRequest> answers = request.getAnswers();
            for (AnswerRequest answer : answers) {
                AnswerWithRemarkDto answerWithRemarkDto = new AnswerWithRemarkDto();
                answerWithRemarkDto.setOptionSn(StringUtils.join(answer.getAnswerSn().toArray(), ","));
                answerWithRemarkDto.setQuestionSn(answer.getQuestionSn());
                answerWithRemarkDto.setRemark(answer.getRemark());

                List<AttachmentRequest> attachments = answer.getAttachments();
                ArrayList<AttachmentDto> attachmentDtos = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(attachments)) {
                    for (AttachmentRequest attachment : attachments) {
                        attachmentDtos.add(new AttachmentDto(Long.parseLong(attachment.getServerId()), 0L, "", ""));
                    }
                }
                answerWithRemarkDto.setAttachmentDtos(attachmentDtos);
                answerWithRemarkDtos.add(answerWithRemarkDto);
            }
            record = new ScaleEvaluationRecord();
            record.setBirthday(request.getBirthday());
            record.setConclusion("");
            record.setCreated(DateUtil.getCurrentTimeStamp());
            record.setGender(request.getGender());
            record.setName(request.getName());
            record.setProgressStatus(ScaleStatus.NOT_EVALUATE.getStatus());
            record.setResultFileId(0l);
            record.setScaleTableCode(request.getScaleTableCode());
            record.setScaleTableName("");
            record.setResult("");
            record.setStatus(BaseStatus.NORMAL.getStatus());
            record.setUpdated(DateUtil.getCurrentTimeStamp());
            record.setUserId(SecurityUtils.getUserId());
            //todo:openId
//            record.setOpenId(AuthContext.getUser().getOpenId());
            record.setType(scaleTable.getType());
            record.setDoctorId(0l);
            record.setOrganizationId(SecurityUtils.getLoginUser().getDeptId());
            record.setChildrenId(request.getChildrenId() == null ? 0 : request.getChildrenId());
            record.setCategory(request.getCategory());
            record.setDataStatus(DataStatus.UPLOADED.getStatus());
            record.setDataUploadSource(DataUploadSource.MINI_APP.getCode());
            record.setDoctorScore(0);
            record.setUserScore(0);
            record.setResult("");
            //add 增加预约信息
            record.setReserveId(0l);
            record.setReserveType(WorkScheduleType.AI_EVALUATE.getType());
            record.setOrderId(request.getOrderId());
            record.setAnswerWithRemark(JSON.toJSONString(answerWithRemarkDtos));
            scaleEvaluationRecordDao.insert(record);
        }
    }

    private ScaleEvaluationRecordListVo convertToScaleEvaluationRecordListVo(ScaleEvaluationRecord record) {

//        ScaleTableVo scaleTableVo = scaleTableService.getScaleTableVo(record.getScaleTableCode(), 0);
        ScaleEvaluationRecordListVo vo = new ScaleEvaluationRecordListVo();
        ScaleStatus status = ScaleStatus.getStatus(record.getProgressStatus());
        vo.setProgressStatusByte(record.getProgressStatus());
        WorkScheduleType type = WorkScheduleType.getType(record.getReserveType());
        if (type == WorkScheduleType.AI_EVALUATE || type == WorkScheduleType.CLINIC_EVALUATE) {
            ScaleTableConstant.ScaleTableCode scaleTableCode = ScaleTableConstant.ScaleTableCode.getScaleTableType(record.getScaleTableCode());
            ScaleTableConstant.ScaleTableClassification scaleTableClassification = ScaleTableConstant.ScaleTableClassification.getScaleTableClassification(scaleTableCode.getClassification());
            vo.setScaleName(scaleTableCode.getDesc());
            vo.setScaleClassification(scaleTableClassification.getDesc());
            vo.setProgressStatus(status.getDesc());
        } else if (type == WorkScheduleType.RECOVERY_GUIDE_OFFLINE) {
            vo.setScaleName("康复指导");
            vo.setScaleClassification("康复指导");
            vo.setProgressStatus(status == ScaleStatus.SCHEDULED ? "待设计" : status.getDesc());
        }
        vo.setId(record.getId());
        vo.setTime(DateUtil.getYMDHMSDate(record.getCreated()));
        vo.setScaleTableCode(record.getScaleTableCode());
        vo.setType(record.getType());
        vo.setReserveType(record.getReserveType());

        if (record.getOrderId() != null && record.getOrderId() > 0) {
            ScaleOrder scaleOrder = scaleOrderService.getById(record.getOrderId());
            if (scaleOrder != null) {
                vo.setUseWay(scaleOrder.getUseWay());
            } else {
                vo.setUseWay(OrderUseWay.ONLINE.getCode());
            }
        } else {
            vo.setUseWay(OrderUseWay.ONLINE.getCode());
        }

        return vo;
    }

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

    private List<SuggestVo> getSuggestVo(String result) {
        return suggestMap.get(result).getSuggest();
    }

    private String getRemark(String result) {
        return suggestMap.get(result).getRemark();
    }


}
