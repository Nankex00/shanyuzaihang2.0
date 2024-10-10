package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.http.HttpUtil;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.storage.FileStorage;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.storage.TempFileStorage;
import com.fushuhealth.recovery.dal.dao.FilesDao;
import com.fushuhealth.recovery.dal.entity.Files;
import com.fushuhealth.recovery.dal.vo.UploadFileTokenVo;
import com.fushuhealth.recovery.device.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileServiceImpl implements FileService {

    private final static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FilesDao filesDao;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private TempFileStorage tempFileStorage;

    @Override
    public String getFileUrl(long fileId, boolean origin) {
        Files file = getFile(fileId);
        String path = StringUtils.isNotBlank(file.getRotatePath()) ? file.getRotatePath() : file.getFilePath();
        return fileStorage.getAccessibleUrl(OldFileType.getType(file.getFileType()), path, origin);
    }

    @Override
    public String getLongExpiredFileUrl(long fileId, boolean origin) {
        Files file = getFile(fileId);
        String path = StringUtils.isNotBlank(file.getRotatePath()) ? file.getRotatePath() : file.getFilePath();
        return fileStorage.getLongExpiredAccessibleUrl(OldFileType.getType(file.getFileType()), path, origin);
    }

    @Override
    public String getFileUrl(String filePath) {
        return fileStorage.getFileUrl(filePath);
    }

    @Override
    public String getAbsolutePath(long fileId) {
        Files file = getFile(fileId);
        if (file == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }
        return fileStorage.getFileUrl(OldFileType.getType(file.getFileType()), file.getFilePath());
    }

    @Override
    public String getFilePath(long fileId) {
        Files file = getFile(fileId);
        if (file == null) {
            throw new OldServiceException(ResultCode.PARAM_ERROR);
        }
        return fileStorage.getFilePath(OldFileType.getType(file.getFileType()), file.getFilePath());
    }

    @Override
    public void insertFiles(Files files) {
        filesDao.insert(files);
    }

    @Override
    public String saveFile(OldFileType fileType, File file, String fileName) {
        return fileStorage.putFile(fileType, file, fileName);
    }

    @Override
    public UploadFileTokenVo getUploadToken(OldFileType fileType, String ext) {
        String bucket = fileType.getName();
        String key = fileStorage.generateFilePath(ext.toLowerCase());
        String token = fileStorage.generateUploadToken(bucket, key);
        return new UploadFileTokenVo(bucket, key, token);
    }

    @Override
    public File getTempFile() {
        return tempFileStorage.touchFile();
    }

    @Override
    public File downloadFile(long fileId) {
        Files file = getFile(fileId);
        if (file == null) {
            return null;
        }
        String extension = file.getExtension();
        File tempFile = getTempFile();
        String fileUrl = getFileUrl(fileId, true);
        try {
            HttpUtil.downloadFile(fileUrl, tempFile);
        } catch (Exception e) {
            log.error("download file error :{}", e);
            return null;
        }
        return tempFile;
    }

    @Override
    public Files getFile(long fileId) {
        return filesDao.selectById(fileId);
    }

    @Override
    public String rotateVideo(long id) {
        Files file = getFile(id);
        if (file == null) {
            return "";
        }
        OldFileType type = OldFileType.getType(file.getFileType());
        if (type != OldFileType.VIDEO) {
            return "";
        }

//        File originFile = downloadFile(file.getId());


        String path = StringUtils.isNotBlank(file.getRotatePath()) ? file.getRotatePath() : file.getFilePath();
        String rotatePath = fileStorage.rotateVideo(type, path, 90);

        file.setRotatePath(rotatePath);
        filesDao.updateById(file);

        fileStorage.convertToM3u8(type.getName(), rotatePath);
        return getFileUrl(file.getId(), false);
    }
}
