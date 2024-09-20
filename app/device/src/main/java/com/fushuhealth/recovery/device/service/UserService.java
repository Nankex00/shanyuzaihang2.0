package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.entity.User;
import com.fushuhealth.recovery.device.model.vo.UserDetailVo;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    void saveUser(User user);

    UserDetailVo userLogin(String phone, String password, HttpServletResponse response);

    UserDetailVo findUser(Integer id);
}
