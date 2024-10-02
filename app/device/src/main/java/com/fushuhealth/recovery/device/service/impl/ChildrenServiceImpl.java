package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.DangerLevelType;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.DiagnoseRecordMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.DiagnoseRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.request.ChildrenRequest;
import com.fushuhealth.recovery.device.model.request.HighRiskChildrenRequest;
import com.fushuhealth.recovery.device.model.response.ChildrenDetail;
import com.fushuhealth.recovery.device.model.response.ChildrenResponse;
import com.fushuhealth.recovery.device.service.IChildrenService;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@Service
public class ChildrenServiceImpl implements IChildrenService {
    @Autowired
    private ChildrenMapper childrenMapper;
    @Autowired
    private DiagnoseRecordMapper diagnoseRecordMapper;
    @Override
    public BaseResponse<List<ChildrenResponse>> searchList(ChildrenRequest request) {
        boolean flag = StringUtils.isNotBlank(request.getQuery());
        Page<ChildrenResponse> childrenPage = new Page<>();
        MPJLambdaWrapper<Children> lambdaQueryWrapper = new MPJLambdaWrapper<>();

        Byte type = request.getType() != null ? request.getType() : 0; // 默认值为 0，根据需要修改
        lambdaQueryWrapper
                .selectAll(Children.class)
                .selectAs(SysDept::getDeptName,ChildrenResponse::getDeptName)
                .eq(type != 0, Children::getDangerLevel, type)
                .like(flag, Children::getId, request.getQuery())
                .or()
                .like(flag, Children::getName, request.getQuery())
                .leftJoin(SysDept.class,SysDept::getDeptId,Children::getDeptId);
        List<ChildrenResponse> responses = childrenMapper.selectJoinPage(childrenPage, ChildrenResponse.class, lambdaQueryWrapper).getRecords();

        return new BaseResponse<>(responses, childrenPage.getTotal());
    }


    @Override
    public ChildrenDetail searchDetail(Long id) {
        MPJLambdaWrapper<Children> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(SysDept::getDeptName,ChildrenDetail::getDeptName)
                .eq(Children::getId,id)
                .leftJoin(SysDept.class,SysDept::getDeptId,Children::getDeptId);
        ChildrenDetail childrenDetail = Optional.ofNullable(childrenMapper.selectJoinOne(ChildrenDetail.class, lambdaWrapper)).orElseThrow(() -> new ServiceException("参数错误，没有对应的儿童信息"));
        LambdaQueryWrapper<DiagnoseRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       lambdaQueryWrapper.eq(DiagnoseRecord::getChildId,id)
               .last("limit 1");
        Optional.ofNullable(diagnoseRecordMapper.selectOne(lambdaQueryWrapper)).ifPresentOrElse(
                diagnoseRecord1 -> childrenDetail.setDiagnoseList(diagnoseRecord1.getDiagnoseDetail())
        ,()->childrenDetail.setDiagnoseList(null));
        return childrenDetail;
    }

    @Override
    public BaseResponse<List<ChildrenResponse>> searchListHighRisk(HighRiskChildrenRequest request) {
        //todo:复选标签列表结构需要更改
        boolean flag = StringUtils.isNotBlank(request.getQuery());
        Page<ChildrenResponse> childrenPage = new Page<>();
        MPJLambdaWrapper<Children> lambdaQueryWrapper = new MPJLambdaWrapper<>();

        Byte type = request.getType() != null ? request.getType() : 0; // 默认值为 0，根据需要修改

        lambdaQueryWrapper
                .selectAll(Children.class)
                .selectAs(SysDept::getDeptName, ChildrenResponse::getDeptName);

        if (type != 0) {
            lambdaQueryWrapper.eq(Children::getDangerLevel, type); // 使用 eq 查询
        } else {
            lambdaQueryWrapper.in(Children::getDangerLevel, 1, 2, 3); // 使用 in 查询
        }

        lambdaQueryWrapper
                .like(flag, Children::getId, request.getQuery())
                .or()
                .like(flag, Children::getName, request.getQuery())
                .leftJoin(SysDept.class, SysDept::getDeptId, Children::getDeptId);

        List<ChildrenResponse> responses = childrenMapper.selectJoinPage(childrenPage, ChildrenResponse.class, lambdaQueryWrapper).getRecords();

        return new BaseResponse<>(responses, childrenPage.getTotal());
    }
}
