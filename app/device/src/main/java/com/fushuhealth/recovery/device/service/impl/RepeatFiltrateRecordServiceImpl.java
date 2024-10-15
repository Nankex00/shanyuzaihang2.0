package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.*;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.RepeatFiltrateRecordMapper;
import com.fushuhealth.recovery.dal.dto.FileDetailDto;
import com.fushuhealth.recovery.dal.dto.FileDto;
import com.fushuhealth.recovery.dal.entity.*;
import com.fushuhealth.recovery.device.model.dto.RepeatFiltrateListDto;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateListRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateRecordRequest;
import com.fushuhealth.recovery.device.model.response.PredictWarnListResponse;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateListResponse;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordDetail;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordResponse;
import com.fushuhealth.recovery.device.model.vo.PredictWarnListVo;
import com.fushuhealth.recovery.device.service.FileService;
import com.fushuhealth.recovery.device.service.IRepeatFiltrateRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Service
public class RepeatFiltrateRecordServiceImpl implements IRepeatFiltrateRecordService {
    @Autowired
    private RepeatFiltrateRecordMapper repeatFiltrateRecordMapper;
    @Autowired
    private ChildrenMapper childrenMapper;
    @Autowired
    private FileService fileService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRepeatFiltrateRecord(RepeatFiltrateRecordRequest request) {
        RepeatFiltrateRecord repeatFiltrateRecord = BeanUtil.copyProperties(request,RepeatFiltrateRecord.class);
        List<Long> aqsIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(request.getAqs())){
            request.getAqs().forEach((aqs)->{
                String fileName = FilenameUtils.getName(aqs.getKey());
                Files files = new Files();
                files.setStatus(BaseStatus.NORMAL.getStatus());
                files.setRawName(fileName);
                files.setOriginalName(fileName);
                files.setFileType(OldFileType.getType(aqs.getBucket()).getCode());
                files.setCreated(DateUtil.getCurrentTimeStamp());
                files.setFileSize(aqs.getSize());
                files.setFilePath(aqs.getKey());
                files.setExtension(FilenameUtils.getExtension(fileName));
                files.setUpdated(DateUtil.getCurrentTimeStamp());
                fileService.insertFiles(files);
                aqsIds.add(files.getId());
            });
        }
        List<Long> ddstIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(request.getDdst())){
            request.getDdst().forEach((ddst)->{
                String fileName = FilenameUtils.getName(ddst.getKey());
                Files files = new Files();
                files.setStatus(BaseStatus.NORMAL.getStatus());
                files.setRawName(fileName);
                files.setOriginalName(fileName);
                files.setFileType(OldFileType.getType(ddst.getBucket()).getCode());
                files.setCreated(DateUtil.getCurrentTimeStamp());
                files.setFileSize(ddst.getSize());
                files.setFilePath(ddst.getKey());
                files.setExtension(FilenameUtils.getExtension(fileName));
                files.setUpdated(DateUtil.getCurrentTimeStamp());
                fileService.insertFiles(files);
                ddstIds.add(files.getId());
            });
        }

        List<Long> otherIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(request.getOther())){
            request.getOther().forEach((other)->{
                String fileName = FilenameUtils.getName(other.getKey());
                Files files = new Files();
                files.setStatus(BaseStatus.NORMAL.getStatus());
                files.setRawName(fileName);
                files.setOriginalName(fileName);
                files.setFileType(OldFileType.getType(other.getBucket()).getCode());
                files.setCreated(DateUtil.getCurrentTimeStamp());
                files.setFileSize(other.getSize());
                files.setFilePath(other.getKey());
                files.setExtension(FilenameUtils.getExtension(fileName));
                files.setUpdated(DateUtil.getCurrentTimeStamp());
                fileService.insertFiles(files);
                otherIds.add(files.getId());
            });
        }
        repeatFiltrateRecord.setOperatedId(SecurityUtils.getUserId());
        repeatFiltrateRecord.setSubmitTime(new Date());
        repeatFiltrateRecord.setAqsUrls(aqsIds.toString());
        repeatFiltrateRecord.setDdstUrls(ddstIds.toString());
        repeatFiltrateRecord.setOtherUrls(otherIds.toString());
        return repeatFiltrateRecordMapper.insert(repeatFiltrateRecord);
    }

    @Override
    public BaseResponse<List<RepeatFiltrateRecordResponse>> searchListByChildId(Long childId) {
        MPJLambdaWrapper<RepeatFiltrateRecord> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper.selectAll(RepeatFiltrateRecord.class)
                .selectAs(SysDept::getDeptName,RepeatFiltrateRecordResponse::getOperateDept)
                .eq(RepeatFiltrateRecord::getChildId,childId)
                .eq(RepeatFiltrateRecord::getDelFlag,0)
                .leftJoin(SysUser.class,SysUser::getUserId,RepeatFiltrateRecord::getOperatedId)
                .leftJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
        List<RepeatFiltrateRecordResponse> repeatFiltrateRecordResponses = repeatFiltrateRecordMapper.selectJoinList(RepeatFiltrateRecordResponse.class, lambdaQueryWrapper);
        repeatFiltrateRecordResponses.forEach((response)->{
            response.setMonthAge(MonthType.findMonthByType(Byte.parseByte(response.getMonthAge())));
        });
        return new BaseResponse<List<RepeatFiltrateRecordResponse>>(repeatFiltrateRecordResponses, (long) repeatFiltrateRecordResponses.size());
    }

    @Override
    public RepeatFiltrateRecordDetail searchDetail(Long id) {
        RepeatFiltrateRecord repeatFiltrateRecord = repeatFiltrateRecordMapper.selectById(id);
        RepeatFiltrateRecordDetail repeatFiltrateRecordDetail = new RepeatFiltrateRecordDetail();
//                BeanUtil.copyProperties(repeatFiltrateRecord, RepeatFiltrateRecordDetail.class);
        repeatFiltrateRecordDetail.setId(repeatFiltrateRecord.getId());
        repeatFiltrateRecordDetail.setMonthAge(repeatFiltrateRecord.getMonthAge());
        repeatFiltrateRecordDetail.setAqsResult(repeatFiltrateRecord.getAqsResult());
        repeatFiltrateRecordDetail.setDdstResult(repeatFiltrateRecord.getDdstResult());
        repeatFiltrateRecordDetail.setOtherResult(repeatFiltrateRecord.getOtherResult());
        repeatFiltrateRecordDetail.setChildId(repeatFiltrateRecord.getChildId());
        String aqsUrls = repeatFiltrateRecord.getAqsUrls();
        if (aqsUrls!=null&&!aqsUrls.equals("[]")){
            aqsUrls = aqsUrls.substring(1, aqsUrls.length() - 1);
            List<FileDetailDto> aqs = new ArrayList<>();
            Arrays.stream(aqsUrls.split(", ")).forEach(ids->{
//                String filePath = fileService.getFilePath(Long.parseLong(ids));
                aqs.add(new FileDetailDto(Long.parseLong(ids),fileService.getFileUrl(Long.parseLong(ids),true)));
            });
            repeatFiltrateRecordDetail.setAqsUrls(aqs);
        }
        String ddstUrls = repeatFiltrateRecord.getDdstUrls();
        if (ddstUrls!=null&&!ddstUrls.equals("[]")){
            ddstUrls = ddstUrls.substring(1, ddstUrls.length() - 1);
            List<FileDetailDto> ddst = new ArrayList<>();
            Arrays.stream(ddstUrls.split(", ")).forEach(ids->{
                String filePath = fileService.getFilePath(Long.parseLong(ids));
                ddst.add(new FileDetailDto(Long.parseLong(ids),fileService.getFileUrl(Long.parseLong(ids),true)));
            });
            repeatFiltrateRecordDetail.setDdstUrls(ddst);
        }

        String otherUrls = repeatFiltrateRecord.getOtherUrls();
        if (otherUrls!=null&&!otherUrls.equals("[]")){
            otherUrls =otherUrls.substring(1, otherUrls.length() - 1);
            List<FileDetailDto> others = new ArrayList<>();
            Arrays.stream(otherUrls.split(", ")).forEach(ids->{
                String filePath = fileService.getFilePath(Long.parseLong(ids));
                others.add(new FileDetailDto(Long.parseLong(ids),fileService.getFileUrl(Long.parseLong(ids),true)));
            });
            repeatFiltrateRecordDetail.setOtherUrls(others);
        }
        return repeatFiltrateRecordDetail;
    }

    @Override
    public int editDetail(RepeatFiltrateEditRequest request) {
        RepeatFiltrateRecord repeatFiltrateRecord1 = Optional.ofNullable(repeatFiltrateRecordMapper.selectById(request.getId())).orElseThrow(() -> new ServiceException("数据异常，不存在对应记录"));
        List<String> aqs = fileService.operateFile(repeatFiltrateRecord1.getAqsUrls(), request.getAqs());
        List<String> ddst = fileService.operateFile(repeatFiltrateRecord1.getDdstUrls(), request.getDdst());
        List<String> others = fileService.operateFile(repeatFiltrateRecord1.getOtherUrls(), request.getOthers());
        RepeatFiltrateRecord repeatFiltrateRecord = BeanUtil.copyProperties(request, RepeatFiltrateRecord.class);
        repeatFiltrateRecord.setSubmitTime(new Date());
        repeatFiltrateRecord.setOperatedId(SecurityUtils.getUserId());
        if (CollectionUtils.isEmpty(aqs)){
            repeatFiltrateRecord.setAqsUrls("[]");
        }else {
            repeatFiltrateRecord.setAqsUrls(String.valueOf(aqs));
        }
        if (CollectionUtils.isEmpty(ddst)){
            repeatFiltrateRecord.setDdstUrls(String.valueOf(ddst));
        }else {
            repeatFiltrateRecord.setDdstUrls("[]");
        }
        if (CollectionUtils.isEmpty(others)){
            repeatFiltrateRecord.setOtherUrls(String.valueOf(others));
        }else {
            repeatFiltrateRecord.setOtherUrls("[]");
        }
        return repeatFiltrateRecordMapper.updateById(repeatFiltrateRecord);
    }

    @Override
    public int delete(Long id) {
        RepeatFiltrateRecord repeatFiltrateRecord = repeatFiltrateRecordMapper.selectById(id);
        //判断数据创建是否在一小时内
        Date submitTime = repeatFiltrateRecord.getSubmitTime();
        Date currentTime = new Date();
        Calendar submitCalendar = Calendar.getInstance();
        submitCalendar.setTime(submitTime);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentTime);
        currentCalendar.add(Calendar.HOUR, 1);
        boolean flag = currentCalendar.after(submitCalendar);
        //判断数据是否为本机构账号创建
        boolean equals = repeatFiltrateRecord.getOperatedId().equals(SecurityUtils.getUserId());
        if (!flag){
            throw new ServiceException("数据生成超过一小时，无法删除");
        }
        if (!equals){
            throw new ServiceException("数据不是本机构创建，无删除权限");
        }
        LambdaUpdateWrapper<RepeatFiltrateRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(RepeatFiltrateRecord::getId,id)
                .set(RepeatFiltrateRecord::getDelFlag,1);
        return repeatFiltrateRecordMapper.update(new RepeatFiltrateRecord(),lambdaUpdateWrapper);
    }

    @Override
    public BaseResponse<List<RepeatFiltrateListResponse>> searchDeptList(RepeatFiltrateListRequest request) {
        Page<RepeatFiltrateListDto> page = new Page<>(request.getPageNum(),request.getPageSize());
        MPJLambdaWrapper<RepeatFiltrateRecord> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAs(Children::getSex,RepeatFiltrateListDto::getSex)
                .selectAs(Children::getName,RepeatFiltrateListDto::getName)
                .selectAs(Children::getDateOfBirth,RepeatFiltrateListDto::getDateOfBirth)
                .selectAs(Children::getDangerLevel,RepeatFiltrateListDto::getDangerLevel)
                .selectAs(SysDept::getDeptName,RepeatFiltrateListDto::getDeptName)
                .innerJoin(Children.class,Children::getId,RepeatFiltrateRecord::getChildId)
                .innerJoin(SysUser.class,SysUser::getUserId,RepeatFiltrateRecord::getOperatedId)
                .innerJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
        Byte type = request.getType() != null ? request.getType() : 0; // 默认值为 0，根据需要修改
        lambdaWrapper.eq(type != 0, Children::getDangerLevel, type);
        if (request.getQuery()!=null&& !request.getQuery().isEmpty()){
            lambdaWrapper.like(Children::getId,request.getQuery())
                    .or()
                    .like(Children::getName,request.getQuery());
        }
        lambdaWrapper.orderByDesc(RepeatFiltrateRecord::getSubmitTime);
        repeatFiltrateRecordMapper.selectJoinPage(page,RepeatFiltrateListDto.class, lambdaWrapper);
        List<RepeatFiltrateListResponse> responses = new ArrayList<>();
        page.getRecords().forEach(repeatFiltrateListDto -> {
            RepeatFiltrateListResponse response = BeanUtil.copyProperties(repeatFiltrateListDto,RepeatFiltrateListResponse.class);
            response.setMonthAge(MonthType.findMonthByType(repeatFiltrateListDto.getMonthAge()));
//            response.setDangerLevel(DangerLevelType.findDangerLevelByType(repeatFiltrateListDto.getDangerLevel()));
            responses.add(response);
        });
        return new BaseResponse<List<RepeatFiltrateListResponse>>(responses, page.getTotal());
    }
}
