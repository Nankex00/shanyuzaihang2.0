package com.fushuhealth.recovery.common.storage;

import com.fushuhealth.recovery.common.util.PathUtil;
import com.fushuhealth.recovery.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public abstract class FilePathPolicy {

    public String generateFilePath(OldFileType type, File file) {
        throw new UnsupportedOperationException();
    }

    protected String generateFilePath(OldFileType type) {
        String rand = String.valueOf(Math.abs(new Random(System.currentTimeMillis()).nextInt()));

        String uuid = StringUtils.replaceAll(UUID.randomUUID().toString(), "-", "");

        String filename = StringUtils.joinWith("_", System.currentTimeMillis(), uuid, rand);

        filename = StringUtils.joinWith(".", filename, type.getName());

        return PathUtil.concatPaths(FastDateFormat.getInstance("yyyy/MM/dd").format(new Date()), filename);
    }

    public String generateFilePath(String filename) {
//        String uuid = StringUtils.joinWith("_", System.currentTimeMillis(),
//                StringUtils.replaceAll(UUID.randomUUID().toString(), "-", ""),
//                String.valueOf(Math.abs(new Random(System.currentTimeMillis()).nextInt())));
        return PathUtil.concatPaths(FastDateFormat.getInstance("yyyy/MM/dd").format(new Date()), StringUtil.uuid(), filename);
    }
}
