package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.DangerLevelType;
import com.fushuhealth.recovery.common.constant.Gender;
import com.fushuhealth.recovery.common.constant.RiskType;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.DataTypeUtils;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.DiagnoseRecordMapper;
import com.fushuhealth.recovery.dal.dao.RisksMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.DiagnoseRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.vo.ChildrenVo;
import com.fushuhealth.recovery.device.model.dto.ChildrenDetailDto;
import com.fushuhealth.recovery.device.model.dto.DiagnoseRecordDto;
import com.fushuhealth.recovery.device.model.request.ChildrenRequest;
import com.fushuhealth.recovery.device.model.request.HighRiskChildrenRequest;
import com.fushuhealth.recovery.device.model.response.ChildrenDetail;
import com.fushuhealth.recovery.device.model.response.ChildrenResponse;
import com.fushuhealth.recovery.device.service.IChildrenService;
import com.fushuhealth.recovery.device.service.IRisksService;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

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
    @Autowired
    private IRisksService risksService;

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
        //查询对应的儿童信息
        MPJLambdaWrapper<Children> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(SysDept::getDeptName, ChildrenDetailDto::getDeptName)
                .eq(Children::getId,id)
                .leftJoin(SysDept.class,SysDept::getDeptId,Children::getDeptId);
        ChildrenDetailDto childrenDetailDto = Optional.ofNullable(childrenMapper.selectJoinOne(ChildrenDetailDto.class, lambdaWrapper)).orElseThrow(() -> new ServiceException("参数错误，没有对应的儿童信息"));
        List<Long> dangerOfChild = DataTypeUtils.IntegerToLongTypeHandler(childrenDetailDto.getDangerOfChild());
        List<Long> dangerOfMother = DataTypeUtils.IntegerToLongTypeHandler(childrenDetailDto.getDangerOfMother());
        //根据儿童信息中的列表为高危因素属性赋值
        ChildrenDetail childrenDetail = BeanUtil.copyProperties(childrenDetailDto, ChildrenDetail.class);
        childrenDetail.setDangerOfMother(risksService.RisksExChanged(id, RiskType.MOTHER_RISK.getType()));
        childrenDetail.setDangerOfChild(risksService.RisksExChanged(id, RiskType.CHILD_RISK.getType()));
        //加入最近的诊断记录
        MPJLambdaWrapper<DiagnoseRecord> lambdaQueryWrapper = new MPJLambdaWrapper<>();
       lambdaQueryWrapper
               .selectAll(DiagnoseRecord.class)
               .eq(DiagnoseRecord::getChildId,id)
               .last("limit 1");
        Optional.ofNullable(diagnoseRecordMapper.selectJoinOne(DiagnoseRecordDto.class,lambdaQueryWrapper)).ifPresentOrElse(
                diagnoseRecord1 -> childrenDetail.setDiagnoseList(DataTypeUtils.IntegerToLongTypeHandler(diagnoseRecord1.getDiagnoseDetail()))
        ,()->childrenDetail.setDiagnoseList(null));
        return childrenDetail;
    }

    @Override
    public BaseResponse<List<ChildrenResponse>> searchListHighRisk(HighRiskChildrenRequest request) {
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
        List<Long> risks = request.getRisks();
        if ((risks != null) && !risks.isEmpty()) {
            lambdaQueryWrapper.nested(qw -> {
                for (int i = 0; i < risks.size(); i++) {
                    if (i > 0) {
                        qw.or();
                    }
                    qw.apply("JSON_CONTAINS(danger_of_mother, '" + risks.get(i) + "')");
                }
            }).or(qw -> {
                for (int i = 0; i < risks.size(); i++) {
                    if (i > 0) {
                        qw.or();
                    }
                    qw.apply("JSON_CONTAINS(danger_of_child, '" + risks.get(i) + "')");
                }
            });
        }

        lambdaQueryWrapper
                .like(flag, Children::getId, request.getQuery())
                .or()
                .like(flag, Children::getName, request.getQuery())
                .leftJoin(SysDept.class, SysDept::getDeptId, Children::getDeptId);

        List<ChildrenResponse> responses = childrenMapper.selectJoinPage(childrenPage, ChildrenResponse.class, lambdaQueryWrapper).getRecords();

        return new BaseResponse<>(responses, childrenPage.getTotal());
    }

    @Override
    public Children getChildrenById(long id) {
        return childrenMapper.selectById(id);
    }

    @Override
    public ChildrenVo getChildren(Long id) {
        Children children = getChildrenById(id);
        if (children != null) {
            return convertToChildrenVo(children);
        }
        return null;
    }


    private ChildrenVo convertToChildrenVo(Children children) {
        ChildrenVo vo = new ChildrenVo();
        vo.setBirthday(DateUtil.getYMD(children.getBirthday()));
        vo.setBirthdayWeight(children.getBirthWeight());
        vo.setGender(Gender.getGender(children.getGender()).getDesc());
        vo.setGestationalWeek(children.getGestationalWeeks());
        vo.setGestationalWeekDay(children.getGestationalWeekDay());
        vo.setId(children.getId());
        vo.setName(children.getName());
        vo.setChildRisks(Arrays.asList(risksService.RisksExChanged(children.getId(), RiskType.CHILD_RISK.getType()).split(",")));
        vo.setMotherRisks(Arrays.asList(risksService.RisksExChanged(children.getId(), RiskType.MOTHER_RISK.getType()).split(",")));
        vo.setAge(OldDateUtil.getAge(children.getBirthday()));
        String correctAge = "";
        long week = 0;
        long day = 0;
        long days = DateUtil.getDaysBetweenTime(DateUtil.getCurrentTimeStamp(), children.getBirthday());
        if (children.getGestationalWeeks() >= 37) {
            week = days / 7;
            day = days % 7;
            correctAge = week + "周" + day + "天";
        } else {
            week = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) / 7;
            day = (days - (280 - children.getGestationalWeeks() * 7 - children.getGestationalWeekDay())) % 7;
            correctAge = week + "周" + day + "天";
        }
        vo.setCorrectAge(correctAge);
        vo.setBirthdayDate(children.getBirthday());
        vo.setMedicalCardNumber(children.getMedicalCardNumber());
        vo.setContactPhone(children.getContactPhone());
        vo.setExtraRisks(children.getExtraRisks());
        vo.setParity(children.getParity());
        vo.setAsphyxia(children.getAsphyxia());
        vo.setHearingScreening(children.getHearingScreening());
        vo.setDeformity(children.getDeformity());
        vo.setFeedingWay(children.getFeedingWay());
        return vo;
    }

}
