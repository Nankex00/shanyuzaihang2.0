package com.fushuhealth.recovery.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName fun_role
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FunRole implements Serializable {
    private Integer id;

    private Integer funId;

    private Integer roleId;

    private Byte status;

    private Long created;

    private Long updated;

    private static final long serialVersionUID = 1L;
}