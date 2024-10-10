package com.fushuhealth.recovery.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {

    private Long fileId;

    private Long coverFileId;

    private String position;

    //视频类型
    private String videoType = "";
}
