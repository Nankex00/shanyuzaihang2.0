package com.fushuhealth.recovery.device.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class PredictWarnListVo {
    private Long id;
    private String name;
    private String sex;
    private Long dateOfBirth;
    private Byte dangerLevel;
    private Byte monthAge;
    private Byte warnResult;
    private Date submitTime;
}
