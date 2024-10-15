package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserListVo {

    private Long id;

    private String phone;

    private String name;

    private String age;

    private String gender;

    private String creator;

    private String created;

    private Byte trainingStatus;

    private Integer trainingPlanCount;

    private List<SimpleTrainingPlanListVo> plans;
}
