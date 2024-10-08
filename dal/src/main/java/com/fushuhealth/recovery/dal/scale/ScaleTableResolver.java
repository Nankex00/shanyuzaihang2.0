package com.fushuhealth.recovery.dal.scale;

import com.fushuhealth.recovery.dal.entity.ScaleTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScaleTableResolver {

    private final static Logger log = LoggerFactory.getLogger(ScaleTableResolver.class);

    private static String scaleTableConfigFilePath;

    private static volatile Map<Byte, ScaleTable> scaleTableMap;

    @Value("${scale-table.file:}")
    public void setScaleTableConfigFilePath(String filePath) {
        scaleTableConfigFilePath = filePath;
    }

    public static Map<Byte, ScaleTable> getScaleTableMap() {

        if (scaleTableMap == null) {
            synchronized (ScaleTableResolver.class) {
                scaleTableMap = new HashMap<Byte, ScaleTable>();
                if (StringUtils.isNotBlank(scaleTableConfigFilePath)) {
                    File file = new File(scaleTableConfigFilePath);
                    try {
                        final String content = FileUtils.readFileToString(file, "UTF-8");
                        List<ScaleTable> scaleTables = JSON.parseObject(content, new TypeReference<List<ScaleTable>>() {});

                        for (ScaleTable scaleTable : scaleTables) {
                            scaleTableMap.put(scaleTable.getCode(), scaleTable);
                        }
                    } catch (IOException e) {
                        log.error("parse scale table map error : {}", e);
                    }
                }
            }

        }
        return scaleTableMap;
    }
}
