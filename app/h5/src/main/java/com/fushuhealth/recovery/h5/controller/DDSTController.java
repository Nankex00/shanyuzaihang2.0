package com.fushuhealth.recovery.h5.controller;

import com.alibaba.fastjson.JSON;
import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.DDSTStatus;
import com.fushuhealth.recovery.common.constant.Gender;
import com.fushuhealth.recovery.common.constant.ScaleStatus;
import com.fushuhealth.recovery.common.constant.ScaleTableConstant.ScaleTableClassification;
import com.fushuhealth.recovery.common.constant.ScaleTableConstant.ScaleTableCode;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.dal.dto.AnswerWithRemarkDto;
import com.fushuhealth.recovery.dal.dto.AttachmentDto;
import com.fushuhealth.recovery.dal.dto.DDSTResult;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.Files;
import com.fushuhealth.recovery.dal.entity.ScaleEvaluationRecord;
import com.fushuhealth.recovery.dal.entity.ScaleTable;
import com.fushuhealth.recovery.dal.scale.ScaleTableResolver;
import com.fushuhealth.recovery.dal.vo.*;
import com.fushuhealth.recovery.dal.vo.h5.ScaleEvaluateLogListVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleRecordVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableVo;
import com.fushuhealth.recovery.h5.model.request.CreateScaleEvaluationRecordRequest;
import com.fushuhealth.recovery.h5.service.*;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionSubjectVo;
import com.fushuhealth.recovery.dal.vo.h5.DDSTQuestionVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionVo;
import com.fushuhealth.recovery.dal.vo.h5.AttachmentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/ddst")
public class DDSTController {


    @Autowired
    private ScaleEvaluationRecordService scaleEvaluationRecordService;

    @Autowired
    private IChildrenService childrenService;

    @Autowired
    private ScaleTableService scaleTableService;

    @Autowired
    private FileService fileService;

//    @Autowired
//    private DoctorService doctorService;

    @Autowired
    private ScaleEvaluateLogService scaleEvaluateLogService;

    // 获取DDST列表
    @GetMapping("/list")
    public OldBaseResponse getDDSTList(@RequestParam Long childrenId) {

        ChildrenVo children = childrenService.getChildren(childrenId);
        if (children == null) {
            return OldBaseResponse.error(ResultCode.PATIENT_ERROR);
        }

        List<ScaleTableCode> scaleTables = ScaleTableCode.getScaleTableByClassification(ScaleTableClassification.DDST);
        List<Byte> scaleTableCodes  = scaleTables.stream().map(ScaleTableCode::getCode).collect(Collectors.toList());

        // 调用service获取DDST评估记录
        List<ScaleEvaluationRecord> scaleEvaluationRecords = scaleEvaluationRecordService
                .listByChildrenAndScaleTableCodes(childrenId, scaleTableCodes, Arrays.asList(ScaleStatus.NOT_EVALUATE, ScaleStatus.EVALUATED));
        Map<Byte, ScaleEvaluationRecord> map = new HashedMap();
        for (ScaleEvaluationRecord scaleEvaluationRecord : scaleEvaluationRecords) {
            map.put(scaleEvaluationRecord.getScaleTableCode(), scaleEvaluationRecord);
        }

        ArrayList<DDSTListVo> list = new ArrayList<>();
        for (ScaleTableCode scaleTable : scaleTables) {
            DDSTListVo ddstListVo = new DDSTListVo();
            ddstListVo.setCode(scaleTable.getCode());
            ddstListVo.setName(scaleTable.getDesc());
            ddstListVo.setChildrenId(childrenId);

            long suggestTime = calSuggestTime(children.getBirthdayDate(), scaleTable);
            long suggestStartTime = OldDateUtil.getTimeAfterDay(suggestTime, -14);
            long suggestEndTime = OldDateUtil.getTimeAfterDay(suggestTime, 7);
            ddstListVo.setSuggestEndTime(OldDateUtil.getYMD(suggestEndTime));
            ddstListVo.setSuggestStartTime(OldDateUtil.getYMD(suggestStartTime));

            long currentTimeStamp = OldDateUtil.getCurrentTimeStamp();
            DDSTStatus status = DDSTStatus.WAIT_START;
            long recordId = 0L;
            if (map.containsKey(scaleTable.getCode())) {
                if (currentTimeStamp > suggestStartTime && currentTimeStamp < suggestEndTime) {
                    status = DDSTStatus.IN_PROGRESS;
                } else {
                    status = DDSTStatus.FINISHED;
                }
                recordId = map.get(scaleTable.getCode()).getId();
            } else {
                if (currentTimeStamp < suggestStartTime) {
                    status = DDSTStatus.WAIT_START;
                } else if (currentTimeStamp > suggestStartTime && currentTimeStamp < suggestEndTime) {
                    status = DDSTStatus.IN_PROGRESS;
                } else if (currentTimeStamp > suggestEndTime) {
                    status = DDSTStatus.EXPIRED;
                }
            }
            ddstListVo.setStatus(status.getCode());
            ddstListVo.setRecordId(recordId);
            list.add(ddstListVo);
        }
        List<DDSTListVo> sortedList = list.stream().sorted(Comparator.comparing(DDSTListVo::getStatus)).collect(Collectors.toList());
        return OldBaseResponse.success(sortedList);
    }

