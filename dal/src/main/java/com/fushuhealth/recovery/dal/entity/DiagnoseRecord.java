package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "diagnose_record",autoResultMap = true)
public class DiagnoseRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long operatedId;
    private Date operatedTime;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> beforeDiagnose;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> diagnoseDetail;
    private Long childId;
}
