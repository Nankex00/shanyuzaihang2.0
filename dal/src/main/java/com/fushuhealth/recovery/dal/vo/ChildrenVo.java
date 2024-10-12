package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class ChildrenVo extends ChildrenListVo {

    private Integer gestationalWeek;

    private Integer gestationalWeekDay;

    private Integer birthdayWeight;

    private List<String> childRisks;

    private List<String> motherRisks;

    private String medicalCardNumber;

    private String contactPhone;

    private String extraRisks;

    //胎次：头胎，二胎及多胎
    private String parity;

    //窒息情况：无，Apgar评分=1min，Apgar评分=5min，不详
    private String asphyxia;

    //听力筛查：通过，未通过，未筛查，不详
    private String hearingScreening;

    //畸形情况，非必填
    private String deformity;

    //喂养方式
    private String feedingWay;
}
