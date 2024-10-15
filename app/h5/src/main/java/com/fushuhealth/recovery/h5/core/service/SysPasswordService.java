package com.fushuhealth.recovery.h5.core.service;

import com.fushuhealth.recovery.common.core.domin.SysUser;
import org.springframework.stereotype.Component;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@Component
public class SysPasswordService {
    public void validate(SysUser user)
    {
//        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
//        String username = usernamePasswordAuthenticationToken.getName();
//        String password = usernamePasswordAuthenticationToken.getCredentials().toString();
//
//        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));
//        if (retryCount == null)
//        {
//            retryCount = 0;
//        }
//
//        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
//        {
//            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
//        }
//
//        if (!matches(user, password))
//        {
//            retryCount = retryCount + 1;
//            redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
//            throw new UserPasswordNotMatchException();
//        }
//        else
//        {
//            clearLoginRecordCache(username);
//        }
    }

}
