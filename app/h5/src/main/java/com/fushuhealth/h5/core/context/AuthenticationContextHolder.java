package com.fushuhealth.h5.core.context;

import org.springframework.security.core.Authentication;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> contextHolder = new ThreadLocal<>();

    public static Authentication getContext()
    {
        return contextHolder.get();
    }

    public static void setContext(Authentication context)
    {
        contextHolder.set(context);
    }

    public static void clearContext()
    {
        contextHolder.remove();
    }
}
