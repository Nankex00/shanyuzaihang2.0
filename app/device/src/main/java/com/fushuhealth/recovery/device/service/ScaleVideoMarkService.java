package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.device.model.request.CreateScaleVideoMarkRequest;
import com.fushuhealth.recovery.device.model.request.UpdateScaleVideoMarkRequest;
import com.fushuhealth.recovery.device.model.response.ListScaleVideoMarkResponse;

import java.util.List;

public interface ScaleVideoMarkService {

    ListScaleVideoMarkResponse listScaleVideoMarks(int pageNo, int pageSize, long recordId, int questionId, long fileId);

    void deleteScaleVideoMark(long id);

//    void createScaleVideoMark(long userId, List<CreateScaleVideoMarkRequest> request);

    void updateScaleVideoMark(long userId, UpdateScaleVideoMarkRequest request);
}
