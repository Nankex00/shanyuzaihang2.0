package com.fushuhealth.recovery.device.security.filter;

import java.io.IOException;
import java.io.Serializable;

import com.alibaba.fastjson2.JSON;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.HttpStatus;
import com.fushuhealth.recovery.common.util.ServletUtils;
import com.fushuhealth.recovery.common.util.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证失败处理类 返回未授权
 *
 * @author Zhuanz
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
