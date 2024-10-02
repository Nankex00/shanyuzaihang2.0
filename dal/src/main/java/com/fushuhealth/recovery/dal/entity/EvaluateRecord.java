package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long childId;
    private Byte monthAge;
    private String geSellResult;
    private String geSellUrls;
    private String sSResult;
    private String sSUrls;
    private String otherResult;
    private String otherUrls;
    private Date submitTime;
    private Long operatedId;
    private Integer delFlag;
}
