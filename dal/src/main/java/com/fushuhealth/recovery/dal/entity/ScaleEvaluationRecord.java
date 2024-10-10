package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ScaleEvaluationRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String openId;

    private Long childrenId;

    private String name;

    private Long birthday;

    private Byte gender;

    //量表 classification
    private Byte type;

    //1：普通，2：线粒体病，3：0-1岁发育评估, 4:扫码登记
    private Byte category;

    private Byte scaleTableCode;

    private String scaleTableName;

    private Long resultFileId;

    private String resultPics;

    private String answerWithRemark;

//    private Long next

    private String conclusion;

    private String result;

    //处理状态，新建，测试中，测试完成，诊断量表还有其他状态
    private Byte progressStatus;

    //数据状态，6 待上传，8 已上传
    private Byte dataStatus;

    //数据上传来源，1：小程序，2：机构端，3：网页端
    private Byte dataUploadSource;

    private Integer userScore;

    private Integer doctorScore;

    private Long doctorId;

    //预约类型，门诊评估，智能评估，康复指导
    private Byte reserveType;

    private Long reserveId;

    private Long reserveStartTime;

    //门诊评估的排班 id
    private Long workScheduleId;

    private Long organizationId;

    private String appId;

    private String channel;

    private String orgId;

//    private Long invoiceFileId;

    private Long orderId;

    private Byte status;

    private Long created;

    private Long updated;
}
