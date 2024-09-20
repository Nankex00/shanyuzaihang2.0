package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.device.model.vo.UserDetailVo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginSuccessResponse {

    private UserDetailVo user;

    private String token;
}
