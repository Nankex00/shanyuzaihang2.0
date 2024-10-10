package com.fushuhealth.recovery.common.storage;

import java.io.File;

public class DefaultFilePathPolicy extends FilePathPolicy {

    @Override
    public String generateFilePath(OldFileType type, File file) {
        return generateFilePath(type);
    }
}
