package com.fushuhealth.recovery.dal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestButtonVo {

    private String type;

    private String copyWriting;

    private String content;

    private String appId;

    private String resourceId;

    private String productId;
}
