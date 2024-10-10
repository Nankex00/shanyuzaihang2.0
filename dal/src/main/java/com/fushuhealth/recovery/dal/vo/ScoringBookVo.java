package com.fushuhealth.recovery.dal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoringBookVo {

    private Long fileId;

    private String fileName;

    private String fileUrl;
}
