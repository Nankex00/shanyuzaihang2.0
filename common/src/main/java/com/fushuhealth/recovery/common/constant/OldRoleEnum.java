package com.fushuhealth.recovery.common.constant;

public enum OldRoleEnum {

    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ROLE_MECHANISM_ADMIN("ROLE_MECHANISM_ADMIN"),
    ROLE_DOCTOR("ROLE_DOCTOR"),
    ROLE_PATIENT("ROLE_PATIENT"),
    ROLE_THERAPIST("ROLE_THERAPIST"),
    UNKNOWN("UNKNOWN");

    public OldRoleEnum getRole(String roleName) {
        for (OldRoleEnum roleEnum : OldRoleEnum.values()) {
            if (roleEnum.getName().equalsIgnoreCase(roleName)) {
                return roleEnum;
            }
        }
        return UNKNOWN;
    }

    private String name;

    OldRoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
