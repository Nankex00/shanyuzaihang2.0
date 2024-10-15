package com.fushuhealth.recovery.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/10/12
 */
@Data
@AllArgsConstructor
public class FileDetailDto {
    private Long fileId;
    private String url;
}
