package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.dal.dto.FileDetailDto;
import com.fushuhealth.recovery.dal.dto.FileDto;
import com.fushuhealth.recovery.dal.entity.Files;
import com.fushuhealth.recovery.dal.vo.UploadFileTokenVo;

import java.io.File;
import java.util.List;

public interface FileService {

    String getFileUrl(long fileId, boolean origin);

    String getLongExpiredFileUrl(long fileId, boolean origin);

    String getFileUrl(String filePath);

    String getAbsolutePath(long fileId);

    String getFilePath(long fileId);

    void insertFiles(Files files);

    String saveFile(OldFileType fileType, File file, String fileName);

    UploadFileTokenVo getUploadToken(OldFileType fileType, String ext);

    File getTempFile();

    File downloadFile(long fileId);

    Files getFile(long id);

    String rotateVideo(long id);

    List<String> operateFile(String orgIds, List<FileDto> fileDtoList);
}
