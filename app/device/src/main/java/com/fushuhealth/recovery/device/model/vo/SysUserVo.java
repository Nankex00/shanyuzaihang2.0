package com.fushuhealth.recovery.device.model.vo;

import com.fushuhealth.recovery.common.core.domin.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserVo extends SysUser {
    private String institutionLevel;
}
