package com.fushuhealth.recovery.common.exception;


/**
 * 用户信息异常类
 *
 * @author Zhuanz
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
