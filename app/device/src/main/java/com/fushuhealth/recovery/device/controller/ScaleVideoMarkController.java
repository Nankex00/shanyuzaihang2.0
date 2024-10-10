package com.fushuhealth.recovery.device.controller;

import com.alibaba.fastjson.JSON;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.device.model.response.ListScaleVideoMarkResponse;
import com.fushuhealth.recovery.device.model.vo.VideoMarkParam;
import com.fushuhealth.recovery.device.service.ScaleVideoMarkService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ScaleVideoMarkController extends BaseController {

    @Autowired
    private ScaleVideoMarkService scaleVideoMarkService;

    private static String videoMarkParam;

    @Value("${scale-table.video-mark-param:}")
    public void setVideoMarkParam(String filePath) {
        videoMarkParam = filePath;
    }

    @GetMapping("/list")
    public OldBaseResponse listScaleVideoMarks(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                               @RequestParam(value = "recordId") Long recordId,
                                               @RequestParam(value = "fileId") Long videoId) {

//        LoginVo user = AuthContext.getUser();
        ListScaleVideoMarkResponse response = scaleVideoMarkService.listScaleVideoMarks(pageNo, pageSize, recordId, 0, videoId);
        return OldBaseResponse.success(response);
    }
//
//    @PostMapping("/create")
//    public BaseResponse createScaleVideoMark(@RequestBody List<CreateScaleVideoMarkRequest> request) {
//        LoginVo user = AuthContext.getUser();
//        scaleVideoMarkService.createScaleVideoMark(user.getId(), request);
//        return BaseResponse.success();
//    }
//
//    @PostMapping("/update")
//    public BaseResponse updateScaleVideoMark(@RequestBody UpdateScaleVideoMarkRequest request) {
//        LoginVo user = AuthContext.getUser();
//        scaleVideoMarkService.updateScaleVideoMark(user.getId(), request);
//        return BaseResponse.success();
//    }
//
//    @GetMapping("/delete")
//    public BaseResponse deleteScaleVideoMark(@RequestParam(value = "id") Long id) {
//        LoginVo user = AuthContext.getUser();
//        scaleVideoMarkService.deleteScaleVideoMark(id);
//        return BaseResponse.success();
//    }

    @GetMapping("/params")
    public OldBaseResponse getScaleVideoMarkParam() {
        File file = new File(videoMarkParam);
        try {
            final String content = FileUtils.readFileToString(file, "UTF-8");
            List<VideoMarkParam> videoMarkParams = JSON.parseArray(content, VideoMarkParam.class);
            return OldBaseResponse.success(videoMarkParams);
        } catch (Exception e) {
            return OldBaseResponse.success(new ArrayList<>());
        }


    }
}
