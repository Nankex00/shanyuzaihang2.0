package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fushuhealth.recovery.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dept")
public class SysDept extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long deptId;

    private String deptName;

    private Long institutionLevel;

    private Long parentId;

    private String ancestors;

    private String contactNumber;

    private String doctor;

    private String address;

    /** 子部门 */
//    @TableField(exist = false)
//    private List<SysDept> children = new ArrayList<SysDept>();
}
