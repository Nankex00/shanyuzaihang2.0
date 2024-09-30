package com.fushuhealth.recovery.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.constant.RiskType;
import com.fushuhealth.recovery.dal.entity.Risks;
import com.fushuhealth.recovery.device.model.vo.RiskVo;
import com.fushuhealth.recovery.device.service.IRisksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@RestController
@RequestMapping("/risks")
public class RisksController {

    @Autowired
    private IRisksService risksService;

    //获取风险
    @GetMapping("/list")
    public AjaxResult getRisks() {
        LambdaQueryWrapper<Risks> wrapper = new QueryWrapper<Risks>().lambda().eq(Risks::getStatus, BaseStatus.NORMAL.getStatus());
        List<Risks> list = risksService.list(wrapper);
        Map<Byte, List<Risks>> collect =
                list.stream().collect(Collectors.groupingBy(Risks::getType));

        RiskVo riskVo = new RiskVo();
        riskVo.setChildRisk(collect.get(RiskType.CHILD_RISK.getType()).stream().map(Risks::getName).collect(Collectors.toList()));
        riskVo.setMotherRisk(collect.get(RiskType.MOTHER_RISK.getType()).stream().map(Risks::getName).collect(Collectors.toList()));
        return AjaxResult.success(riskVo);
    }
}
