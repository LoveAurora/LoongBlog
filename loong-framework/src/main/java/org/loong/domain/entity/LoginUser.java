package org.loong.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// 使用@Data注解，Lombok会为这个类提供getter、setter、equals、hashCode和toString方法
// 使用@AllArgsConstructor注解，Lombok会为这个类提供一个包含所有字段的构造方法
// 使用@NoArgsConstructor注解，Lombok会为这个类提供一个无参数的构造方法
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    // 用户对象
    private User user;

    // 用户权限列表
    private List<String> permissions;

    // 返回用户的权限，这里暂时返回null
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 返回用户的密码
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 返回用户名
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    // 账户是否未过期，这里暂时返回true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未锁定，这里暂时返回true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 凭证（密码）是否未过期，这里暂时返回true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否启用，这里暂时返回true
    @Override
    public boolean isEnabled() {
        return true;
    }
}