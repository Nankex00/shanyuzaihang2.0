package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String phone;

    private String email;

    private String name;

    private Byte enable;

    private Integer organizationId;

    private String password;

    private String saltKey;

    private String avatar;

    private Byte status;

    private Long created;

    private Long updated;
}