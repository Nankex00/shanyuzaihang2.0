package com.fushuhealth.recovery.h5.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.ReportType;
import com.fushuhealth.recovery.common.constant.ServiceConstants;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.SysDeptMapper;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.entity.SysRoleDept;
import com.fushuhealth.recovery.common.constant.Dict;
import com.fushuhealth.recovery.h5.service.ISysDeptService;
import com.fushuhealth.recovery.h5.service.ISysUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysUserMapper sysUserMapper;




    @Override
    public String selectDeptNameById(Long institutionId) {
        return Optional.ofNullable(sysDeptMapper.selectById(institutionId)).orElseThrow(() ->
                new ServiceException("数据异常，不存在对应机构")).getDeptName();
    }



    @Override
    public ReportType getReportType(long id) {
        SysDept organization = sysDeptMapper.selectById(id);
        if (organization == null) {
            return ReportType.SIMPLE;
        }
        //todo:默认为简约版吧
//        ReportType reportType = ReportType.getReportType(organization.getReportType());
//        if (reportType == ReportType.UNKNOWN) {
//            reportType = ReportType.SIMPLE;
//        }
        ReportType reportType = ReportType.SIMPLE;
        return reportType;
    }
}
