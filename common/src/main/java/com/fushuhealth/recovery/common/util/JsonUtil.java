package com.fushuhealth.recovery.common.util;

import com.alibaba.fastjson.parser.ParserConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private JsonUtil() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        //禁用自动类型 https://github.com/alibaba/fastjson/wiki/fastjson_safemode
        ParserConfig.getGlobalInstance().setSafeMode(true);
    }

    public static ObjectMapper getMapper() {
        return OBJECT_MAPPER;
    }

}
