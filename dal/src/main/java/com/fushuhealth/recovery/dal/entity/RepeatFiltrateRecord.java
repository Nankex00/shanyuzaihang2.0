package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepeatFiltrateRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long childId;
    private Byte monthAge;
    private Long operatedId;
    private Date submitTime;
    private String aqsResult;
    private String aqsUrls;
    private String ddstResult;
    private String ddstUrls;
    private String otherResult;
    private String otherUrls;
    private Integer delFlag;
}
