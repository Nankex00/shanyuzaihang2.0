package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class ScaleRecordVo {

    private Long id;

    private Long userId;

    private Long childrenId;

    private String medicalNumber;

    private String name;

    private String gender;

    private String birthday;

    private String age;

    private String phone;

    private String gestationalWeek;

    private String correctAge;

    private Integer birthdayWeight;

    private Integer birthWeight;

    private List<String> childRisks;

    private List<String> motherRisks;

    private String extraRisks;

    private Integer userScore;

    private Integer doctorScore;

    private String conclusion;

    private String scaleTableName;

    private Byte scaleTableCode;

    private String created;

    private String progressStatus;

    private Byte progressStatusCode;

    private List<ScaleRecordQuestionVo> answers;
}
