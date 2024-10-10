package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.dal.vo.UploadFileTokenVo;
import com.fushuhealth.recovery.device.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FileService fileService;

    @GetMapping("/token")
    public OldBaseResponse getUploadToken(@RequestParam Byte type,
                                          @RequestParam String ext) {
        OldFileType fileType = OldFileType.getType(type);
        UploadFileTokenVo token = fileService.getUploadToken(fileType, ext);
        return OldBaseResponse.success(token);
    }
}
