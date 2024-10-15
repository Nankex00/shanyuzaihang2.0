package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.DangerLevelType;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.constant.StatusType;
import com.fushuhealth.recovery.common.constant.WarnResultType;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.PredictWarnMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.PredictWarn;
import com.fushuhealth.recovery.device.model.request.PredictWarnRequest;
import com.fushuhealth.recovery.device.model.response.PredictWarnListResponse;
import com.fushuhealth.recovery.device.model.response.PredictWarnResponse;
import com.fushuhealth.recovery.device.model.vo.PredictWarnListVo;
import com.fushuhealth.recovery.device.service.IPredictWarnService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Service
public class PredictWarnServiceImpl implements IPredictWarnService {
    @Autowired
    private PredictWarnMapper predictWarnMapper;
    @Autowired
    private ChildrenMapper childrenMapper;
    @Override
    public BaseResponse<List<PredictWarnResponse>> searchPredictByChildId(Long id) {
        MPJLambdaWrapper<PredictWarn> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper.eq(PredictWarn::getChildId,id);
        List<PredictWarn> predictWarns = predictWarnMapper.selectList(lambdaQueryWrapper);
        List<PredictWarnResponse> predictWarnResponses = new ArrayList<>();
        predictWarns.forEach(predictWarn -> {
            PredictWarnResponse predictWarnResponse = BeanUtil.copyProperties(predictWarn, PredictWarnResponse.class);
            predictWarnResponse.setMonthAge(MonthType.findMonthByType(predictWarn.getMonthAge()));
            predictWarnResponse.setWarnStatus(StatusType.findStatusByType(predictWarn.getWarnStatus()));
            predictWarnResponse.setWarnResult(WarnResultType.findWarnResultByType(predictWarn.getWarnResult()));
            predictWarnResponses.add(predictWarnResponse);
        });
        return new BaseResponse<List<PredictWarnResponse>>(predictWarnResponses, (long) predictWarnResponses.size());
    }

    @Override
    public BaseResponse<List<PredictWarnListResponse>> searchList(PredictWarnRequest request) {
        Page<PredictWarnListVo> page = new Page<>(request.getPageNum(),request.getPageSize());
        MPJLambdaWrapper<PredictWarn> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(PredictWarn::getMonthAge, PredictWarnListVo::getMonthAge)
                .selectAs(PredictWarn::getWarnResult,PredictWarnListVo::getWarnResult)
                .selectAs(PredictWarn::getSubmitTime,PredictWarnListVo::getSubmitTime)
                .leftJoin(Children.class,Children::getId,PredictWarn::getChildId);
        Byte type = request.getType() != null ? request.getType() : 0; // 默认值为 0，根据需要修改
        lambdaWrapper.eq(type != 0, Children::getDangerLevel, type);
        if (request.getQuery()!=null&& !request.getQuery().isEmpty()){
            lambdaWrapper.like(Children::getId,request.getQuery())
                    .or()
                    .like(Children::getName,request.getQuery());
        }
        lambdaWrapper.orderByDesc(PredictWarn::getSubmitTime);
        predictWarnMapper.selectJoinPage(page,PredictWarnListVo.class, lambdaWrapper);
        List<PredictWarnListResponse> responses = new ArrayList<>();
        List<PredictWarnListVo> records = page.getRecords();
        records.forEach(predictWarnListVo -> {
            PredictWarnListResponse response = BeanUtil.copyProperties(predictWarnListVo,PredictWarnListResponse.class);
            if (predictWarnListVo.getMonthAge()==null||predictWarnListVo.getWarnResult()==null){
                throw new ServiceException("数据异常，不存在预警记录");
            }
            response.setMonthAge(MonthType.findMonthByType(predictWarnListVo.getMonthAge()));
            response.setWarnResult(WarnResultType.findWarnResultByType(predictWarnListVo.getWarnResult()));
//            response.setDangerLevel(DangerLevelType.findDangerLevelByType(predictWarnListVo.getDangerLevel()));
            responses.add(response);
        });
        return new BaseResponse<List<PredictWarnListResponse>>(responses, page.getTotal());
    }

    @Override
    public int updateStatus() {
        return predictWarnMapper.updateWarnStatusByTime(LocalDateTime.now());
    }
}
