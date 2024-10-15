package com.fushuhealth.recovery.dal.vo.h5;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fushuhealth.recovery.dal.dto.BaseScaleEvaluationResult;
import lombok.Data;

import java.util.List;

@Data
public class ScaleRecordVo {

    private Long id;

    private Long userId;

    private String name;

    private String gender;

    private String birthday;

    private String gestationalWeek;

    private Integer birthdayWeight;

    private String age;

    private Byte scaleTableCode;

    private String scaleTableName;

    private String medicalCardNumber;

    private String created;

    private String evaluateDate;

    private Integer userScore;

    private Integer doctorScore;

    private Byte progressStatusCode;

    private String progressStatus;

    private String conclusion;

    private String doctorName;

    private String reviewDoctorName;

    private Boolean askDoctor;

    private BaseScaleEvaluationResult scaleResult;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ScaleRecordQuestionVo> answers;

    private String weWorkQrCode;
}
