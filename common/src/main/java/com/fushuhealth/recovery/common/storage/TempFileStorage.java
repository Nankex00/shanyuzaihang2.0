package com.fushuhealth.recovery.common.storage;

import com.fushuhealth.recovery.common.exception.StorageException;
import com.fushuhealth.recovery.common.util.PathUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component
public class TempFileStorage {

    @Value(value = "${storage.temp-storage-root}")
    private String storageRoot;

    public File touchFile() {
        String filename = getRandomFileUrl();
        File file = new File(filename);
        try {
            FileUtils.touch(file);
            return file;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    public File randomFile() {
        String filename = getRandomFileUrl();
        return new File(filename);
    }

    public File touchDir() {
        String filename = getRandomFileUrl();
        File file = new File(filename);
        try {
            FileUtils.forceMkdir(file);
            return file;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * 用于创建按照日期组织的文件夹，用于临时文件共享
     */
    public File getDayShareDir() {
        String date = FastDateFormat.getInstance("yyyy/MM/dd").format(System.currentTimeMillis());
        String path = PathUtil.concatPaths(storageRoot, date);
        File dateDir = new File(path);
        try {
            FileUtils.forceMkdir(dateDir);
            return dateDir;
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    private String getRandomFileUrl() {
        String filename = getRandomFilename();
        return PathUtil.concatPaths(storageRoot, DateFormatUtils.format(new Date(), "yyyy/MM/dd/HH"), filename);
    }

    public String getRandomFilename() {
        String rand = String.valueOf(Math.abs(new Random(System.currentTimeMillis()).nextInt()));
        String uuid = StringUtils.replace(UUID.randomUUID().toString(), "-", "");
        return StringUtils.joinWith("_", System.currentTimeMillis(), uuid, rand);

    }

    public void delete(File file) {
        FileUtils.deleteQuietly(file);
    }

    public void setStorageRoot(String storageRoot) {
        this.storageRoot = storageRoot;
    }
}
