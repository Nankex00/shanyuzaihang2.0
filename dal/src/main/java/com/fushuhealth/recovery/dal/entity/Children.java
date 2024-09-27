package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fushuhealth.recovery.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("children")
public class Children extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String sex;
    private String dateOfBrith;
    private Integer age;
    private String dangerLevel;
    private String socialName;
    private String deptName;
}
