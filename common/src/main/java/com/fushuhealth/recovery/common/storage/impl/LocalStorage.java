package com.fushuhealth.recovery.common.storage.impl;

import com.fushuhealth.recovery.common.entity.dto.FileInfoDto;
import com.fushuhealth.recovery.common.util.PathUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.WillCloseWhenClosed;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.fushuhealth.recovery.common.util.Asserts.assertStringNotBlank;

@Component
@ConfigurationProperties(prefix = "storage.local")
@ConditionalOnProperty(value = "storage.mode", havingValue = "local", matchIfMissing = true)
public class LocalStorage implements InitializingBean, Storage {

    @Value(value = "${storage.local.root:''}")
    private String storageRoot;

    @Value(value = "${storage.local.file-url-base:''}")
    private String fileUrlBase;

    @Value(value = "${storage.local.endpoint:''}")
    private String endpoint;

    @Override
    public void afterPropertiesSet() {
        assertStringNotBlank(storageRoot, fileUrlBase);
        assertStringNotBlank(endpoint);
    }

    @Override
    public InputStream getFile(String type, String filePath) throws Exception {
        String root = getFileTypeRoot(type);
        File file = new File(root, filePath);
        return FileUtils.openInputStream(file);
    }

    @Override
    public void getFile(String type, String filePath, File destFile) throws Exception {
        String root = getFileTypeRoot(type);
        File srcFile = new File(root, filePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    @Override
    public void putFile(String type, File file, String destFilePath) throws Exception {
        String root = getFileTypeRoot(type);
        File destFile = new File(root, destFilePath);
        FileUtils.copyFile(file, destFile);
    }

    @Override
    public void putFileContent(String type, String content, String destFilePath) throws Exception {
        String root = getFileTypeRoot(type);
        File destFile = new File(root, destFilePath);
        FileUtils.writeStringToFile(destFile, content, Charset.defaultCharset());
    }

    @Override
    public void copyFile(String type, String srcFilePath, String destFilePath) throws Exception {
        String root = getFileTypeRoot(type);
        File srcFile = new File(root, srcFilePath);
        File destFile = new File(root, destFilePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    @Override
    public void copyFile(String srcType, String srcFilePath, String destType, String destFilePath) throws Exception {
        String srcRoot = getFileTypeRoot(srcType);
        String destRoot = getFileTypeRoot(destType);
        File srcFile = new File(srcRoot, srcFilePath);
        File destFile = new File(destRoot, destFilePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    @Override
    public boolean checkFileExists(String type, String filePath) throws Exception {
        String root = getFileTypeRoot(type);
        return new File(root, filePath).exists();
    }

    @Override
    public void deleteFile(String type, String filePath) throws Exception {
        String root = getFileTypeRoot(type);
        FileUtils.deleteQuietly(new File(root, filePath));
    }

    @Override
    public void putBytes(String type, byte[] bytes, String destFilePath) throws Exception {
        String root = getFileTypeRoot(type);
        File file = new File(root, destFilePath);
        FileUtils.writeByteArrayToFile(file, bytes);
    }

    @Override
    public void putStream(String type, @WillCloseWhenClosed InputStream inputStream, String destFilePath) throws Exception {
        File file = new File(getFileTypeRoot(type), destFilePath);
        FileUtils.forceMkdir(file.getParentFile());
        try (OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copyLarge(inputStream, outputStream, new byte[1024 * 8]);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public byte[] getBytes(String type, String destFilePath) throws Exception {
        String root = getFileTypeRoot(type);
        File file = new File(root, destFilePath);
        return FileUtils.readFileToByteArray(file);
    }

    @Override
    public FileInfoDto fetchFile(String url, String type, String filePath) {
        return null;
    }

    @Override
    public boolean isLocalMode() {
        return true;
    }

    @Override
    public String getFileUrl(String type, String filePath, boolean origin) {
        return PathUtil.concatPaths(fileUrlBase, type, filePath);
    }

    @Override
    public String getLongExpiredFileUrl(String type, String filePath, boolean origin) {
        return PathUtil.concatPaths(fileUrlBase, type, filePath);
    }

    @Override
    public String getFileUrl(String filePath) {
        return null;
    }

    @Override
    public String getFilePath(String type, String filePath) {
        return PathUtil.concatPaths(fileUrlBase, type, filePath);
    }

    @Override
    public String getUploadToken(String type, String filePath) {
        return "";
    }

    //TODO FIX ME
    @Override
    public String moveFile(String path, String destType, String destFilePath) throws Exception{
        FileUtils.moveFile(new File(path), new File(destFilePath));
        return "";
    }

    @Override
    public boolean addWaterMark(String bucket, String key) {
        return true;
    }

    @Override
    public boolean convertToM3u8(String bucket, String key) {
        return true;
    }

    @Override
    public boolean rotateVideo(String type, String key, String newKey, Integer degree) {
        return false;
    }

    private String getFileTypeRoot(String type) {
        return PathUtil.concatPaths(this.storageRoot, type);
    }

    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String storageRoot) {
        this.storageRoot = storageRoot;
    }

    public String getFileUrlBase() {
        return fileUrlBase;
    }

    public void setFileUrlBase(String fileUrlBase) {
        this.fileUrlBase = fileUrlBase;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
