package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.dto.PositionFile;
import com.fushuhealth.recovery.dal.entity.ActionVideo;
import com.fushuhealth.recovery.dal.vo.ActionVideoVo;
import com.fushuhealth.recovery.device.model.request.SaveActionVideoRequest;
import com.fushuhealth.recovery.device.model.request.UpdateActionVideoRequest;
import com.fushuhealth.recovery.device.model.response.ListActionVideoResponse;
import com.fushuhealth.recovery.device.model.vo.LoginVo;

import java.util.List;

public interface ActionVideoService {

//    List<ActionVideoVo> listByActionId(long actionId, LoginVo loginVo);

//    void saveActionVideo(Long userId, SaveActionVideoRequest request);

    ActionVideo getById(long id);

    PositionFile getPositionFile(List<PositionFile> positionFileVos, String position);

    ListActionVideoResponse listActionVideo(int pageNo, int pageSize, long actionId, String name, byte isPublic, LoginVo loginVo);

    ActionVideoVo getActionVideoVo(long id);

    void changeActionVideoPublic(long id, byte isPublic);

    void saveActionVideo(SaveActionVideoRequest request, LoginVo loginVo);

    void updateActionVideo(UpdateActionVideoRequest request, LoginVo loginVo);

    void deleteActionVideo(long id);
}
