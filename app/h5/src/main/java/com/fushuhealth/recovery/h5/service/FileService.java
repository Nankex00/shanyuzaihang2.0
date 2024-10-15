package com.fushuhealth.recovery.h5.service;

import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.dal.entity.Files;

import java.io.File;

public interface FileService {

    String getFileUrl(long fileId, boolean origin);

    String getFileUrl(OldFileType fileType, String filePath, boolean origin);

//    String getAbsolutePath(long fileId);
//
//    String getFilePath(long fileId);

    void insertFiles(Files files);

    String saveFile(OldFileType fileType, File file, String fileName);

    File getTempFile();

    File downloadFile(long fileId);

    Files getFile(long id);
//
//    UploadFileTokenVo getUploadToken(FileType fileType, String ext);
//
//    FileInfoDto getFile(String url, FileType fileType);
}
