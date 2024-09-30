package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diagnose {
    private Long id;
    private Byte category;
    private String categoryName;
    private Long parentId;
}