    //获取 DDST 题目列表
    @GetMapping("/questions")
    public OldBaseResponse getDDSTQuestions(@RequestParam(defaultValue = "0") Byte code,
                                         @RequestParam(defaultValue = "0") Long childrenId) {

        ChildrenVo children = childrenService.getChildren(childrenId);
        if (children == null) {
            return OldBaseResponse.error(ResultCode.PATIENT_ERROR);
        }
        ScaleTableVo scaleTableVo = scaleTableService.getScaleTableVo(code, children.getBirthdayDate());

        ScaleEvaluationRecord record = null;
        List<ScaleEvaluationRecord> scaleEvaluationRecords = scaleEvaluationRecordService
                .listByChildrenAndScaleTableCodes(childrenId, Collections.singletonList(code), Collections.singletonList(ScaleStatus.NOT_EVALUATE));
        if (CollectionUtils.isNotEmpty(scaleEvaluationRecords)) {
            record = scaleEvaluationRecords.get(0);
        }

        HashMap<String, AnswerWithRemarkDto> map = new HashMap<>();
        if (record != null) {
            String answerWithRemark = record.getAnswerWithRemark();
            if (StringUtils.isNotBlank(answerWithRemark)) {
                List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
                for (AnswerWithRemarkDto answerWithRemarkDto : answerWithRemarkDtos) {
                    map.put(answerWithRemarkDto.getQuestionSn() + "", answerWithRemarkDto);
                }
            }
        }

        List<DDSTQuestionVo> list = new ArrayList<>();
        List<ScaleTableQuestionSubjectVo> subjects = scaleTableVo.getSubjects();
        if (CollectionUtils.isNotEmpty(subjects)) {
            for (ScaleTableQuestionSubjectVo subject : subjects) {
                List<ScaleTableQuestionVo> questions = subject.getQuestions();
                for (ScaleTableQuestionVo question : questions) {
                    DDSTQuestionVo vo = new DDSTQuestionVo();
                    vo.setSubjectCode(subject.getSubjectCode());
                    vo.setSubject(subject.getSubject());
                    vo.setSn(question.getSn());
                    vo.setName(question.getName());
                    vo.setType(question.getType());
//                    vo.setCarousels(question.getCarousels());
                    List<String> carousels = question.getCarousels();
                    List<String> videos = new ArrayList<>();
                    List<String> pictures = new ArrayList<>();
                    for (String carousel : carousels) {
                        if (carousel.endsWith("mp4")) {
                            videos.add(carousel);
                        } else {
                            pictures.add(carousel);
                        }
                    }
                    vo.setIntroduction(question.getIntroduction());
                    vo.setAnswers(question.getAnswers());
                    vo.setAttachmentType(question.getAttachmentType());
                    if (map.containsKey(question.getSn() + "")) {
                        vo.setStatus((byte)1);
                        AnswerWithRemarkDto answer = map.get(question.getSn() + "");
                        vo.setRemark(answer.getRemark());
                        List<AttachmentDto> attachmentDtos = answer.getAttachmentDtos();
                        if (CollectionUtils.isNotEmpty(attachmentDtos)) {
                            ArrayList<AttachmentVo> attachmentVos = new ArrayList<>(attachmentDtos.size());
                            for (AttachmentDto attachmentDto : attachmentDtos) {

                                if (attachmentDto.getVideoType().equalsIgnoreCase("depth") || attachmentDto.getVideoType().equalsIgnoreCase("ir")) {
                                    continue;
                                }
                                AttachmentVo attachmentVo = new AttachmentVo();
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
                                attachmentVos.add(attachmentVo);
                            }
                            vo.setAttachments(attachmentVos);
                        } else {
                            vo.setAttachments(new ArrayList<>());
                        }
                    } else {
                        vo.setStatus((byte)0);
                        vo.setRemark("");
                        vo.setAttachments(new ArrayList<>());
                    }
                    list.add(vo);
                }
            }
        }
        return OldBaseResponse.success(list);
    }

