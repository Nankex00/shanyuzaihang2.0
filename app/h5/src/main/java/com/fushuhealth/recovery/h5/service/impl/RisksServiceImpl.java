package com.fushuhealth.recovery.h5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.dal.dao.ChildrenRisksMapper;
import com.fushuhealth.recovery.dal.dao.RisksMapper;
import com.fushuhealth.recovery.dal.entity.ChildrenRisks;
import com.fushuhealth.recovery.dal.entity.Risks;
import com.fushuhealth.recovery.h5.service.IRisksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Service
public class RisksServiceImpl extends ServiceImpl<RisksMapper, Risks> implements IRisksService {
    @Autowired
    private RisksMapper risksMapper;
    @Autowired
    private ChildrenRisksMapper childrenRisksMapper;

    @Override
    public String RisksExChanged(List<Long> ids){
        StringJoiner stringJoiner = new StringJoiner(",");
        ids.forEach((riskId)->{
            String name = Optional.ofNullable(risksMapper.selectById(riskId)).orElseThrow(() -> new ServiceException("数据异常，不存在对应的风险数据"))
                    .getName();
            stringJoiner.add(name);
        });
        return stringJoiner.toString();
    }

    @Override
    public String RisksExChanged(Long childId,Byte type){
        LambdaQueryWrapper<ChildrenRisks> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChildrenRisks::getChildId,childId)
                .eq(ChildrenRisks::getType,type);
        List<Long> ids = childrenRisksMapper.selectList(lambdaQueryWrapper).stream().map(ChildrenRisks::getRiskId).toList();
        StringJoiner stringJoiner = new StringJoiner(",");
        ids.forEach((riskId)->{
            String name = Optional.ofNullable(risksMapper.selectById(riskId)).orElseThrow(() -> new ServiceException("数据异常，不存在对应的风险数据"))
                    .getName();
            stringJoiner.add(name);
        });
        return stringJoiner.toString();
    }
}
