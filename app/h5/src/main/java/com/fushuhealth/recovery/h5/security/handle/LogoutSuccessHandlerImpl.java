package com.fushuhealth.recovery.h5.security.handle;

import com.alibaba.fastjson.JSON;
import com.fushuhealth.recovery.h5.core.service.TokenService;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author Zhuanz
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
//        LoginUser loginUser = tokenService.getLoginUser(request);
//        if (StringUtils.isNotNull(loginUser))
//        {
//            String userName = loginUser.getUsername();
            // 删除用户缓存记录
//            tokenService.delLoginUser(loginUser.getToken(),request);
//            // 记录用户退出日志
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
//        }
        //todo:修改为国际化消息插件
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success("登出成功")));
    }
}
