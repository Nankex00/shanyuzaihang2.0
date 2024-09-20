package com.fushuhealth.recovery.common.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface Storage {

    InputStream getFile(String filePath) throws Exception;

    void getFile(String filePath, File destFile) throws Exception;

    void putFile(File file, String destFilePath) throws Exception;

    void putFileContent(String content, String destFilePath) throws Exception;

    void copyFile(String srcFilePath, String destFilePath) throws Exception;

    boolean checkFileExists(String filePath) throws Exception;

    void deleteFile(String filePath) throws Exception;

    String getFileUrl(String filePath);

    void putBytes(byte[] bytes, String destFilePath) throws Exception;

    byte[] getBytes(String destFilePath) throws Exception;

    boolean isLocalMode();

    String getAbsolutePath(String path);

    String putFile(MultipartFile file, String destFilePath) throws Exception;

    void moveFile(String originPath, String destPath) throws Exception;

    String getTmpRoot();
}
