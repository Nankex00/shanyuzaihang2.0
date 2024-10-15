package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fushuhealth.recovery.dal.dto.GetGoodsParam;
import lombok.Data;

import java.util.List;

@Data
public class AbnormalItermInterventionMethodVo {

    private Long recordId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GetGoodsParam> resources;

    private String productId;

    private String resourceId;

    private Integer type;

    private String abnormalIterm;

    private String name;

    private String coverUrl;

    private String appId;

    private String page;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InterventionMethodVo methodDetail;
}
