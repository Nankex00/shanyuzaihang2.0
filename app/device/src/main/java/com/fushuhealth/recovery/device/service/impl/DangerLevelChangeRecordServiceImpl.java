package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.DangerLevelChangeRecordMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.DangerLevelChangeRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.request.DangerLevelRequest;
import com.fushuhealth.recovery.device.model.response.DangerLevelChangeRecordResponse;
import com.fushuhealth.recovery.device.service.IDangerLevelChangeRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Service
public class DangerLevelChangeRecordServiceImpl implements IDangerLevelChangeRecordService {
    @Autowired
    private ChildrenMapper childrenMapper;
    @Autowired
    private DangerLevelChangeRecordMapper dangerLevelChangeDetailMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeLevel(DangerLevelRequest request) {
        //todo:注意：系统自动通过预警筛查出来或者用户补充的高危因素，如果原高危等级较高（Ⅲ类最高，则不变更原有等级）未进行预警筛查
        //儿童表修改数据
        LambdaUpdateWrapper<Children> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Children::getDangerLevel,request.getDangerLevel())
                .set(Children::getUpdateBy, SecurityUtils.getUserId());
        childrenMapper.update(new Children(),lambdaUpdateWrapper);
        //记录表新增数据
        DangerLevelChangeRecord dangerLevelChangeDetail = new DangerLevelChangeRecord();
        dangerLevelChangeDetail.setReason(request.getReason());
        dangerLevelChangeDetail.setDangerLevel(request.getDangerLevel());
        dangerLevelChangeDetail.setOperatedTime(new Date());
        dangerLevelChangeDetail.setOperatedInstitution(SecurityUtils.getLoginUser().getUser().getDeptId());
        dangerLevelChangeDetail.setChildId(request.getChildId());
        return dangerLevelChangeDetailMapper.insert(dangerLevelChangeDetail);
    }

    @Override
    public BaseResponse<List<DangerLevelChangeRecordResponse>> searchChangeLevelList(Long childrenId) {
        MPJLambdaWrapper<DangerLevelChangeRecord> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(DangerLevelChangeRecord.class)
                .selectAs(SysDept::getDeptName,DangerLevelChangeRecordResponse::getOperatedInstitutionName)
                .eq(DangerLevelChangeRecord::getChildId,childrenId)
                .leftJoin(SysDept.class,SysDept::getDeptId,DangerLevelChangeRecord::getOperatedInstitution);
        List<DangerLevelChangeRecordResponse> dangerLevelChangeRecordResponses = dangerLevelChangeDetailMapper.selectJoinList(DangerLevelChangeRecordResponse.class, lambdaQueryWrapper);
        return new BaseResponse<List<DangerLevelChangeRecordResponse>>(dangerLevelChangeRecordResponses, (long) dangerLevelChangeRecordResponses.size());
    }
}
