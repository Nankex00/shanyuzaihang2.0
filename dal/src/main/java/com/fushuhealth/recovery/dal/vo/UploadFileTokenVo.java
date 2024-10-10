package com.fushuhealth.recovery.dal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileTokenVo {

    private String bucket;

    private String key;

    private String token;
}
