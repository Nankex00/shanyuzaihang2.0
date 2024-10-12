package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

@Data
public class ChildrenListVo {

    private Long id;

    private String name;

    private String gender;

    private String birthday;

    private Long birthdayDate;

    private String age;

    private String correctAge;
}
