package com.fushuhealth.recovery.device.core.service;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;
import com.fushuhealth.recovery.common.constant.Constants;
import com.fushuhealth.recovery.common.constant.SessionConstants;
import com.fushuhealth.recovery.common.core.domin.LoginUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.fushuhealth.recovery.common.util.IdUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * token验证处理
 *
 * @author Zhuanz
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

//    @Autowired
//    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);

                //采用Session存储数据对象
                HttpSession session = request.getSession();
                if (ObjectUtil.equals(session.getAttribute("userKey"),userKey)){
                    return (LoginUser) session.getAttribute("loginUser");
                }else {
                    throw new ServiceException();
                }

            }
            catch (Exception e)
            {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

//    /**
//     * 设置用户身份信息
//     */
//    public void setLoginUser(LoginUser loginUser)
//    {
//        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
//        {
//            refreshToken(loginUser);
//        }
//    }
//
    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token,HttpServletRequest request)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
//            redisCache.deleteObject(userKey);
            HttpSession session = request.getSession();
            session.invalidate();

        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser,HttpServletRequest request)
    {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
//        setUserAgent(loginUser);
        refreshToken(loginUser,request);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

//    /**
//     * 创建永不过期令牌
//     *
//     * @param loginUser 用户信息
//     * @return 令牌
//     */
//    public String createPermanentToken(LoginUser loginUser)
//    {
//        //删除之前的token
//        redisCache.deleteObject(getTokenKey(String.valueOf(loginUser.getUserId())));
//        String token = IdUtils.fastUUID();
//        loginUser.setToken(token);
//        setUserAgent(loginUser);
//        refreshPermanentToken(loginUser);
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(Constants.LOGIN_USER_KEY, loginUser.getUserId().toString());
//        return createToken(claims);
//    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser,HttpServletRequest request)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser,request);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser,HttpServletRequest request)
    {
//        loginUser.setLoginTime(System.currentTimeMillis());
//        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
//        String userKey = getTokenKey(loginUser.getToken());
//        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        // 将更新后的 loginUser 存储在 Session 中
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("loginTime",System.currentTimeMillis());
        httpSession.setAttribute("expireTime",loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        httpSession.setAttribute("loginUser", loginUser);
        httpSession.setAttribute("userKey",getTokenKey(loginUser.getToken()));
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
//    public void refreshPermanentToken(LoginUser loginUser)
//    {
//        loginUser.setLoginTime(System.currentTimeMillis());
//        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
//        // 根据uuid将loginUser缓存
//        String userKey =  getTokenKey(String.valueOf(loginUser.getUserId()));
//        redisCache.setCacheObject(userKey, loginUser);
//    }

//    /**
//     * 设置用户代理信息
//     *
//     * @param loginUser 登录信息
//     */
//    public void setUserAgent(LoginUser loginUser)
//    {
//        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
//        String ip = IpUtils.getIpAddr();
//        loginUser.setIpaddr(ip);
//        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
//        loginUser.setBrowser(userAgent.getBrowser().getName());
//        loginUser.setOs(userAgent.getOperatingSystem().getName());
//    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return SessionConstants.LOGIN_TOKEN_KEY + uuid;
    }

}
