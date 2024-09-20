package com.fushuhealth.recovery.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName fun_per
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FunPer implements Serializable {

    private Long id;

    private Long parentId;

    private Byte funType;

    private String funName;

    private String frontRoute;

    private Byte status;

    private Long created;

    private Long updated;

    private static final long serialVersionUID = 1L;
}