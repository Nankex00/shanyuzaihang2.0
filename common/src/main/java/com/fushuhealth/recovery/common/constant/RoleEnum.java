package com.fushuhealth.recovery.common.constant;

public enum RoleEnum {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
