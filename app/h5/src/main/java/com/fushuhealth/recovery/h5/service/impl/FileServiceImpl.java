package com.fushuhealth.recovery.h5.service.impl;

import cn.hutool.http.HttpUtil;
import com.fushuhealth.recovery.common.storage.FileStorage;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.storage.TempFileStorage;
import com.fushuhealth.recovery.dal.dao.FilesDao;
import com.fushuhealth.recovery.dal.entity.Files;
import com.fushuhealth.recovery.h5.service.FileService;
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
    public String getFileUrl(OldFileType fileType, String filePath, boolean origin) {
        return fileStorage.getAccessibleUrl(fileType, filePath, origin);
    }

    @Override
    public String getFileUrl(long fileId, boolean origin) {
        Files file = getFile(fileId);
        return fileStorage.getAccessibleUrl(OldFileType.getType(file.getFileType()), file.getFilePath(), origin);
    }


    @Override
    public Files getFile(long fileId) {
        return filesDao.selectById(fileId);
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
    public File getTempFile() {
        return tempFileStorage.randomFile();
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
}
