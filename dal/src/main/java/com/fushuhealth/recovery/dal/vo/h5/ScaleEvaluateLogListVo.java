package com.fushuhealth.recovery.dal.vo.h5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ScaleEvaluateLogListVo {

    private Long id;

    private Long userId;

    private String userName;

    private String created;

    @JsonIgnore
    private Byte statusByte;

    private String status;

    private String remark;
}
