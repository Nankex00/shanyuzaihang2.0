package com.fushuhealth.recovery.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.common.constant.RoleEnum;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.StringUtil;
import com.fushuhealth.recovery.dal.dao.ActionsDao;
import com.fushuhealth.recovery.dal.entity.Actions;
import com.fushuhealth.recovery.dal.entity.Tags;
import com.fushuhealth.recovery.dal.vo.ActionVo;
import com.fushuhealth.recovery.device.service.ActionService;
import com.fushuhealth.recovery.device.service.FileService;
import com.fushuhealth.recovery.device.service.TagService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActionServiceImpl implements ActionService {
//
//    private final static Logger log = LoggerFactory.getLogger(ActionServiceImpl.class);
//
    @Autowired
    private ActionsDao actionsDao;
//
//    @Autowired
//    private DoctorService doctorService;
//
    @Autowired
    private TagService tagService;

    @Autowired
    private FileService fileService;
//
//    @Autowired
//    private ActionVideoService actionVideoService;
//
//    @Value("${action.attribute}")
//    private String actionAttribute;
//
//    private static Map<String, ActionAttributeVo> actionAttributes = new HashMap<>();
//
//    @Override
//    public ListActionResponse queryActions(ActionQuery actionQuery, LoginVo loginVo) {
//        QueryWrapper<Actions> wrapper = new QueryWrapper<>();
//
//        Page<Actions> page = new Page<>(actionQuery.getPageNo(), actionQuery.getPageSize());
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        if (!RoleEnum.ROLE_SUPER_ADMIN.getName().equalsIgnoreCase(loginVo.getRoleName())) {
//            if (!CollectionUtils.isEmpty(loginVo.getActionIds())) {
//                wrapper.in("id", loginVo.getActionIds());
//            }
//        }
//
//        if (actionQuery.getTagId() > 0) {
//            List<Long> ids = tagService.listActionIdsByTagId(actionQuery.getTagId());
//            if (!CollectionUtils.isEmpty(ids)) {
//                wrapper.in("id", ids);
//            } else {
//                wrapper.eq("id", 0);
//            }
//        }
//        if (!StringUtils.isBlank(actionQuery.getWord())) {
//            wrapper.like("action_name", actionQuery.getWord());
//        }
//        wrapper.orderByDesc("id");
////        page = actionsDao.selectActionListVoPage(page, wrapper);
////        List<ActionListVo> actions = page.getRecords();
////        actions.stream().map(actionListVo -> fillUrl(actionListVo)).collect(Collectors.toList());
//        page = actionsDao.selectPage(page, wrapper);
//        List<Actions> records = page.getRecords();
//        List<ActionListVo> list = records.stream().map(actions -> convertToActionListVo(actions)).collect(Collectors.toList());
//
//        PageVo pageVo = PageVo.builder()
//                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
//        return new ListActionResponse(list, pageVo);
//    }
//
    @Override
    public ActionVo getActionVo(long id) {
        Actions action = getAction(id);
        if (action == null) {
            throw new OldServiceException(ResultCode.NOT_FOUND);
        }
        return convertToActionVo(action);
    }
//
//    @Transactional
//    @Override
//    public void saveAction(SaveActionRequest request, LoginVo vo) {
//
//        Actions actions = new Actions();
//        actions.setVideoCount(0);
//        actions.setUserId(vo.getId());
//        actions.setUpdated(DateUtil.getCurrentTimeStamp());
//        actions.setStatus(BaseStatus.NORMAL.getStatus());
//        actions.setPrecaution("");
//        actions.setInstruction("");
//        actions.setCreated(DateUtil.getCurrentTimeStamp());
//        actions.setActionName(request.getName());
//        actions.setCameraCount(request.getCameraCount());
//        actions.setCameraPosition(StringUtils.join(request.getCameraPosition(), ","));
//        actions.setRemark(request.getRemark());
//
//        String actionCode = request.getActionCode();
//        ActionAttributeVo actionAttributeVo = actionAttributes.get(actionCode);
//        if (actionAttributeVo != null) {
//            actions.setMotionId(actionAttributeVo.getMotionId());
//            actions.setMatrix(actionAttributeVo.getCoordinate());
//            actions.setActionCode(actionAttributeVo.getCode());
//            actions.setAnalysisPosition(actionAttributeVo.getPosition());
//        } else {
//            actions.setMotionId("");
//            actions.setMatrix("");
//            actions.setActionCode("");
//            actions.setAnalysisPosition("");
//        }
//
//        String fileName = FilenameUtils.getName(request.getFileInfo().getKey());
//        FileType fileType = FileType.getType(request.getFileInfo().getBucket());
//        Files files = new Files();
//        files.setStatus(BaseStatus.NORMAL.getStatus());
//        files.setRawName(fileName);
//        files.setOriginalName(fileName);
//        files.setFileType(fileType.getCode());
//        files.setCreated(DateUtil.getCurrentTimeStamp());
//        files.setFileSize(request.getFileInfo().getSize());
//        files.setFilePath(request.getFileInfo().getKey());
//        files.setExtension(FilenameUtils.getExtension(fileName));
//        files.setUpdated(DateUtil.getCurrentTimeStamp());
//        fileService.insertFiles(files);
//
//        File file = fileService.downloadFile(files.getId());
//        log.info("file name : {}", file.getAbsolutePath());
//        if (FileUtil.isVideo(fileName)) {
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
//                actions.setPhotoFileId(coverFile.getId());
//
//            } catch (Exception e) {
//                log.error("error:", e);
//                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            actions.setPhotoFileId(files.getId());
//        }
//
//        actions.setDrawingFileId(files.getId());
//        actionsDao.insert(actions);
//
//        ActionTagMap actionTagMap = new ActionTagMap();
//        actionTagMap.setTagId(request.getTagId());
//        actionTagMap.setActionId(actions.getId());
//        tagService.saveActionTagMap(actionTagMap);
//    }
//
//    @Override
//    public List<Long> getIdsLikeName(String name) {
//        QueryWrapper<Actions> wrapper = new QueryWrapper<>();
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        wrapper.like("action_name", name);
//        List<Actions> actions = actionsDao.selectList(wrapper);
//        List<Long> collect = actions.stream().map(Actions::getId).collect(Collectors.toList());
//        return collect;
//    }
//
//    @Transactional
//    @Override
//    public void updateAction(UpdateActionRequest request, LoginVo vo) {
//        Actions actions = getAction(request.getId());
//        if (actions == null) {
//            throw new ServiceException(ResultCode.PARAM_ERROR);
//        }
//        actions.setVideoCount(0);
//        actions.setUpdated(DateUtil.getCurrentTimeStamp());
//        actions.setActionName(request.getName());
//        actions.setCameraCount(request.getCameraCount());
//        actions.setCameraPosition(StringUtils.join(request.getCameraPosition(), ","));
//        actions.setRemark(request.getRemark());
//        actions.setUserId(vo.getId());
//
//        String actionCode = request.getActionCode();
//        ActionAttributeVo actionAttributeVo = actionAttributes.get(actionCode);
//        if (actionAttributeVo != null) {
//            actions.setMotionId(actionAttributeVo.getMotionId());
//            actions.setMatrix(actionAttributeVo.getCoordinate());
//            actions.setActionCode(actionAttributeVo.getCode());
//            actions.setAnalysisPosition(actionAttributeVo.getPosition());
//        } else {
//            actions.setMotionId("");
//            actions.setMatrix("");
//            actions.setActionCode("");
//            actions.setAnalysisPosition("");
//        }
//
//        if (request.getFileInfo() != null) {
//            String fileName = FilenameUtils.getName(request.getFileInfo().getKey());
//            FileType fileType = FileType.getType(request.getFileInfo().getBucket());
//            Files files = new Files();
//            files.setStatus(BaseStatus.NORMAL.getStatus());
//            files.setRawName(fileName);
//            files.setOriginalName(fileName);
//            files.setFileType(fileType.getCode());
//            files.setCreated(DateUtil.getCurrentTimeStamp());
//            files.setFileSize(request.getFileInfo().getSize());
//            files.setFilePath(request.getFileInfo().getKey());
//            files.setExtension(FilenameUtils.getExtension(fileName));
//            files.setUpdated(DateUtil.getCurrentTimeStamp());
//            fileService.insertFiles(files);
//
//            File file = fileService.downloadFile(files.getId());
//            log.info("file name : {}", file.getAbsolutePath());
//            if (FileUtil.isVideo(fileName)) {
//                String coverName = StringUtil.uuid() + ".jpg";
//                VideoInfo videoInfo = null;
//                String coverPath = "";
//                try {
//                    videoInfo = VideoTool.getVideoInfo(file.getAbsolutePath(), coverName);
//                    coverPath = fileService.saveFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//                    log.info("cover file name : {}", coverPath);
//                    Files coverFile = new Files();
//                    coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//                    coverFile.setExtension(FilenameUtils.getExtension(coverName));
//                    coverFile.setFilePath(coverPath);
//                    coverFile.setFileSize(videoInfo.getCover().length());
//                    coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//                    coverFile.setFileType(FileType.PICTURE.getCode());
//                    coverFile.setOriginalName(coverName);
//                    coverFile.setRawName(coverName);
//                    coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//                    fileService.insertFiles(coverFile);
//
//                    actions.setPhotoFileId(coverFile.getId());
//
//                } catch (Exception e) {
//                    log.error("error:", e);
//                    throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//                }
//            } else {
//                actions.setPhotoFileId(files.getId());
//            }
//
//            actions.setDrawingFileId(files.getId());
//        }
//        actionsDao.updateById(actions);
//    }
//
//    @Override
//    public List<ActionAttributeVo> getActionAttribute() {
//        File file = new File(actionAttribute);
//        try {
//            String content = FileUtils.readFileToString(file, "UTF-8");
//            List<ActionAttributeVo> actionAttributeVos = JSON.parseArray(content, ActionAttributeVo.class);
//
//            resetActionAttribute(actionAttributeVos);
//            return actionAttributeVos;
//        } catch (IOException e) {
//            log.info("read action attribute file error : {}", e);
//            return new ArrayList<ActionAttributeVo>();
//        }
//    }
//
    private Actions getAction(long id) {
        return actionsDao.selectById(id);
    }
//
//    private ActionListVo convertToActionListVo(Actions actions) {
////        //TODO
////        List<Tags> tags = tagService.getTagByActionId(actions.getId());
////        ArrayList<String> name = new ArrayList<>();
////        name.add(actions.getActionName());
////        if (actions.getCameraCount() == 2) {
////            name.add("双摄");
////        } else if (actions.getCameraCount() == 3) {
////            name.add("三摄");
////        } else {
////            name.add("单摄");
////        }
////        for (Tags tag : tags) {
////            name.add(tag.getName());
////        }
//
//        ActionListVo vo = new ActionListVo();
//        vo.setId(actions.getId());
//        vo.setCoverUrl(fileService.getFileUrl(actions.getPhotoFileId(), false));
//        vo.setActionName(actions.getActionName());
//        vo.setUrl(fileService.getFileUrl(actions.getDrawingFileId(), false));
//        vo.setType(actions.getPhotoFileId().equals(actions.getDrawingFileId()) ? "picture" : "video");
//        vo.setCreated(DateUtil.getYMDHMSDate(actions.getCreated()));
//        List<Tags> tags = tagService.getTagByActionId(actions.getId());
//        vo.setEquipmentId(tags.get(0).getId());
//        vo.setEquipment(tags.get(0).getName());
//        vo.setInstrumentId(tags.get(1).getId());
//        vo.setInstrument(tags.get(1).getName());
//        return vo;
//    }
//
    private ActionVo convertToActionVo(Actions actions) {
        ActionVo vo = new ActionVo();
        List<Tags> tags = tagService.getTagByActionId(actions.getId());
        vo.setId(actions.getId());
        vo.setCoverUrl(fileService.getFileUrl(actions.getPhotoFileId(), false));
        vo.setActionName(actions.getActionName());
        vo.setUrl(fileService.getFileUrl(actions.getDrawingFileId(), false));
        vo.setType(actions.getPhotoFileId().equals(actions.getDrawingFileId()) ? "picture" : "video");
        vo.setCameraCount(actions.getCameraCount());
        vo.setCameraPosition(Arrays.asList(actions.getCameraPosition().split(",")));
        vo.setEquipmentId(tags.get(0).getId());
        vo.setEquipment(tags.get(0).getName());
        vo.setInstrumentId(tags.get(1).getId());
        vo.setInstrument(tags.get(1).getName());
        vo.setRemark(actions.getRemark());
        vo.setAnalysisPosition(actions.getAnalysisPosition());
        vo.setResultPage(actions.getResultPage());
        vo.setCoverFileId(actions.getPhotoFileId());
        vo.setActionCode(actions.getActionCode());
        vo.setMotionId(actions.getMotionId());
        vo.setTagId(tags.get(1).getId());
        return vo;
    }
//
//    private synchronized void resetActionAttribute(List<ActionAttributeVo> list) {
//        actionAttributes.clear();
//        for (ActionAttributeVo actionAttributeVo : list) {
//            actionAttributes.put(actionAttributeVo.getCode(), actionAttributeVo);
//        }
//    }
}
