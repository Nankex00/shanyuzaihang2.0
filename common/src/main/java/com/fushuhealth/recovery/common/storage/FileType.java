package com.fushuhealth.recovery.common.storage;


public enum FileType {

    JPG("JPG"),
    PNG("PNG"),
    STL("STL"),
    UNKNOWN("UNKNOWN");

    public static FileType getType(String type) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getType().equalsIgnoreCase(type)) {
                return fileType;
            }
        }
        return UNKNOWN;
    }

    private String type;

    FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
