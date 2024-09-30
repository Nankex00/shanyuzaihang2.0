package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DangerLevelChangeRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long operatedInstitution;
    private Date operatedTime;
    private Byte dangerLevel;
    private String reason;
    private Long childId;
}
