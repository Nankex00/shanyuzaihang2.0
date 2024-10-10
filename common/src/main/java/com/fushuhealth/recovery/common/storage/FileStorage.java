package com.fushuhealth.recovery.common.storage;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fushuhealth.recovery.common.exception.StorageException;
import com.fushuhealth.recovery.common.util.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fushuhealth.recovery.common.storage.impl.Storage;
import com.fushuhealth.recovery.common.entity.dto.FileInfoDto;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;


@Component
public class FileStorage implements InitializingBean {

    private final static Logger log = LoggerFactory.getLogger(FileStorage.class);

    private FilePathPolicy filePathPolicy = new DefaultFilePathPolicy();

    @Value(value = "${storage.local.endpoint:''}")
    private String localStorageEndpoint;
    @Value(value = "${storage.outside.endpoint:''}")
    private String outsideStorageEndpoint;

    private Storage storage;

    private TempFileStorage tempFileStorage;

    public FileStorage(Storage storage, TempFileStorage tempFileStorage) {
        this.storage = storage;
        this.tempFileStorage = tempFileStorage;
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.assertNotNull(tempFileStorage);
    }

    public InputStream getFile(OldFileType type, String filePath) {
        Asserts.assertNotNull(type, filePath);
        try {
            return storage.getFile(type.getName(), filePath);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public void getFile(OldFileType type, String filePath, File file) {
        Asserts.assertNotNull(type, filePath, file);
        if (!file.exists() || file.isDirectory()) {
            throw new StorageException("file error");
        }
        try {
            storage.getFile(type.getName(), filePath, file);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putFile(OldFileType type, File file, FilePathPolicy filePathPolicy) {
        Asserts.assertNotNull(type, file);
        if (filePathPolicy == null) {
            filePathPolicy = this.filePathPolicy;
        }
        if (!file.exists() || file.isDirectory()) {
            throw new StorageException("file error");
        }
        try {
            String filePath = filePathPolicy.generateFilePath(type, file);
            storage.putFile(type.getName(), file, filePath);
            return filePath;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putFile(OldFileType type, File file) {
        return putFile(type, file, filePathPolicy);
    }

    public String putFileContent(OldFileType type, String content) {
        Asserts.assertNotNull(type, content);
        if (StringUtils.isEmpty(content)) {
            throw new StorageException("file content error");
        }
        try {
            String filePath = filePathPolicy.generateFilePath(type);
            storage.putFileContent(type.getName(), content, filePath);
            return filePath;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String copyFile(OldFileType type, String filePath) {
        Asserts.assertNotNull(type, filePath);
        if (!checkFileExists(type, filePath)) {
            throw new StorageException("filePath error");
        }
        try {
            String newFilePath = filePathPolicy.generateFilePath(type);
            storage.copyFile(type.getName(), filePath, newFilePath);
            return newFilePath;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String copyFile(OldFileType srcType, String srcFilePath, OldFileType destType) {
        Asserts.assertNotNull(srcType, srcFilePath, destType);
        if (!checkFileExists(srcType, srcFilePath)) {
            throw new StorageException("filePath error");
        }
        try {
            String newFilePath = filePathPolicy.generateFilePath(srcType);
            storage.copyFile(srcType.getName(), srcFilePath, destType.getName(), newFilePath);
            return newFilePath;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String getFileUrl(OldFileType type, String filePath) {
        Asserts.assertNotNull(type, filePath);
        return storage.getFileUrl(type.getName(), filePath, false);
    }

    public String getFilePath(OldFileType type, String filePath) {
        return storage.getFilePath(type.getName(), filePath);
    }


    public boolean checkFileExists(OldFileType type, String filePath) {
        Asserts.assertNotNull(type, filePath);
        try {
            return storage.checkFileExists(type.getName(), filePath);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public void deleteFile(OldFileType type, String filePath) {
        Asserts.assertNotNull(type, filePath);
        try {
            storage.deleteFile(type.getName(), filePath);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    private String putInputStreamAsJSONBytes(OldFileType type, InputStream inputStream) {
        try {
            byte[] bytes = ByteUtil.inputStreamToByte(inputStream);
            return putBytes(type, bytes);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putObjectAsJSONBytes(OldFileType type, Object object) {
        Asserts.assertNotNull(type, object);
        if (object instanceof InputStream) {
            return putInputStreamAsJSONBytes(type, (InputStream) object);
        }
        try {
            byte[] bytes = bytes = JSON.toJSONBytes(object);
            return putBytes(type, bytes);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putObjectAsJSONBytes(OldFileType type, Object object, FilePathPolicy filePathPolicy) {
        Asserts.assertNotNull(type, object);
        if (object instanceof InputStream) {
            return putInputStreamAsJSONBytes(type, (InputStream) object);
        }
        try {
            byte[] bytes = bytes = JSON.toJSONBytes(object);
            return putBytes(type, bytes, filePathPolicy);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public byte[] getObjectJSONBytes(OldFileType type, String filePath) {
        Asserts.assertNotNull(type, filePath);
        try {
            return storage.getBytes(type.getName(), filePath);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putBytes(OldFileType type, byte[] bytes) {
        return putBytes(type, bytes, filePathPolicy);
    }

    public String putBytes(OldFileType type, byte[] bytes, FilePathPolicy filePathPolicy) {
        Asserts.assertNotNull(type, bytes);
        try {
            String filePath = filePathPolicy.generateFilePath(type);
            storage.putBytes(type.getName(), bytes, filePath);
            return filePath;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putFile(OldFileType type, File file, String filename) {
        Asserts.assertNotNull(type, file, filename);
        Asserts.assertStringNotBlank(filename);
        if (!file.exists() || file.isDirectory()) {
            throw new StorageException("file error");
        }
        try {
            String filePath = filePathPolicy.generateFilePath(filename);

            storage.putFile(type.getName(), file, filePath);
            return filePath;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putStream(OldFileType type, InputStream inputStream) {
        String filePath = filePathPolicy.generateFilePath(type);
        try {
            storage.putStream(type.getName(), inputStream, filePath);
            return filePath;
        } catch (Exception e) {
            throw new StorageException("put stream error.", e);
        }
    }

    public <T> T getJsonObject(OldFileType type, String path, Class<T> reference) {
        try (InputStream inputStream = openJsonStream(type, path)) {
            return JsonUtil.getMapper().readValue(inputStream, reference);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public <T> T getJsonObject(OldFileType type, String path, TypeReference<T> reference) {
        try (InputStream inputStream = openJsonStream(type, path)) {
            return JsonUtil.getMapper().readValue(inputStream, reference);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String putJsonObject(OldFileType type, Object object) {
        checkNotNull(object);

        try (ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
             OutputStream sink = byteArrayStream) {
            JsonUtil.getMapper().writeValue(sink, object);
            return putStream(type, byteArrayStream.toInputStream());
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public String generateFilePath(String ext) {
        String fileName = StringUtil.generateFileName(ext);
        return filePathPolicy.generateFilePath(fileName);
    }

    public String generateUploadToken(String bucket, String key) {
        String token = storage.getUploadToken(bucket, key);
        return token;
    }

    public String moveFile(String path, OldFileType OldFileType) {
        try {
            return storage.moveFile(path, OldFileType.getName(), path);
        } catch (Exception e) {
            log.error("move file error: {}", e);
            throw new StorageException(e);
        }
    }

    public void addWaterMark(String type, String key) {
        storage.addWaterMark(type, key);
    }

    public void convertToM3u8(String type, String key) {
        storage.convertToM3u8(type, key);
    }

    /**
     * 注意区别于getFileUrl。获取的url为绝对url,本地模式时存储的url为相对url
     *
     * @return 绝对地址的url
     * @see #getFileUrl
     */
    public String getAccessibleUrl(OldFileType OldFileType, String filePath, boolean origin) {
//        if (isLocalMode()) {
//            return localStorageEndpoint + url;
//        } else {
//            return url;
//        }
        String url = storage.getFileUrl(OldFileType.getName(), filePath, origin);
        if (isLocalMode()) {
            return PathUtil.concatPaths(outsideStorageEndpoint, url);
        }
        return url;
    }

    public String getLongExpiredAccessibleUrl(OldFileType OldFileType, String filePath, boolean origin) {
//        if (isLocalMode()) {
//            return localStorageEndpoint + url;
//        } else {
//            return url;
//        }
        String url = storage.getLongExpiredFileUrl(OldFileType.getName(), filePath, origin);
        if (isLocalMode()) {
            return PathUtil.concatPaths(outsideStorageEndpoint, url);
        }
        return url;
    }

    public String getFileUrl(String path) {
        String url = storage.getFileUrl(path);
        if (isLocalMode()) {
            return PathUtil.concatPaths(outsideStorageEndpoint, url);
        }
        return url;
    }

    public FileInfoDto fetchFileFromWx(String url, OldFileType type, String key) {
        FileInfoDto fileInfoDto = storage.fetchFile(url, type.getName(), key);
        return fileInfoDto;
    }

    private InputStream openJsonStream(OldFileType type, String path) throws Exception {
        InputStream origin = storage.getFile(type.getName(), path);
        BufferedInputStream buffer = IOUtils.buffer(origin);
        return buffer;
    }

    public boolean isLocalMode() {
        return storage.isLocalMode();
    }

    public TempFileStorage getTempFileStorage() {
        return tempFileStorage;
    }

    public String getLocalStorageEndpoint() {
        return localStorageEndpoint;
    }

    public String getOutsideStorageEndpoint() {
        return outsideStorageEndpoint;
    }

    public String rotateVideo(OldFileType type, String path, int degree) {
        String rotateName = StringUtil.getRotateName(path);
        boolean b = storage.rotateVideo(type.getName(), path, rotateName, degree);
        if (b) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return rotateName;
        }
        return "";
    }

}
