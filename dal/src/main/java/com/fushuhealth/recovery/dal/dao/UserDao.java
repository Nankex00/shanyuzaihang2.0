package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.dal.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {
}
