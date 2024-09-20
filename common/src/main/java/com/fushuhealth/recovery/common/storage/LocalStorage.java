package com.fushuhealth.recovery.common.storage;

import com.fushuhealth.recovery.common.util.PathUtil;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;


@Data
@Component
@ConfigurationProperties(prefix = "storage.local")
@ConditionalOnProperty(value = "storage.mode", havingValue = "local")
public class LocalStorage implements InitializingBean, Storage {

    private String root;

    private String endpoint;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(root, "storageRoot is null");
        Assert.notNull(endpoint, "endpoint is null");

        File file = new File(root);
        if (!file.exists()) {
            file.mkdirs();
        }

        File tmpRoot = new File(this.getTmpRoot());
        if (!tmpRoot.exists()) {
            tmpRoot.mkdirs();
        }
    }

    @Override
    public InputStream getFile(String filePath) throws Exception {
        File file = new File(this.root, filePath);
        return FileUtils.openInputStream(file);
    }

    @Override
    public void getFile(String filePath, File destFile) throws Exception {
        File srcFile = new File(this.root, filePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    @Override
    public void putFile(File file, String destFilePath) throws Exception {
        File destFile = new File(this.root, destFilePath);
        FileUtils.copyFile(file, destFile);
    }

    @Override
    public void putFileContent(String content, String destFilePath) throws Exception {
        File destFile = new File(this.root, destFilePath);
        FileUtils.writeStringToFile(destFile, content, Charset.defaultCharset());
    }

    @Override
    public void copyFile(String srcFilePath, String destFilePath) throws Exception {
        File srcFile = new File(this.root, srcFilePath);
        File destFile = new File(this.root, destFilePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    @Override
    public boolean checkFileExists(String filePath) throws Exception {
        return new File(this.root, filePath).exists();
    }

    @Override
    public void deleteFile(String filePath) throws Exception {
        FileUtils.deleteQuietly(new File(this.root, filePath));
    }

    @Override
    public void putBytes(byte[] bytes, String destFilePath) throws Exception {
        File file = new File(this.root, destFilePath);
        FileUtils.writeByteArrayToFile(file, bytes);
    }

    @Override
    public byte[] getBytes(String destFilePath) throws Exception {
        File file = new File(this.root, destFilePath);
        return FileUtils.readFileToByteArray(file);
    }

    @Override
    public boolean isLocalMode() {
        return true;
    }

    @Override
    public String getAbsolutePath(String path) {
        return PathUtil.concatPaths(this.root, path);
    }

    @Override
    public String putFile(MultipartFile file, String destFilePath) throws Exception {
        String absolutePath = getAbsolutePath(destFilePath);
        File destFile = new File(absolutePath);
        FileUtils.forceMkdirParent(destFile);
        file.transferTo(destFile);
        return absolutePath;
    }

    @Override
    public void moveFile(String originPath, String destPath) throws Exception {
        File originFile = new File(this.root, originPath);
        File destFile = new File(this.root, destPath);
        FileUtils.moveFile(originFile, destFile);
    }

    @Override
    public String getTmpRoot() {
        return PathUtil.concatPaths(this.root, "tmp");
    }

    @Override
    public String getFileUrl(String filePath) {
        return PathUtil.concatPaths(endpoint, filePath);
    }
}
