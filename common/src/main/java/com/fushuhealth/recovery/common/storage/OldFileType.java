package com.fushuhealth.recovery.common.storage;

import org.apache.commons.lang3.StringUtils;

public enum OldFileType {

    ERROR((byte) -1, "", ""),
    OTHER((byte) 0, "oth", ""),
    PICTURE((byte) 1, "picture", "picture"),
    VIDEO((byte) 2, "video", "video"),
    AUDIO((byte) 3, "audio", "audio"),
    TMP((byte) 4, "tmp", ""),
    JSON((byte) 5, "json", "json"),
    XML((byte) 6, "xml", "xml"),
    ZIP((byte) 8, "zip", "zip"),
    PDF((byte) 9, "pdf", "pdf");


    private final byte code;
    private final String name;
    private final String ext;

    OldFileType(final byte code, final String name, final String ext) {
        this.code = code;
        this.name = name;
        this.ext = ext;
    }

    public static OldFileType getType(byte c) {
        for (OldFileType t : OldFileType.values()) {
            if (t.code == c) {
                return t;
            }
        }
        return OldFileType.OTHER;
    }

    public static OldFileType getType(String name) {
        for (OldFileType t : OldFileType.values()) {
            if (StringUtils.equals(t.name, name)) {
                return t;
            }
        }
        return OldFileType.OTHER;
    }

    public static OldFileType getTypeByExt(String ext) {
        for (OldFileType t : OldFileType.values()) {
            if (StringUtils.equalsIgnoreCase(t.ext, ext)) {
                return t;
            }
        }
        return OldFileType.OTHER;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }
}
