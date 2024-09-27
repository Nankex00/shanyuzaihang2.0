package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.device.model.request.ChildrenRequest;
import com.fushuhealth.recovery.device.model.response.ChildrenResponse;
import com.fushuhealth.recovery.device.service.IChildrenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@Service
public class ChildrenServiceImpl implements IChildrenService {
    @Autowired
    private ChildrenMapper childrenMapper;
    @Override
    public BaseResponse<List<ChildrenResponse>> searchList(ChildrenRequest request) {
        boolean flag = StringUtils.isNotBlank(request.getQuery());
        LambdaQueryWrapper<Children> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Children::getDangerLevel,request.getDangerLevel().getDangerLevel())
                .like(flag,Children::getId,request.getQuery())
                .or()
                .like(flag,Children::getName,request.getQuery());
        childrenMapper.selectList(lambdaQueryWrapper);
        return null;
    }
}
