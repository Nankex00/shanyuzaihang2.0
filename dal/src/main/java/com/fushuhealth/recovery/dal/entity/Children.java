package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fushuhealth.recovery.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "children",autoResultMap = true)
public class Children extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String sex;
    private Long dateOfBirth;
    private Integer age;
    private Integer gestationalWeeks;
    private Integer gestationalWeekDay;
    private Integer birthWeight;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> dangerOfChild;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> dangerOfMother;
    private Byte dangerLevel;
    private String diagnose;
    private String telephone;
    private String identification;
    private String socialName;
    private String deptId;
    //新增字段
    private String extraRisks;
    private String medicalCardNumber;

    public Long getBirthday(){
        return this.getDateOfBirth();
    }

    public String getGender()
    {
        return this.sex;
    }
}
