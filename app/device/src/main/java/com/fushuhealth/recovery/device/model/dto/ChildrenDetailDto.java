package com.fushuhealth.recovery.device.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.device.model.response.ChildrenDetail;
import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class ChildrenDetailDto{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String sex;
    private String dateOfBirth;
    private Integer age;
    private Integer gestationalWeeks;
    private Integer gestationalWeekDay;
    private Integer brithWight;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> dangerOfChild;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> dangerOfMother;
    private Byte dangerLevel;
    private String diagnose;
    private String telephone;
    private String identification;
    private String socialName;
    private String deptId;
    private String deptName;
}
