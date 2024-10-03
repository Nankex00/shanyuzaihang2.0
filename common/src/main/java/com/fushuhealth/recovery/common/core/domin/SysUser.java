package com.fushuhealth.recovery.common.core.domin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fushuhealth.recovery.common.core.domin.dto.RoleDTO;
import com.fushuhealth.recovery.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @TableName user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private String userName;

    private Long deptId;

    private String password;

    private Byte enable;

    private String status;

    @TableField(exist = false)
    private SysRole role;

    @TableField(exist = false)
    private Long roleId;

    @TableField(exist = false)
    private List<SysRole> roles;

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public SysUser(String userName){
        this.userName = userName;
    }

}