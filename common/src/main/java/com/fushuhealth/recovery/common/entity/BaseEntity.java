package com.fushuhealth.recovery.common.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 * @author Zhuanz
 * @date 2024/9/23
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableLogic
    private Integer delFlag;
}