    //保存 DDST 题答案
    @PostMapping("/save")
    public OldBaseResponse saveDDSTQuestions(@RequestBody CreateScaleEvaluationRecordRequest request) {

        log.info("保存 DDST request:{}", JSON.toJSONString(request));

        ChildrenVo children = childrenService.getChildren(request.getChildrenId());
        if (children == null) {
            return OldBaseResponse.error(ResultCode.PATIENT_ERROR);
        }
        ScaleTableCode scaleTable = ScaleTableCode.getScaleTableType(request.getScaleTableCode());
        long suggestTime = calSuggestTime(children.getBirthdayDate(), scaleTable);
        long suggestEndTime = OldDateUtil.getTimeAfterDay(suggestTime, 7);

        if (OldDateUtil.getCurrentTimeStamp() > suggestEndTime) {
            return OldBaseResponse.error(ResultCode.SCALE_RECORD_EXPIRED);
        }
        scaleEvaluationRecordService.saveOrUpdateDDSTRecord(request);
        return OldBaseResponse.success();
    }

    @GetMapping("/intro")
    public OldBaseResponse getDDSTIntroduction(@RequestParam Byte code, @RequestParam Integer questionSn) {
        ScaleTableVo scaleTableVo = scaleTableService.getScaleTableVo(code, -1);
        List<ScaleTableQuestionSubjectVo> subjects = scaleTableVo.getSubjects();
        Map<String, ScaleTableQuestionVo> map = new HashMap<>();
        for (ScaleTableQuestionSubjectVo subject : subjects) {
            List<ScaleTableQuestionVo> questions = subject.getQuestions();
            for (ScaleTableQuestionVo question : questions) {
                map.put(question.getSn() + "", question);
            }
        }
        ScaleTableQuestionVo scaleTableQuestionVo = map.get(questionSn + "");
        if (scaleTableQuestionVo != null) {
            List<String> carousels = scaleTableQuestionVo.getCarousels();
            List<String> videos = new ArrayList<>();
            List<String> pictures = new ArrayList<>();
            for (String carousel : carousels) {
                if (carousel.endsWith("mp4")) {
                    videos.add(carousel);
                } else {
                    pictures.add(carousel);
                }
            }
            DDSTQuestionIntroductionVo vo = new DDSTQuestionIntroductionVo(
                    scaleTableQuestionVo.getName(),
                    videos,
                    pictures,
                    scaleTableQuestionVo.getIntroduction());
            return OldBaseResponse.success(vo);
        }
        return OldBaseResponse.success();
    }

