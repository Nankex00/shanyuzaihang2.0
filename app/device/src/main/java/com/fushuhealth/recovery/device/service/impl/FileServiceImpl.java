package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.http.HttpUtil;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.constant.UploadType;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.storage.FileStorage;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.storage.TempFileStorage;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.StringUtil;
import com.fushuhealth.recovery.dal.dao.FilesDao;
import com.fushuhealth.recovery.dal.dto.FileDetailDto;
import com.fushuhealth.recovery.dal.dto.FileDto;
import com.fushuhealth.recovery.dal.entity.Files;
import com.fushuhealth.recovery.dal.vo.UploadFileTokenVo;
import com.fushuhealth.recovery.device.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public List<String> operateFile(String orgIds, List<FileDto> fileDtoList) {
        List<String> ids = new ArrayList<>();
        if (!StringUtils.isBlank(orgIds)){
            orgIds = orgIds.substring(1,(orgIds.length()-1));
            ids = Arrays.stream(orgIds.split(", ")).toList();
        }
        List<String> finalIds = new ArrayList<>(ids);
        if (CollectionUtils.isEmpty(fileDtoList)){
            return finalIds;
        }else {
            fileDtoList.forEach(org->{
                if (org.getCommendType().equals(UploadType.ADD.getType())){
                    String fileName = FilenameUtils.getName(org.getKey());
                    Files files = new Files();
                    files.setStatus(BaseStatus.NORMAL.getStatus());
                    files.setRawName(fileName);
                    files.setOriginalName(fileName);
                    files.setFileType(OldFileType.getType(org.getBucket()).getCode());
                    files.setCreated(DateUtil.getCurrentTimeStamp());
                    files.setFileSize(org.getSize());
                    files.setFilePath(org.getKey());
                    files.setExtension(FilenameUtils.getExtension(fileName));
                    files.setUpdated(DateUtil.getCurrentTimeStamp());
                    insertFiles(files);
                    //插入新的fileId
                    finalIds.add(String.valueOf(files.getId()));
                    System.out.println(finalIds);
                }else if (org.getCommendType().equals(UploadType.DETELE.getType())){
                    //不做删除的处理
                    //删除fileId
                    finalIds.remove(String.valueOf(org.getId()));
                }else {
                    throw new ServiceException("参数异常，不存在对应的类型");
                }
            });
            return finalIds;
        }

    }
}
