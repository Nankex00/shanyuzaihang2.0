package com.fushuhealth.recovery.dal.context;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhuanz
 * @date 2024/10/3
 */
@Component
public class RolePermissionContext {

    private static RolePermissionContext instance;

    private Map<String, Object> context = new HashMap<>();

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static RolePermissionContext getInstance() {
        return instance;
    }

    public Map<String, Object> getContext() {
        return context;
    }
}
