package com.fushuhealth.recovery.common.util;

import java.util.UUID;

public class StringUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
