package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.Enable;
import com.fushuhealth.recovery.common.error.ServiceException;
import com.fushuhealth.recovery.common.util.MD5Util;
import com.fushuhealth.recovery.dal.dao.UserDao;
import com.fushuhealth.recovery.dal.entity.User;
import com.fushuhealth.recovery.device.model.vo.UserDetailVo;
import com.fushuhealth.recovery.device.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveUser(User user) {

    }

    @Override
    public UserDetailVo userLogin(String phone, String password, HttpServletResponse response) {

        User userQuery = User.builder().phone(phone).build();

        User user = userDao.selectOne(new QueryWrapper<>(userQuery));
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_FOUND);
        }
        String inputPass = MD5Util.getMD5String(password + user.getSaltKey());
        if (!user.getPassword().equals(inputPass)) {
            throw new ServiceException(ResultCode.PASSWORD_ERROR);
        }
        if (!Enable.isEnable(user.getEnable())) {
            throw new ServiceException(ResultCode.USER_DISABLED);
        }
        return convertToVo(user);
    }

    @Override
    public UserDetailVo findUser(Integer id) {
        User user = userDao.selectById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_FOUND);
        }
        if (!Enable.isEnable(user.getEnable())) {
            throw new ServiceException(ResultCode.USER_DISABLED);
        }
        return convertToVo(user);
    }

    private UserDetailVo convertToVo(User user) {
        return this.modelMapper.map(user, UserDetailVo.class);
    }
}
