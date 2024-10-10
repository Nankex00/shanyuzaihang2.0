package com.fushuhealth.recovery.dal.handler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.util.List;

public class LongListTypeHandler extends JacksonTypeHandler {
    public LongListTypeHandler(Class<?> type) {
        super(type);
    }
    // 可以根据需要重写父类的方法
}
