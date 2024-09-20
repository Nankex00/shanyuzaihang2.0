package com.fushuhealth.recovery.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName role_user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUser {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Byte status;

    private Long created;

    private Long updated;
}