package com.fushuhealth.recovery.device.model.vo;

import com.fushuhealth.recovery.dal.vo.AttachmentVo;
import lombok.Data;

import java.util.List;

@Data
public class ScaleRecordListVo {

    private Long id;

    private Long userId;

    private String scaleTableName;

    private Byte scaleTableCode;

    private String classification;

    private String userName;

    private String phone;

    private String birthday;

    private String created;

    private Integer userScore;

    private Integer doctorScore;

    private String progressStatus;

    private Byte progressStatusByte;

    private Integer haveAbnormalIterm;

    private String gmsResult;

    private List<AttachmentVo> videos;

    private String doctorName;

    private Long doctorId;
}
