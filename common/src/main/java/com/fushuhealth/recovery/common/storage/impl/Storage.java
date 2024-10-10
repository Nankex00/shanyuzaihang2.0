package com.fushuhealth.recovery.common.storage.impl;

import com.fushuhealth.recovery.common.entity.dto.FileInfoDto;

import javax.annotation.WillCloseWhenClosed;
import java.io.File;
import java.io.InputStream;

public interface Storage {

    InputStream getFile(String type, String filePath) throws Exception;

    void getFile(String type, String filePath, File destFile) throws Exception;

    void putFile(String type, File file, String destFilePath) throws Exception;

    void putFileContent(String type, String content, String destFilePath) throws Exception;

    void copyFile(String type, String srcFilePath, String destFilePath) throws Exception;

    void copyFile(String srcType, String srcFilePath, String destType, String destFilePath) throws Exception;

    boolean checkFileExists(String type, String filePath) throws Exception;

    void deleteFile(String type, String filePath) throws Exception;

    String getFileUrl(String type, String filePath, boolean origin);

    String getLongExpiredFileUrl(String type, String filePath, boolean origin);

    String getFileUrl(String filePath);

    String getFilePath(String type, String filePath);

    String getUploadToken(String type, String filePath);

    String moveFile(String path, String destType, String destFilePath) throws Exception;

    boolean addWaterMark(String type, String key);

    boolean convertToM3u8(String type, String key);

    boolean rotateVideo(String type, String key, String newKey, Integer degree);

    void putBytes(String type, byte[] bytes, String destFilePath) throws Exception;

    void putStream(String type, @WillCloseWhenClosed InputStream inputStream, String destFilePath) throws Exception;

    byte[] getBytes(String type, String destFilePath) throws Exception;

    FileInfoDto fetchFile(String url, String type, String filePath);

    boolean isLocalMode();

}
