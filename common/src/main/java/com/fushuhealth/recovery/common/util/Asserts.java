package com.fushuhealth.recovery.common.util;

import org.apache.commons.lang3.StringUtils;

public class Asserts {

    public static void assertNotNull(final Object... objs) {
        if (objs == null) {
            throw new IllegalArgumentException();
        }
        for (final Object obj : objs) {
            if (obj == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertStringNotBlank(final String... strs) {
        if (strs == null) {
            throw new IllegalArgumentException();
        }
        for (final String str : strs) {
            if (StringUtils.isBlank(str)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertGreaterThanZero(final long... nums) {
        if (nums == null) {
            throw new IllegalArgumentException();
        }
        for (final long n : nums) {
            if (n < 1) {
                throw new IllegalArgumentException();
            }
        }
    }
}
