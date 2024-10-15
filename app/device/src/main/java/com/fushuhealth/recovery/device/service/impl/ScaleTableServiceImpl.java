//package com.fushuhealth.recovery.device.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.fushuhealth.recovery.common.constant.BaseStatus;
//import com.fushuhealth.recovery.common.constant.QuestionSubjectEnum;
//import com.fushuhealth.recovery.common.constant.ScaleTableConstant;
//import com.fushuhealth.recovery.common.util.DateUtil;
//import com.fushuhealth.recovery.common.util.OldDateUtil;
//import com.fushuhealth.recovery.dal.dao.ScaleCodeDao;
//import com.fushuhealth.recovery.dal.dto.Option;
//import com.fushuhealth.recovery.dal.dto.QuestionDto;
//import com.fushuhealth.recovery.dal.entity.ScaleCode;
//import com.fushuhealth.recovery.dal.entity.ScaleTable;
//import com.fushuhealth.recovery.dal.vo.h5.ScaleTableVo;
//import com.fushuhealth.recovery.dal.scale.ScaleTableResolver;
//import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionAnswerVo;
//import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionSubjectVo;
//import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionVo;
//import com.fushuhealth.recovery.device.model.vo.*;
//import com.fushuhealth.recovery.device.service.ScaleTableService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static java.util.stream.Collectors.collectingAndThen;
//import static java.util.stream.Collectors.toCollection;
//
//@Service
//public class ScaleTableServiceImpl implements ScaleTableService {
//
//    @Value("${file.domain}")
//    private String fileDomain;
//
//    @Autowired
//    private ScaleCodeDao scaleCodeDao;
//
//    @Override
//    public ScaleTableVo getScaleTableVo(byte code) {
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(code);
//
//        if (scaleTable == null) {
//            return null;
//        }
//        return convertToScaleTableVo(scaleTable);
//    }
//
//    @Override
//    public ScaleTableVo getScaleTableVo(byte code, long birthday) {
//
//        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
//        ScaleTable scaleTable = scaleTableMap.get(code);
//
//        if (scaleTable == null) {
//            return null;
//        }
//
//        if (birthday >= 0) {
//            int ageOfMonth = OldDateUtil.getMonthBetween(birthday, DateUtil.getCurrentTimeStamp());
//            //根据年龄筛选题
//            List<QuestionDto> questions = scaleTable.getQuestions();
//            List<QuestionDto> list = questions.stream().filter(questionDto -> questionDto.getMaxMonth() >= ageOfMonth && questionDto.getMinMonth() <= ageOfMonth).collect(Collectors.toList());
//            ScaleTable st = new ScaleTable();
//            BeanUtils.copyProperties(scaleTable, st);
//            st.setQuestions(list);
//            return convertToScaleTableVo(st);
//        }
//        return convertToScaleTableVo(scaleTable);
//    }
//
//    @Override
//    public List<ScaleTableListVo> listScaleTable(String name, byte firstLevel, byte secondLevel) {
//        LambdaQueryWrapper<ScaleCode> wrapper = new QueryWrapper<ScaleCode>().lambda();
//        if (StringUtils.isNotBlank(name)) {
//            wrapper.like(ScaleCode::getName, name);
//        }
//        if (firstLevel > 0) {
//            wrapper.eq(ScaleCode::getFirstLevel, firstLevel);
//        }
//        if (secondLevel > 0) {
//            wrapper.eq(ScaleCode::getSecondLevel, secondLevel);
//        }
//        wrapper.eq(ScaleCode::getStatus, BaseStatus.NORMAL.getStatus());
//        List<ScaleCode> scaleCodes = scaleCodeDao.selectList(wrapper);
//        ArrayList<ScaleCode> collect = scaleCodes.stream().collect(
//                collectingAndThen(
//                        toCollection(() -> new TreeSet<>(Comparator.comparing(ScaleCode::getCode))), ArrayList::new));
//        return collect.stream().map(scaleCode -> new ScaleTableListVo(scaleCode.getName(), scaleCode.getCode())).collect(Collectors.toList());
//    }
//
//    private ScaleTableVo convertToScaleTableVo(ScaleTable scaleTable) {
//        ScaleTableVo scaleTableVo = new ScaleTableVo();
//        scaleTableVo.setCode(scaleTable.getCode());
//        scaleTableVo.setName(scaleTable.getName());
//        scaleTableVo.setClassification(ScaleTableConstant.ScaleTableClassification.getScaleTableClassification(scaleTable.getClassification()).getDesc());
//
//        List<String> scaleTableCarouselFileIds = scaleTable.getCarouselFileIds();
//        List<String> scaleTableFileUrls = scaleTableCarouselFileIds.stream().map(id -> fileDomain + id.trim()).collect(Collectors.toList());
//        scaleTableVo.setCarousels(scaleTableFileUrls);
//
//        List<ScaleTableQuestionSubjectVo> subjects = new ArrayList<>();
//
//        List<QuestionDto> questions = scaleTable.getQuestions();
//
//        Map<Byte, List<QuestionDto>> subjectMap = questions.stream().collect(Collectors.groupingBy(QuestionDto::getSubject));
//
//        for (Map.Entry<Byte, List<QuestionDto>> entry : subjectMap.entrySet()) {
//
//            ScaleTableQuestionSubjectVo scaleTableQuestionSubjectVo = new ScaleTableQuestionSubjectVo();
//
//            //设置总数
//            List<QuestionDto> questionsBySubject =  entry.getValue();
//            scaleTableQuestionSubjectVo.setSum(questionsBySubject.size());
//
//            //设置分类名字
//            Byte subjectByte = entry.getKey();
//            scaleTableQuestionSubjectVo.setSubjectCode(subjectByte);
//            QuestionSubjectEnum questionSubject = QuestionSubjectEnum.getQuestionSubject(subjectByte);
//            scaleTableQuestionSubjectVo.setSubject(questionSubject.getDesc());
//
//            ArrayList<ScaleTableQuestionVo> questionVos = new ArrayList<>();
//            for (QuestionDto questionDto : questionsBySubject) {
//                List<Option> options = questionDto.getOptions();
//                List<ScaleTableQuestionAnswerVo> answerVos = options.stream().map(option -> convertToScaleTableQuestionAnswerVo(option)).collect(Collectors.toList());
//
//                List<String> carouselFileIds = questionDto.getCarouselFileIds();
//                List<String> fileUrls = carouselFileIds.stream().map(id -> fileDomain + id.trim()).collect(Collectors.toList());
//
//                ScaleTableQuestionVo scaleTableQuestionVo = new ScaleTableQuestionVo(questionDto.getSn(), questionDto.getName(), questionDto.getType(), fileUrls, answerVos);
//
//                questionVos.add(scaleTableQuestionVo);
//            }
//            scaleTableQuestionSubjectVo.setQuestions(questionVos);
//            subjects.add(scaleTableQuestionSubjectVo);
//        }
//        List<ScaleTableQuestionSubjectVo> collect = subjects.stream().sorted(Comparator.comparing(ScaleTableQuestionSubjectVo::getSubjectCode)).collect(Collectors.toList());
//        scaleTableVo.setSubjects(collect);
//        return scaleTableVo;
//    }
//
//    private ScaleTableQuestionAnswerVo convertToScaleTableQuestionAnswerVo(Option option) {
//        ScaleTableQuestionAnswerVo vo = new ScaleTableQuestionAnswerVo();
//        vo.setSn(option.getSn());
//        vo.setContent(option.getContent());
//        return vo;
//    }
//}
