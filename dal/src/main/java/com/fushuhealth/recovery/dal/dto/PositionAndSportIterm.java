package com.fushuhealth.recovery.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionAndSportIterm {

    private Integer questionSn;

    private String name;

    private Integer status;

    private Integer optionSn;
}
