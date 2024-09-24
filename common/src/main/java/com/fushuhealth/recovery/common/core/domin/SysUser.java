package com.fushuhealth.recovery.common.core.domin;

import com.fushuhealth.recovery.common.entity.BaseEntity;
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
public class SysUser extends BaseEntity {

    private Long userId;

    private String userName;

    private Long deptId;

    private String password;

    private String saltKey;

    private Byte enable;

    private String status;

    private SysRole role;

    private Long roleId;

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