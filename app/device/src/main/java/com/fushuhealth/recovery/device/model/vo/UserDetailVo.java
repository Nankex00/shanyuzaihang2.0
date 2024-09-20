package com.fushuhealth.recovery.device.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailVo {

    private Integer id;

    private String phone;

    private String email;

    private String name;

    private String avatar;
}
