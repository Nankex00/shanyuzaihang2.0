package com.fushuhealth.recovery.common.constant;

public enum ScaleStatus {


    ALL((byte) 0, "全部"),
    NOT_EVALUATE((byte) 1, "未评估"),
    PROGRESSING((byte) 2, "计算中"),
    EVALUATED((byte) 3, "已发送"),
    GMS_EVALUATED((byte) 4, "GMS已评估"),
    BERE_EVALUATED((byte) 5, "脑瘫已评估"),

    WAIT_UPLOAD((byte)6, "待上传"),

    WAIT_SCHEDULE((byte)7, "待排班"),

    UPLOADED((byte)8, "已上传"),

    SCHEDULED((byte)9, "已排班"),

    TRAINING((byte) 10, "训练中"),

    PARENTS_WAITING_WRITE((byte) 11, "待家长填写"),

    PARENTS_WRITTEN((byte) 12, "家长已填写"),

    EVALUATED_WAIT_REVIEW((byte)13, "评估待审核"),

    REVIEW_NOT_PASS((byte)14, "审核不通过待修改"),

    REVIEWED_WAIT_SEND((byte)15, "审核通过待发送"),



    CANCELLED((byte)20, "已取消"),
    FINISHED((byte)21, "已完成"),
    REFUNDED((byte) 25, "已退费");

    private final Byte status;
    private final String desc;

    ScaleStatus(Byte status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ScaleStatus getStatus(Byte status) {

        for (ScaleStatus scaleStatus : ScaleStatus.values()) {
            if (status.equals(scaleStatus.getStatus())) {
                return scaleStatus;
            }
        }
        throw new IllegalArgumentException();
    }

    public Byte getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
