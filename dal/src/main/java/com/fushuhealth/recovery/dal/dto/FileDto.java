package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/10/12
 */
@Data
public class FileDto{
    private Long id;
    private String bucket;
    private String key;
    private Long size;
    private Byte type;
    private Byte commendType;
}