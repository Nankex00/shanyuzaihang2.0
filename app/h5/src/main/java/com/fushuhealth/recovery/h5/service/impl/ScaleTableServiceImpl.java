package com.fushuhealth.recovery.h5.service.impl;

import com.fushuhealth.recovery.h5.service.ScaleTableService;
import com.fushuhealth.recovery.common.constant.QuestionSubjectEnum;
import com.fushuhealth.recovery.common.constant.ScaleTableConstant;
import com.fushuhealth.recovery.h5.service.FileService;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.dal.dto.Option;
import com.fushuhealth.recovery.dal.dto.QuestionDto;
import com.fushuhealth.recovery.dal.entity.ScaleTable;
import com.fushuhealth.recovery.dal.scale.ScaleTableResolver;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionAnswerVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionSubjectVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Service
public class ScaleTableServiceImpl implements ScaleTableService {

    @Autowired
    private FileService fileService;

    @Value("${file.domain}")
    private String fileDomain;

    @Value("${spring.profiles.active}")
    private String env;

    private static HashMap<Byte, Integer> scaleTablePriceMap = new HashMap<>();

    //量表金额
    static {
        scaleTablePriceMap.put((byte)10, 9900);
        scaleTablePriceMap.put((byte)11, 9900);
        scaleTablePriceMap.put((byte)13, 9900);
    }

    @Override
    public ScaleTableVo getScaleTableVo(byte code, long birthday) {

        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(code);

        if (scaleTable == null) {
            return null;
        }

        if (birthday >= 0) {
            int ageOfMonth = OldDateUtil.getMonthBetween(birthday, OldDateUtil.getCurrentTimeStamp());
            //根据年龄筛选题
            List<QuestionDto> questions = scaleTable.getQuestions();
            List<QuestionDto> list = questions.stream().filter(questionDto -> questionDto.getMaxMonth() >= ageOfMonth && questionDto.getMinMonth() <= ageOfMonth).collect(Collectors.toList());
            ScaleTable st = new ScaleTable();
            BeanUtils.copyProperties(scaleTable, st);
            st.setQuestions(list);
            return convertToScaleTableVo(st);
        }
        return convertToScaleTableVo(scaleTable);
    }

    @Override
    public ScaleTable getScaleTable(byte code) {
        Map<Byte, ScaleTable> scaleTableMap = ScaleTableResolver.getScaleTableMap();
        ScaleTable scaleTable = scaleTableMap.get(code);
        return scaleTable;
    }

    @Override
    public Integer getPrice(byte code) {
        Integer integer = scaleTablePriceMap.get(code);
        if (integer == null) {
            return 0;
        }
        if (env.equalsIgnoreCase("test")) {
            integer = 1;
        }
        return integer;
    }


    private ScaleTableVo convertToScaleTableVo(ScaleTable scaleTable) {
        ScaleTableVo scaleTableVo = new ScaleTableVo();
        scaleTableVo.setCode(scaleTable.getCode());
        scaleTableVo.setName(scaleTable.getName());
        scaleTableVo.setClassification(ScaleTableConstant.ScaleTableClassification.getScaleTableClassification(scaleTable.getClassification()).getDesc());

        List<String> scaleTableCarouselFileIds = scaleTable.getCarouselFileIds();
        List<String> scaleTableFileUrls = scaleTableCarouselFileIds.stream().map(id -> fileDomain + id.trim()).collect(Collectors.toList());
        scaleTableVo.setCarousels(scaleTableFileUrls);

        List<ScaleTableQuestionSubjectVo> subjects = new ArrayList<>();

        List<QuestionDto> questions = scaleTable.getQuestions();

        Map<Byte, List<QuestionDto>> subjectMap = questions.stream().collect(Collectors.groupingBy(QuestionDto::getSubject));

        for (Map.Entry<Byte, List<QuestionDto>> entry : subjectMap.entrySet()) {

            ScaleTableQuestionSubjectVo scaleTableQuestionSubjectVo = new ScaleTableQuestionSubjectVo();

            //设置总数
            List<QuestionDto> questionsBySubject =  entry.getValue();
            scaleTableQuestionSubjectVo.setSum(questionsBySubject.size());

            //设置分类名字
            Byte subjectByte = entry.getKey();
            scaleTableQuestionSubjectVo.setSubjectCode(subjectByte);
            QuestionSubjectEnum questionSubject = QuestionSubjectEnum.getQuestionSubject(subjectByte);
            scaleTableQuestionSubjectVo.setSubject(questionSubject.getDesc());

            ArrayList<ScaleTableQuestionVo> questionVos = new ArrayList<>();
            for (QuestionDto questionDto : questionsBySubject) {
                List<Option> options = questionDto.getOptions();
                List<ScaleTableQuestionAnswerVo> answerVos = options.stream().map(option -> convertToScaleTableQuestionAnswerVo(option)).collect(Collectors.toList());

                List<String> carouselFileIds = questionDto.getCarouselFileIds();
                List<String> fileUrls = carouselFileIds.stream().map(id -> fileDomain + id.trim()).collect(Collectors.toList());

                String introduction = questionDto.getIntroduction();
                ScaleTableQuestionVo scaleTableQuestionVo = new ScaleTableQuestionVo(questionDto.getSn(), questionDto.getName(), questionDto.getType(), fileUrls, questionDto.getAttachmentType(), answerVos, introduction);

                questionVos.add(scaleTableQuestionVo);
            }
            scaleTableQuestionSubjectVo.setQuestions(questionVos);
            subjects.add(scaleTableQuestionSubjectVo);
        }
        List<ScaleTableQuestionSubjectVo> collect = subjects.stream().sorted(Comparator.comparing(ScaleTableQuestionSubjectVo::getSubjectCode)).collect(Collectors.toList());
        scaleTableVo.setSubjects(collect);
        return scaleTableVo;
    }

    private ScaleTableQuestionAnswerVo convertToScaleTableQuestionAnswerVo(Option option) {
        ScaleTableQuestionAnswerVo vo = new ScaleTableQuestionAnswerVo();
        vo.setSn(option.getSn());
        vo.setContent(option.getContent());
        return vo;
    }
}
