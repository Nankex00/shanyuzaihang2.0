package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.device.model.vo.SysUserVo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @author Zhuanz
 * @date 2024/9/25
 */
@Data
@AllArgsConstructor
public class InfoResponse {
    private SysUserVo user;
    private Set<String> roles;
    private Set<String> permissions;
    private Long institutionLevel;
//    private Long level;
}
