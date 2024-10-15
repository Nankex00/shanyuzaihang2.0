package com.fushuhealth.recovery.device.service;


import com.fushuhealth.recovery.device.model.vo.ScaleTableListVo;
import com.fushuhealth.recovery.device.model.vo.ScaleTableVo;

import java.util.List;

public interface ScaleTableService {

    ScaleTableVo getScaleTableVo(byte code);

    ScaleTableVo getScaleTableVo(byte code, long birthday);

    List<ScaleTableListVo> listScaleTable(String name, byte firstLevel, byte secondLevel);
}
