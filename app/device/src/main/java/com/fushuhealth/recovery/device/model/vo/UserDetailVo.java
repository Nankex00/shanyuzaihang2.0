package com.fushuhealth.recovery.device.model.vo;

import com.fushuhealth.recovery.common.core.domin.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class UserDetailVo implements UserDetails {

    private Long id;

    private Long institutionId;

    private Long roleId;

    private String token;

    private Set<String> permissions;

    private SysUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getUserName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
