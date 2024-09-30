package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@Data
public class Risks {

    @TableId(type = IdType.AUTO)
    private Long id;

    //1:儿童高危因素，2：母亲高危因素
    private Byte type;

    private String name;

    private Byte status;

    private Long created;

    private Long updated;
}
