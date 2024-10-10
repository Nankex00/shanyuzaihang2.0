package com.fushuhealth.recovery.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {

    private String fileName;

    private String key;

    private Long size;
}