    @GetMapping("/report")
    public OldBaseResponse getDDSTReport(@RequestParam Long id) {
        ScaleEvaluationRecord record = scaleEvaluationRecordService.getById(id);
        if (record == null) {
            return OldBaseResponse.error(ResultCode.PARAM_ERROR);
        }

        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(record.getScaleTableCode());
        if (scaleTable == null) {
            return null;
        }

        ScaleRecordVo vo = new ScaleRecordVo();
        vo.setWeWorkQrCode("");
        vo.setUserId(record.getUserId());

        Children children = childrenService.getChildrenById(record.getChildrenId());
        vo.setAge(OldDateUtil.getAge(children.getBirthday()));
        vo.setBirthday(OldDateUtil.getYMD(children.getBirthday()));
        vo.setName(children.getName());
        vo.setGender(Gender.getGender(children.getGender()).getDesc());
        vo.setBirthdayWeight(children.getBirthWeight());
        vo.setGestationalWeek(children.getGestationalWeeks() + "周" + children.getGestationalWeekDay() + "天");
        vo.setMedicalCardNumber(children.getMedicalCardNumber());
        vo.setAskDoctor(false);
        vo.setCreated(OldDateUtil.getYMDHMSDate(record.getCreated()));
        vo.setDoctorScore(record.getDoctorScore());
        vo.setId(record.getId());
        vo.setProgressStatus(ScaleStatus.getStatus(record.getProgressStatus()).getDesc());
        vo.setProgressStatusCode(record.getProgressStatus());
        vo.setScaleTableName(scaleTable.getName());
        vo.setScaleTableCode(scaleTable.getCode());
        vo.setUserScore(record.getUserScore());
        vo.setConclusion(record.getConclusion());
        vo.setEvaluateDate(OldDateUtil.getYMDHMDate(record.getUpdated()));

        //插入结果
        if (record.getProgressStatus() == ScaleStatus.EVALUATED.getStatus()) {

//            todo:加入医生标签
//            DoctorVo doctorVo = doctorService.getDoctorVo(record.getDoctorId());
//            vo.setDoctorName(doctorVo.getName());

            //审核者
            String reviewDoctorName = "";
            List<ScaleEvaluateLogListVo> scaleEvaluateLogListVos = scaleEvaluateLogService.listScaleEvaluateLogByScaleRecordId(record.getId());
            for (ScaleEvaluateLogListVo scaleEvaluateLogListVo : scaleEvaluateLogListVos) {
                if (scaleEvaluateLogListVo.getStatusByte() == ScaleStatus.REVIEWED_WAIT_SEND.getStatus()) {
                    reviewDoctorName = scaleEvaluateLogListVo.getUserName();
                }
            }
            vo.setReviewDoctorName(reviewDoctorName);


            String result = record.getResult();
            if (StringUtils.isNotBlank(result)) {
                vo.setScaleResult(JSON.parseObject(result, DDSTResult.class));
            }
        }
        return OldBaseResponse.success(vo);
    }

    private long calSuggestTime(long datetime, ScaleTableCode scaleTable) {
        int month = 0;
        switch (scaleTable) {
            case DDST_3_MONTH:
                month = 3;
                break;
            case DDST_6_MONTH:
                month = 6;
                break;
            case DDST_8_MONTH:
                month = 8;
                break;
            case DDST_10_MONTH:
                month = 10;
                break;
            case DDST_12_MONTH:
                month = 12;
                break;
            case DDST_15_MONTH:
                month = 15;
                break;
            case DDST_18_MONTH:
                month = 18;
                break;
            case DDST_24_MONTH:
                month = 24;
                break;
            case DDST_30_MONTH:
                month = 30;
                break;
            case DDST_36_MONTH:
                month = 36;
                break;
            default:
                month = 0;
                break;
        }
        return OldDateUtil.getTimeAfterMonth(datetime, month);
    }
}
