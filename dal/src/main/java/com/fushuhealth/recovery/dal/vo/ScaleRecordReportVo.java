package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fushuhealth.recovery.dal.dto.BaseScaleEvaluationResult;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ScaleRecordReportVo {

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

    private String created;

    private String evaluateDate;

    private Integer userScore;

    private Integer doctorScore;

    private Byte progressStatusCode;

    private String progressStatus;

    private String conclusion;

    private String doctorName;

    @JsonIgnore
    private List<String> bigMovement;

    @JsonIgnore
    private Integer monthAge;

    private BaseScaleEvaluationResult scaleResult;

    @JsonIgnore
    private Map<String, Set<String>> abnormalVideos;

    @JsonIgnore
    private Map<String, Set<String>> bigMoveVideos;
}
