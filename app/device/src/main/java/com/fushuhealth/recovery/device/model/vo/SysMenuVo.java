package com.fushuhealth.recovery.device.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/10/8
 */
@Data
@NoArgsConstructor
public class SysMenuVo {
    private List<Long> checkedKeys;
    private List<TreeSelect> menus;
    private Byte dataScope;
}
