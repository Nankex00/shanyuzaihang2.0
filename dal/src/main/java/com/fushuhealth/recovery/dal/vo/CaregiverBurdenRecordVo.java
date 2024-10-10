package com.fushuhealth.recovery.dal.vo;

import com.fushuhealth.recovery.dal.dto.CaregiverBurdenResult;
import lombok.Data;

@Data
public class CaregiverBurdenRecordVo extends ScaleRecordVo{

    private CaregiverBurdenResult result;
}
