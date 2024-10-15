//package com.fushuhealth.h5.task;
//
//import com.alibaba.fastjson.JSON;
//import com.fushuhealth.recovery.common.config.BeanContext;
//import com.fushuhealth.recovery.common.constant.BaseStatus;
//import com.fushuhealth.recovery.common.storage.FileStorage;
//import com.fushuhealth.recovery.common.storage.FileType;
//import com.fushuhealth.recovery.common.util.DateUtil;
//import com.fushuhealth.recovery.common.util.StringUtil;
//import com.fushuhealth.recovery.dal.entity.ScaleEvaluationRecord;
//import com.fushuhealth.recovery.device.h5.service.FileService;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SaveScaleAttachmentAsyncTask implements Runnable {
//
//    private final static Logger log = LoggerFactory.getLogger(SaveScaleAttachmentAsyncTask.class);
//
//    private Long id;
//
//    private List<AnswerRequest> answers;
//
//    private ScaleEvaluationRecordService scaleEvaluationRecordService = BeanContext.getBean(ScaleEvaluationRecordService.class);
//
//    private FileService fileService = BeanContext.getBean(FileService.class);
//
//    private FileStorage fileStorage = BeanContext.getBean(FileStorage.class);
//
//    public SaveScaleAttachmentAsyncTask(Long id, List<AnswerRequest> answers) {
//        this.id = id;
//        this.answers = answers;
//    }
//
//    @Override
//    public void run() {
//
//        log.info("异步处理附件");
//        ScaleEvaluationRecord record = scaleEvaluationRecordService.getById(id);
//        if (record == null) {
//            return;
//        }
//
//        String answerWithRemark = record.getAnswerWithRemark();
//        List<AnswerWithRemarkDto> answerWithRemarkDtos = JSON.parseArray(answerWithRemark, AnswerWithRemarkDto.class);
//
//        for (AnswerWithRemarkDto answerWithRemarkDto : answerWithRemarkDtos) {
//            AnswerRequest answer = getAnswerRequest(answerWithRemarkDto.getQuestionSn());
//
//            List<AttachmentRequest> attachments = answer.getAttachments();
//            if (CollectionUtils.isNotEmpty(attachments)) {
//                ArrayList<AttachmentDto> attachmentDtos = new ArrayList<>();
//                for (AttachmentRequest attachment : attachments) {
//                    long coverFileId = 0l;
//
//                    Files file = new Files();
//                    file.setStatus(BaseStatus.NORMAL.getStatus());
//                    file.setFileType(FileType.getType(attachment.getType()).getCode());
//                    file.setCreated(DateUtil.getCurrentTimeStamp());
//                    file.setUpdated(DateUtil.getCurrentTimeStamp());
//
//                    long fileId = 0l;
//                    if (attachment.getType() == FileType.PICTURE.getCode() ||
//                            attachment.getType() == FileType.AUDIO.getCode()) {
//                        fileId = Long.parseLong(attachment.getServerId());
//                        log.info("file id: {}", fileId);
//                    } else {
//
//                        Files videoFile = fileService.getFile(Long.parseLong(attachment.getServerId()));
//                        log.info("get file from qiniu: {}", JSON.toJSONString(videoFile));
//                        FileType type = FileType.getType(videoFile.getFileType());
//                        String key = type.getName() + "/" + videoFile.getFilePath();
//
//                        File originFile = fileService.downloadFile(videoFile.getId());
//                        File localFile = null;
//                        if (key.endsWith(".webm")) {
//                            try {
//
//                                File mp4File = VideoTool.convertToMp4(originFile);
//                                key = fileService.saveFile(FileType.VIDEO, mp4File, mp4File.getName());
//
//                                Files mp4 = new Files();
//                                mp4.setStatus(BaseStatus.NORMAL.getStatus());
//                                mp4.setRawName(mp4File.getName());
//                                mp4.setOriginalName(mp4File.getName());
//                                mp4.setFileType(FileType.VIDEO.getCode());
//                                mp4.setCreated(DateUtil.getCurrentTimeStamp());
//                                mp4.setFileSize(mp4File.getTotalSpace());
//                                mp4.setFilePath(key);
//                                mp4.setExtension(FilenameUtils.getExtension(mp4File.getName()));
//                                mp4.setUpdated(DateUtil.getCurrentTimeStamp());
//                                fileService.insertFiles(mp4);
//
//                                fileId = mp4.getId();
//
//                                localFile = mp4File;
//                            } catch (Exception e) {
//                                log.error("convert webm to mp4 error:{}", e.getMessage());
//                                continue;
//                            }
//                        } else {
//                            fileId = videoFile.getId();
//                            localFile = originFile;
//                        }
//                        fileStorage.convertToM3u8(FileType.VIDEO.getName(), key);
//
//                        try {
//                            String coverName = StringUtil.uuid() + ".jpg";
//                            VideoInfo videoInfo = VideoTool.getVideoInfo(localFile.getAbsolutePath(), coverName);
//                            String coverPath = fileService.saveFile(FileType.PICTURE, videoInfo.getCover(), coverName);
//                            log.info("cover file name : {}", coverPath);
//                            Files coverFile = new Files();
//                            coverFile.setUpdated(DateUtil.getCurrentTimeStamp());
//                            coverFile.setExtension(FilenameUtils.getExtension(coverName));
//                            coverFile.setFilePath(coverPath);
//                            coverFile.setFileSize(videoInfo.getCover().length());
//                            coverFile.setCreated(DateUtil.getCurrentTimeStamp());
//                            coverFile.setFileType(FileType.PICTURE.getCode());
//                            coverFile.setOriginalName(coverName);
//                            coverFile.setRawName(coverName);
//                            coverFile.setStatus(BaseStatus.NORMAL.getStatus());
//                            fileService.insertFiles(coverFile);
//                            coverFileId = coverFile.getId();
//                        } catch (Exception e) {
//                            log.error("generate mp4 cover error:{}", e.getMessage());
//                            continue;
//                        }
//                    }
//                    attachmentDtos.add(new AttachmentDto(fileId, coverFileId, "", ""));
//                }
//                answerWithRemarkDto.setAttachmentDtos(attachmentDtos);
//            }
//        }
//
//        scaleEvaluationRecordService.update(new UpdateWrapper<ScaleEvaluationRecord>().lambda()
//                .set(ScaleEvaluationRecord::getAnswerWithRemark, JSON.toJSONString(answerWithRemarkDtos))
//                .eq(ScaleEvaluationRecord::getId, id));
//    }
//
//    private AnswerRequest getAnswerRequest(int answerId)  {
//        for (AnswerRequest answer : answers) {
//            if (answer.getQuestionSn() == answerId) {
//                return answer;
//            }
//        }
//        return null;
//    }
//}
