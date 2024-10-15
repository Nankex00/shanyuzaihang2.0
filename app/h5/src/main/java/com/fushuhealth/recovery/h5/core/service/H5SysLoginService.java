package com.fushuhealth.recovery.h5.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.h5.core.context.AuthenticationContextHolder;
import com.fushuhealth.recovery.common.core.domin.LoginUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


/**
 * @author Zhuanz
 * @date 2024/9/23
 */
@Component
public class H5SysLoginService {

    @Autowired
    private H5TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid,HttpServletRequest request)
    {
        String message = (String) request.getSession().getAttribute(username);
        if (!message.equals(code)){
            throw new ServiceException("登录异常，验证码错误");
        }
//        boolean captchaEnabled = configService.selectCaptchaEnabled();
//        if (captchaEnabled)
//        {
//            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
//            String captcha = redisCache.getCacheObject(verifyKey);
//            redisCache.deleteObject(verifyKey);
//            if (captcha == null)
//            {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//                throw new CaptchaExpireException();
//            }
//            if (!code.equalsIgnoreCase(captcha))
//            {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//                throw new CaptchaException();
//            }
//        }

    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
//        SysUser sysUser = new SysUser();
//        sysUser.setUserId(userId);
//        sysUser.setLoginIp(IpUtils.getIpAddr());
//        sysUser.setLoginDate(DateUtils.getNowDate());
//        userService.updateUserProfile(sysUser);
    }

    /**
     * 验证用户是否存在，如果不存在则创建用户
     */
    public SysUser checkUserIsExist(String username){
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUserName,username);
        return sysUserMapper.selectUserByUserName(username);
    }

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String code, String uuid, HttpServletRequest request)
    {
        // 验证码校验
        validateCaptcha(username, code, uuid,request);
        //验证用户存在逻辑
        SysUser sysUser = checkUserIsExist(username);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, sysUser.getPassword());
            AuthenticationContextHolder.setContext(authenticationToken);
//             该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
            throw new ServiceException(e.getMessage());
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
//        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        loginUser.setLoginTime(System.currentTimeMillis());
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser,request);
    }
}
