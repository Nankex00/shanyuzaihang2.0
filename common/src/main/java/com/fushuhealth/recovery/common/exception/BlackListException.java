package com.fushuhealth.recovery.common.exception;

/**
 * 黑名单IP异常类
 *
 * @author Zhuanz
 */
public class BlackListException extends UserException
{
    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
