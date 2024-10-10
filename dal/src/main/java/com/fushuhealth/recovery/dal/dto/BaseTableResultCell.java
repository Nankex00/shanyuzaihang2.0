package com.fushuhealth.recovery.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseTableResultCell {

    private String order;

    private String name;

    private String value;

    private String type;
}
