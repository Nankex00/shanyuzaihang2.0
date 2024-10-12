package com.fushuhealth.recovery.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Result Code Enum
 *
 * @author william
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),

    USER_NOT_FOUND(1, "登录错误"),

    USER_EXIST(1, "用户已存在"),

    PASSWORD_ERROR(1, "登录错误"),

    USER_DISABLED(1, "用户已禁用"),

    USER_NOT_LOGIN(2, "用户未登陆"),

    PERMISSION_ERROR(1, "非法操作"),

    NOT_FOUND(1, "没有找到"),

    INTERNAL_SERVER_ERROR(1, "内部错误"),

    PARAM_ERROR(1, "参数错误"),

    UNSUPPORTED_FILE_TYPE(1, "不支持的文件类型"),

    UNSUPPORTED_SCALE_CODE(1, "不支持的量表类型"),

    SCALE_ORDER_STATUS_ERROR(1, "订单状态错误，请刷新后重试"),

    WX_PAY_ERROR(1, "支付失败"),

    CLASS_EXIST(1, "班级已存在"),

    CLASS_NOT_EXIST(1, "班级不存在"),

    STATUS_ERROR(1, "状态错误"),

    STUDENT_NUM_EXIST(1, "学号重复，请检查后重新输入"),

    WX_NOT_BIND(2, "用户微信未绑定"),
    WX_INVALID_OPENID(1, "非法的OPENID"),

    ORG_CONFIG_NOT_EXIST(1, "机构配置不存在"),

    SCHEDULE_TIME_HAS_USED(1, "您选择的时间内有其他排班安排"),
    WX_NOT_SUBSCRIBE(3, "用户未关注"),

    SMS_CODE_ERROR(1, "验证码错误"),

    SCALE_ORDER_ERROR(1, "订单错误"),

    SCALE_NO_CEREBRAL_PALSY_IN_7_DAYS(4, "儿童在一周内没有儿童脑瘫评测，需要先完成该量表评测"),

    SCALE_AGE_MORE_THAN_12_MONTH(5, "该测评仅限1岁内孩子评估"),
    GMS_NOT_EVALUATED(6, "评测未完成"),
    RESERVE_FULL(1001, "当前时间已约满"),
    RECOVERY_PLAN_NULL(2001, "请先创建训练方案"),
    RECOVERY_FIRST_VISIT_NULL(2002, "请填写【首诊评估】"),
    RECOVERY_STAGE_SUMMARY_NULL(2003, "请填写【康复阶段总结】"),

    SLEEP_RECORD_EXIST(2100, "当前日期睡眠记录已存在"),

    PATIENT_ERROR(3001, "儿童信息错误"),

    SCALE_RECORD_STATUS_ERROR(3002, "未出报告"),
    //量表填写时间过期
    SCALE_RECORD_EXPIRED(3003, "量表填写时间过期"),;

    final int code;

    final String msg;
}
