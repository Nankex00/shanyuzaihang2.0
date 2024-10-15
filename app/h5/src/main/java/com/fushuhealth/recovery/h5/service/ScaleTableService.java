package com.fushuhealth.recovery.h5.service;


import com.fushuhealth.recovery.dal.entity.ScaleTable;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableVo;

import java.math.BigDecimal;

public interface ScaleTableService {

    ScaleTableVo getScaleTableVo(byte code, long birthday);

    ScaleTable getScaleTable(byte code);

    Integer getPrice(byte code);
}
