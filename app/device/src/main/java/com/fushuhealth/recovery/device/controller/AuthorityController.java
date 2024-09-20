package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.device.config.AppProps;
import com.fushuhealth.recovery.device.model.request.LoginRequest;
import com.fushuhealth.recovery.device.model.response.LoginSuccessResponse;
import com.fushuhealth.recovery.device.model.vo.UserDetailVo;
import com.fushuhealth.recovery.device.security.Sessions;
import com.fushuhealth.recovery.device.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthorityController {

    @Autowired
    private AppProps appProps;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest request,
                              HttpServletResponse response) {

        UserDetailVo userDetail = userService.userLogin(request.getPhone(), request.getPassword(), response);

        final String token = Sessions.loginUser(String.valueOf(userDetail.getId()), appProps.getSigningSecret());
        return BaseResponse.success(LoginSuccessResponse.builder()
                .token(token)
                .user(userDetail)
                .build());
    }

//    @RequestMapping(value = "/logout", method = {RequestMethod.POST, RequestMethod.GET})
//    public BaseResponse logout(HttpServletResponse response) {
//        Sessions.logout(appProps.getRecaptchaPublic(), response);
//        return BaseResponse.success();
//    }

}
