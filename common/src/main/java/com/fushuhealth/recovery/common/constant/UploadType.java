package com.fushuhealth.recovery.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhuanz
 * @date 2024/10/12
 */
@AllArgsConstructor
@Getter
public enum UploadType {
    ADD((byte)1,"新增"),
    DETELE((byte)2,"删除");

    private Byte type;
    private String text;

}
