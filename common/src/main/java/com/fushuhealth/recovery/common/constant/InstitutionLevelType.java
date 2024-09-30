package com.fushuhealth.recovery.common.constant;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */

@Getter
public enum InstitutionLevelType {
    ROOT((byte)0,"系统管理员"),
    CITY((byte)1,"市级"),
    PREFECTURE((byte)2,"区县级"),
    COMMUNITY((byte)3,"社区级");

    private Byte type;
    private String institutionLevel;

    InstitutionLevelType(Byte type,String institutionLevel){
        this.type = type;
        this.institutionLevel = institutionLevel;
    }

}
