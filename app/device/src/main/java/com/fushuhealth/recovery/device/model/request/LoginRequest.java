package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String phone;

    private String password;
}
