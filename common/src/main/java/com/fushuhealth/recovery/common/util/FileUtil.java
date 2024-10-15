package com.fushuhealth.recovery.common.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static final List<String> VIDEO_TYPE = Arrays.asList(("MP4"));

    public static boolean isVideo(String fileName) {
        return VIDEO_TYPE.contains(FilenameUtils.getExtension(fileName).toUpperCase());
    }

    public static void main(String[] args) {
        System.out.println(isVideo("1626697060465_986a01323eb9429f82a9b338e6af660d_2055999258.mp4"));
    }
}
