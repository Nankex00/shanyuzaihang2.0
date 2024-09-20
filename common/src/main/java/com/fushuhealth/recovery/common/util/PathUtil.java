package com.fushuhealth.recovery.common.util;

import org.apache.commons.lang3.StringUtils;

public class PathUtil {

    public static String concatPaths(String... paths) {
        if (paths != null && paths.length > 0) {
            String[] ps = new String[paths.length];
            for (int i = 0; i < paths.length; i++) {
                if (i == 0) {
                    ps[i] = StringUtils.stripEnd(paths[i], "/");
                } else {
                    ps[i] = StringUtils.strip(paths[i], "/");
                }
            }
            return String.join("/", ps);
        }
        return null;
    }

}
