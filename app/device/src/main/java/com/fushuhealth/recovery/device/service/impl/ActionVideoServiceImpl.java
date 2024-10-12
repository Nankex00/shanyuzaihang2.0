package com.fushuhealth.recovery.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.constant.RoleEnum;
import com.fushuhealth.recovery.common.storage.FileStorage;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.StringUtil;
import com.fushuhealth.recovery.dal.dao.ActionVideoDao;
import com.fushuhealth.recovery.dal.dto.PositionFile;
import com.fushuhealth.recovery.dal.entity.ActionVideo;
import com.fushuhealth.recovery.dal.vo.ActionVo;
import com.fushuhealth.recovery.dal.vo.PositionFileVo;
import com.fushuhealth.recovery.device.service.ActionService;
import com.fushuhealth.recovery.device.service.ActionVideoService;
import com.fushuhealth.recovery.device.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionVideoServiceImpl implements ActionVideoService {

    private final static Logger log = LoggerFactory.getLogger(ActionVideoServiceImpl.class);

    @Autowired
    private ActionVideoDao actionVideoDao;

//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private ActionService actionService;
//
//    @Autowired
//    private FileStorage fileStorage;

//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private OrganizationService organizationService;

//    @Override
//    public List<ActionVideoVo> listByActionId(long actionId, LoginVo loginVo) {
//        QueryWrapper<ActionVideo> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus()).eq("action_id", actionId);
////        wrapper.eq();
//        List<ActionVideo> actionVideos = actionVideoDao.selectList(wrapper);
//        List<ActionVideoVo> list = actionVideos.stream().map(actionVideo -> convertToVo(actionVideo)).collect(Collectors.toList());
//        return list;
//    }

//    @Transactional
//    @Override
//    public void saveActionVideo(Long userId, SaveActionVideoRequest request) {
//        ActionVo actionVo = actionService.getActionVo(request.getActionId());
//        if (actionVo == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        String fileName = FilenameUtils.getName(request.getKey());
//        Files files = new Files();
//        files.setUpdated(DateUtil.getCurrentTimeStamp());
//        files.setExtension(FilenameUtils.getExtension(fileName));
//        files.setFilePath(request.getKey());
//        files.setFileSize(request.getSize());
//        files.setCreated(DateUtil.getCurrentTimeStamp());
//        files.setFileType(FileType.getType(request.getBucket()).getCode());
//        files.setOriginalName(fileName);
//        files.setRawName(fileName);
//        files.setStatus(BaseStatus.NORMAL.getStatus());
//        fileService.insertFiles(files);
//
//        File file = fileService.downloadFile(files.getId());
//        if (file == null) {
//            log.error("file not exist");
//            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//        }
//        VideoInfo videoInfo = null;
//        String coverName = StringUtil.uuid() + ".jpg";
//        String coverPath = "";
//        try {
//            //TODO  FIX ME
//            videoInfo = VideoTool.getVideoInfo(file.getAbsolutePath(), coverName);
//            coverPath = fileStorage.putFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//        } catch (Exception e) {
//            log.error("error:", e);
//            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//        }
//
//        Files coverFile = new Files();
//        coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//        coverFile.setExtension(FilenameUtils.getExtension(coverName));
//        coverFile.setFilePath(coverPath);
//        coverFile.setFileSize(videoInfo.getCover().length());
//        coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//        coverFile.setFileType(FileType.PICTURE.getCode());
//        coverFile.setOriginalName(coverName);
//        coverFile.setRawName(coverName);
//        coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//        fileService.insertFiles(coverFile);
//
//        ActionVideo actionVideo = new ActionVideo();
//        actionVideo.setActionId(actionVo.getId());
//        actionVideo.setCoverFileId(coverFile.getId());
//        actionVideo.setCreated(DateUtil.getCurrentTimeStamp());
//        actionVideo.setDuration(videoInfo.getDuration().longValue());
////        actionVideo.setFileId(files.getId());
//        actionVideo.setStatus(BaseStatus.NORMAL.getStatus());
//        actionVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//        actionVideo.setUserId(userId);
//        actionVideo.setVideoName(actionVo.getActionName());
//        actionVideoDao.insert(actionVideo);
//    }

    @Override
    public ActionVideo getById(long id) {
        return actionVideoDao.selectById(id);
    }

    @Override
    public PositionFile getPositionFile(List<PositionFile> positionFileVos, String position) {
        for (PositionFile positionFile : positionFileVos) {
            if (position.equalsIgnoreCase(positionFile.getPosition())) {
                return positionFile;
            }
        }
        return null;
    }
//
//    @Override
//    public ListActionVideoResponse listActionVideo(int pageNo, int pageSize, long actionId, String name, byte isPublic, LoginVo loginVo) {
//
//        Page<ActionVideo> page = new Page<>(pageNo, pageSize);
//        QueryWrapper<ActionVideo> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
////        if (isPublic != PublicStatus.All.getStatus()) {
////            wrapper.eq("is_public", isPublic);
////        }
//        if (actionId != 0) {
//            wrapper.eq("action_id", actionId);
//        } else {
//            wrapper.in("action_id", loginVo.getActionIds());
//        }
//        if (!StringUtils.isBlank(name)) {
//            wrapper.like("video_name", name);
//        }
//        List<Long> ids = doctorService.getDoctorIdByOrganizationId(loginVo.getOrganizationId());
//        if (loginVo.getRoleName().equalsIgnoreCase(RoleEnum.ROLE_MECHANISM_ADMIN.getName())) {
//            wrapper.in("user_id", ids).eq("is_public", PublicStatus.YES_MECHANISM.getStatus());
//        } else if (loginVo.getRoleName().equalsIgnoreCase(RoleEnum.ROLE_DOCTOR.getName())){
//            wrapper.and( wp -> wp.eq("user_id", loginVo.getId())
//                    .or(wpp -> wpp.in("user_id", ids).eq("is_public", PublicStatus.YES_MECHANISM.getStatus()))
//                    .or(wppp -> wppp.eq("is_public", PublicStatus.YES_PLATFORM.getStatus())));
//        }
//        wrapper.orderByDesc("id");
//        page = actionVideoDao.selectPage(page, wrapper);
//        List<ActionVideo> records = page.getRecords();
//        List<ActionVideoListVo> list = records.stream().map(record -> convertToActionVideoListVo(record)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListActionVideoResponse(pageVo, list);
//    }
//
//    @Override
//    public ActionVideoVo getActionVideoVo(long id) {
//        ActionVideo actionVideo = actionVideoDao.selectById(id);
//        return convertToActionVideoVo(actionVideo);
//    }
//
//    @Override
//    public void changeActionVideoPublic(long id, byte isPublic) {
//        ActionVideo actionVideo = actionVideoDao.selectById(id);
////        if (actionVideo.getIsPublic() == PublicStatus.NO.getStatus()) {
////            actionVideo.setIsPublic(PublicStatus.YES_MECHANISM.getStatus());
////        } else {
////            actionVideo.setIsPublic(PublicStatus.NO.getStatus());
////        }
//        actionVideo.setIsPublic(isPublic);
//        actionVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//        actionVideoDao.updateById(actionVideo);
//    }
//
//    @Transactional
//    @Override
//    public void saveActionVideo(SaveActionVideoRequest request, LoginVo loginVo) {
//        ActionVo actionVo = actionService.getActionVo(request.getActionId());
//        List<CommitUploadRequest> commits = request.getFiles();
//
//        List<PositionFile> positionFiles = new ArrayList<>();
//
//        long fileId = 0l;
//        for (CommitUploadRequest commit : commits) {
//            String key = commit.getKey();
//            fileStorage.convertToM3u8(FileType.VIDEO.getName(), key);
//            String fileName = "";
//            if (StringUtils.isNotBlank(commit.getFileName())) {
//                fileName = commit.getFileName();
//            } else {
//                fileName = FilenameUtils.getName(key);
//            }
//            Files files = new Files();
//            files.setStatus(BaseStatus.NORMAL.getStatus());
//            files.setRawName(fileName);
//            files.setOriginalName(fileName);
//            files.setFileType(FileType.getType(commit.getBucket()).getCode());
//            files.setCreated(DateUtil.getCurrentTimeStamp());
//            files.setFileSize(commit.getSize());
//            files.setFilePath(commit.getKey());
//            files.setExtension(FilenameUtils.getExtension(fileName));
//            files.setUpdated(DateUtil.getCurrentTimeStamp());
//            fileService.insertFiles(files);
//
//            String position = commit.getPosition();
//
//            PositionFile positionFile = new PositionFile(position, files.getId());
//            positionFiles.add(positionFile);
//        }
//
//        fileId = positionFiles.get(0).getFileId();
//
//        ActionVideo actionVideo = new ActionVideo();
//        actionVideo.setVideoName(actionVo.getActionName());
//        actionVideo.setUserId(loginVo.getId());
//        actionVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//        actionVideo.setStatus(BaseStatus.NORMAL.getStatus());
//        actionVideo.setDuration(0l);
//        actionVideo.setCreated(DateUtil.getCurrentTimeStamp());
//        actionVideo.setCoverFileId(actionVo.getCoverFileId());
//        actionVideo.setActionId(actionVo.getId());
//        actionVideo.setIsPublic(request.getIsPublic());
//        actionVideo.setVideos(JSON.toJSONString(positionFiles));
//        actionVideoDao.insert(actionVideo);
//
//        File file = fileService.downloadFile(fileId);
//
//        String coverName = StringUtil.uuid() + ".jpg";
//        VideoInfo videoInfo = null;
//        String coverPath = "";
//        try {
//            videoInfo = VideoTool.getVideoInfo(file.getAbsolutePath(), coverName);
//            coverPath = fileService.saveFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//            log.info("cover file name : {}", coverPath);
//            Files coverFile = new Files();
//            coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//            coverFile.setExtension(FilenameUtils.getExtension(coverName));
//            coverFile.setFilePath(coverPath);
//            coverFile.setFileSize(videoInfo.getCover().length());
//            coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//            coverFile.setFileType(FileType.PICTURE.getCode());
//            coverFile.setOriginalName(coverName);
//            coverFile.setRawName(coverName);
//            coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//            fileService.insertFiles(coverFile);
//
//            actionVideo.setCoverFileId(coverFile.getId());
//            actionVideo.setDuration((long)videoInfo.getDuration());
//
//            actionVideoDao.updateById(actionVideo);
//
//        } catch (Exception e) {
//            log.error("error:", e);
//            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Transactional
//    @Override
//    public void updateActionVideo(UpdateActionVideoRequest request, LoginVo loginVo) {
//        ActionVideo actionVideo = actionVideoDao.selectById(request.getId());
//        if (actionVideo == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        ActionVo actionVo = actionService.getActionVo(request.getActionId());
//        List<CommitUploadRequest> commits = request.getFiles();
//
//
//        if (CollectionUtils.isNotEmpty(commits)) {
//            List<PositionFile> positionFiles = new ArrayList<>();
//            long fileId = 0l;
//            for (CommitUploadRequest commit : commits) {
//                String key = commit.getKey();
//                fileStorage.convertToM3u8(FileType.VIDEO.getName(), key);
//                String fileName = "";
//                if (StringUtils.isNotBlank(commit.getFileName())) {
//                    fileName = commit.getFileName();
//                } else {
//                    fileName = FilenameUtils.getName(key);
//                }
//                Files files = new Files();
//                files.setStatus(BaseStatus.NORMAL.getStatus());
//                files.setRawName(fileName);
//                files.setOriginalName(fileName);
//                files.setFileType(FileType.getType(commit.getBucket()).getCode());
//                files.setCreated(DateUtil.getCurrentTimeStamp());
//                files.setFileSize(commit.getSize());
//                files.setFilePath(commit.getKey());
//                files.setExtension(FilenameUtils.getExtension(fileName));
//                files.setUpdated(DateUtil.getCurrentTimeStamp());
//                fileService.insertFiles(files);
//
//                String position = commit.getPosition();
//
//                PositionFile positionFile = new PositionFile(position, files.getId());
//                positionFiles.add(positionFile);
//            }
//
//            fileId = positionFiles.get(0).getFileId();
//
//            File file = fileService.downloadFile(fileId);
//
//            String coverName = StringUtil.uuid() + ".jpg";
//            VideoInfo videoInfo = null;
//            String coverPath = "";
//            try {
//                videoInfo = VideoTool.getVideoInfo(file.getAbsolutePath(), coverName);
//                coverPath = fileService.saveFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//                log.info("cover file name : {}", coverPath);
//                Files coverFile = new Files();
//                coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//                coverFile.setExtension(FilenameUtils.getExtension(coverName));
//                coverFile.setFilePath(coverPath);
//                coverFile.setFileSize(videoInfo.getCover().length());
//                coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//                coverFile.setFileType(FileType.PICTURE.getCode());
//                coverFile.setOriginalName(coverName);
//                coverFile.setRawName(coverName);
//                coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//                fileService.insertFiles(coverFile);
//
//                actionVideo.setCoverFileId(coverFile.getId());
//                actionVideo.setDuration((long)videoInfo.getDuration());
//
//                actionVideoDao.updateById(actionVideo);
//
//            } catch (Exception e) {
//                log.error("error:", e);
//                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//            }
//            actionVideo.setVideos(JSON.toJSONString(positionFiles));
//        }
//        actionVideo.setVideoName(actionVo.getActionName());
//        actionVideo.setUserId(loginVo.getId());
//        actionVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//        actionVideo.setActionId(actionVo.getId());
//        actionVideo.setIsPublic(request.getIsPublic());
//
//        actionVideoDao.updateById(actionVideo);
//    }
//
//    @Override
//    public void deleteActionVideo(long id) {
//        ActionVideo actionVideo = getById(id);
//        if (actionVideo == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        actionVideo.setStatus(BaseStatus.DELETE.getStatus());
//        actionVideo.setUpdated(DateUtil.getCurrentTimeStamp());
//        actionVideoDao.updateById(actionVideo);
//    }
//
//    private ActionVideoVo convertToActionVideoVo(ActionVideo actionVideo) {
//        ActionVideoVo vo = new ActionVideoVo();
//        ActionVo actionVo = actionService.getActionVo(actionVideo.getActionId());
//        vo.setDuration(DateUtil.secondToHMS(actionVideo.getDuration()));
//        vo.setId(actionVideo.getId());
//        List<PositionFile> positionFiles = JSON.parseArray(actionVideo.getVideos(), PositionFile.class);
//        if (CollectionUtils.isNotEmpty(positionFiles)) {
//            PositionFile positionFile = positionFiles.get(0);
//            Files file = fileService.getFile(positionFile.getFileId());
//            vo.setFileName(file.getOriginalName());
//        } else {
//            vo.setFileName("");
//        }
//        List<PositionFileVo> collect = positionFiles.stream().map(positionFile -> convertToPositionFileVo(positionFile)).collect(Collectors.toList());
//        vo.setVideos(collect);
//        vo.setActionName(actionVideo.getVideoName());
//        vo.setAnalysisPosition(actionVo.getAnalysisPosition());
//
//        return vo;
//    }
//
//    private PositionFileVo convertToPositionFileVo(PositionFile positionFile) {
//        PositionFileVo vo = new PositionFileVo();
//        vo.setUrl(fileService.getFileUrl(positionFile.getFileId(), false));
//        vo.setPosition(positionFile.getPosition());
//        return vo;
//    }
//
//    private ActionVideoListVo convertToActionVideoListVo(ActionVideo actionVideo) {
//        ActionVo actionVo = actionService.getActionVo(actionVideo.getActionId());
//        ActionVideoListVo vo = new ActionVideoListVo();
//        DoctorVo doctorVo = doctorService.getDoctorVo(actionVideo.getUserId());
//        vo.setActionName(actionVideo.getVideoName());
//        vo.setCreated(DateUtil.getYMDHMSDate(actionVideo.getCreated()));
//        vo.setDuration(DateUtil.secondToHMS(actionVideo.getDuration()));
//        vo.setId(actionVideo.getId());
//        vo.setIsPublic(actionVideo.getIsPublic());
//        vo.setUserId(doctorVo.getId());
//        vo.setUserName(doctorVo.getName());
//        vo.setSort(actionVo.getEquipment() + "-" + actionVo.getInstrument());
//        vo.setActionId(actionVideo.getActionId());
//        vo.setCoverUrl(fileService.getFileUrl(actionVideo.getCoverFileId(), false));
//        List<PositionFile> positionFiles = JSON.parseArray(actionVideo.getVideos(), PositionFile.class);
//        if (CollectionUtils.isNotEmpty(positionFiles) && positionFiles.size() > 1) {
//            PositionFile pf = getPositionFile(positionFiles, actionVo.getAnalysisPosition());
//            if (pf != null) {
//                Collections.swap(positionFiles, positionFiles.indexOf(pf), 0);
//            }
//        }
//        List<PositionFileVo> collect = positionFiles.stream().map(positionFile -> convertToPositionFileVo(positionFile)).collect(Collectors.toList());
//        vo.setVideos(collect);
//        vo.setAnalysisPostion(actionVo.getAnalysisPosition());
//        return vo;
//    }
}
