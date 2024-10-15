package com.fushuhealth.recovery.dal.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ActionQuery {

    private Integer pageNo;

    private Integer pageSize;

    private String word;

    private Long tagId;

}
