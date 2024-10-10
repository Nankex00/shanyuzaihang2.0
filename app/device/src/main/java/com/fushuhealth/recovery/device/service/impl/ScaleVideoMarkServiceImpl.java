package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.dal.dao.ScaleVideoMarkDao;
import com.fushuhealth.recovery.dal.entity.ScaleVideoMark;
import com.fushuhealth.recovery.device.model.request.UpdateScaleVideoMarkRequest;
import com.fushuhealth.recovery.device.model.response.ListScaleVideoMarkResponse;
import com.fushuhealth.recovery.device.model.vo.PageVo;
import com.fushuhealth.recovery.device.service.ScaleVideoMarkService;
import com.fushuhealth.recovery.web.model.vo.ScaleVideoMarkListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScaleVideoMarkServiceImpl implements ScaleVideoMarkService {

    @Autowired
    private ScaleVideoMarkDao scaleVideoMarkDao;

//    @Autowired
//    private ScaleRecordService scaleRecordService;

    @Override
    public ListScaleVideoMarkResponse listScaleVideoMarks(int pageNo, int pageSize, long recordId, int questionId, long fileId) {
        Page<ScaleVideoMark> page = new Page<>(pageNo, pageSize);
        QueryWrapper<ScaleVideoMark> wrapper = new QueryWrapper<>();
        wrapper.eq("record_id", recordId);
//        wrapper.eq("scale_question_sn", questionId);
        wrapper.eq("file_id", fileId);
        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
        wrapper.orderByAsc("created");
        page = scaleVideoMarkDao.selectPage(page, wrapper);
        List<ScaleVideoMark> records = page.getRecords();
        List<ScaleVideoMarkListVo> collect = records.stream().map(record -> convertToScaleVideoMarkListVo(record)).collect(Collectors.toList());
        PageVo pageVo = PageVo.builder()
                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
        return new ListScaleVideoMarkResponse(pageVo, collect);
    }

    @Override
    public void deleteScaleVideoMark(long id) {
        ScaleVideoMark scaleVideoMark = getScaleVideoMark(id);
        scaleVideoMark.setStatus(BaseStatus.DELETE.getStatus());
        scaleVideoMark.setUpdated(DateUtil.getCurrentTimeStamp());
        scaleVideoMarkDao.updateById(scaleVideoMark);
    }

//    @Override
//    public void createScaleVideoMark(long userId, List<CreateScaleVideoMarkRequest> request) {
//        if (CollectionUtils.isNotEmpty(request)) {
//            CreateScaleVideoMarkRequest markRequest = request.get(0);
//            ScaleEvaluationRecord scaleEvaluationRecord = scaleRecordService.getScaleEvaluationRecord(markRequest.getRecordId());
//            for (CreateScaleVideoMarkRequest rq : request) {
//                if (rq.getId() == 0) {
//                    ScaleVideoMark mark = new ScaleVideoMark();
//                    mark.setUpdated(DateUtil.getCurrentTimeStamp());
//                    mark.setStatus(BaseStatus.NORMAL.getStatus());
//                    mark.setCreated(DateUtil.getCurrentTimeStamp());
//                    mark.setDoctorId(userId);
//                    mark.setEndTime(rq.getEndTime());
//                    mark.setRecordId(scaleEvaluationRecord.getId());
//                    mark.setScaleQuestionSn(rq.getQuestionId());
//                    mark.setFileId(rq.getFileId());
//                    mark.setSource((byte) 1);
//                    mark.setScaleTableCode(scaleEvaluationRecord.getScaleTableCode());
//                    mark.setStartTime(rq.getStartTime());
//                    mark.setTag(rq.getTag());
//                    scaleVideoMarkDao.insert(mark);
//                } else {
//                    ScaleVideoMark scaleVideoMark = getScaleVideoMark(rq.getId());
//                    scaleVideoMark.setUpdated(DateUtil.getCurrentTimeStamp());
//                    scaleVideoMark.setEndTime(rq.getEndTime());
//                    scaleVideoMark.setScaleTableCode(scaleEvaluationRecord.getScaleTableCode());
//                    scaleVideoMark.setStartTime(rq.getStartTime());
//                    scaleVideoMark.setTag(rq.getTag());
//                    scaleVideoMarkDao.updateById(scaleVideoMark);
//                }
//            }
//        }
//    }

    @Override
    public void updateScaleVideoMark(long userId, UpdateScaleVideoMarkRequest request) {
        ScaleVideoMark scaleVideoMark = getScaleVideoMark(request.getId());
        scaleVideoMark.setTag(request.getTag());
//        scaleVideoMark.setStartTime(request.getStartTime());
//        scaleVideoMark.setEndTime(request.getEndTime());
        scaleVideoMark.setUpdated(DateUtil.getCurrentTimeStamp());
        scaleVideoMarkDao.updateById(scaleVideoMark);
    }

    private ScaleVideoMark getScaleVideoMark(long id) {
        ScaleVideoMark scaleVideoMark = scaleVideoMarkDao.selectById(id);
        if (scaleVideoMark == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }
        return scaleVideoMark;
    }

    private ScaleVideoMarkListVo convertToScaleVideoMarkListVo(ScaleVideoMark mark) {
        ScaleVideoMarkListVo vo = new ScaleVideoMarkListVo();
        vo.setEndTime(mark.getEndTime());
        vo.setId(mark.getId());
        vo.setSource(mark.getSource());
        vo.setStartTime(mark.getStartTime());
        vo.setTag(mark.getTag());
        return vo;
    }
}
