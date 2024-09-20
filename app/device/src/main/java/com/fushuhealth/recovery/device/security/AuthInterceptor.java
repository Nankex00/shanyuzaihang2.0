package com.fushuhealth.recovery.device.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.crypto.Sign;
import com.fushuhealth.recovery.common.error.NotLoginException;
import com.fushuhealth.recovery.device.config.AppProps;
import com.fushuhealth.recovery.device.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements InitializingBean, HandlerInterceptor {

    private final static Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private AppProps appProps;

    @Autowired
    private UserService userService;
//
//    @Autowired
//    private LocalCache<String, UserDetailVo> userCache;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Session session = getSessionFromHeader(request);
        if (session == null) {
            throw new NotLoginException(ResultCode.USER_NOT_LOGIN);
        }
//        UserDetailVo userDetail = userCache.get(session.getUserId());
//        if (userDetail == null) {
//            userDetail = userService.findUser(Integer.valueOf(session.getUserId()));
//        }
//        UserDetailVo userDetail = userService.findUser(Integer.valueOf(session.getUserId()));
//        request.setAttribute(AuthConstant.CURRENT_USER_HEADER, userDetail);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private Session getSessionFromCookie(HttpServletRequest request) {
        String token = Sessions.getTokenFromCookie(request);
        return getSession(token);
    }

    private Session getSessionFromHeader(HttpServletRequest request) {
        String token = Sessions.getTokenFromHeader(request);
        return getSession(token);
    }

    private Session getSession(String token) {
        if (token == null) return null;
        try {
            DecodedJWT decodedJWT = Sign.verifySessionToken(token, appProps.getSigningSecret());
            String userId = decodedJWT.getClaim(Sign.CLAIM_USER_ID).asString();
            Session session = Session.builder().userId(userId).build();
            return session;
        } catch (Exception e) {
//            log.error("fail to verify token", "token", token, e);
            return null;
        }
    }



    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Session {
        private String userId;
    }
}
