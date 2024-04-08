package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.loong.domain.entity.LoginUser;
import org.loong.mapper.MenuMapper;
import org.loong.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.loong.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, s);
        User user = userMapper.selectOne(queryWrapper);
        // 如果用户不存在，抛出UsernameNotFoundException异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        // 如果用户存在，返回UserDetails对象
//        if (user.getType().equals(SystemConstants.ADMAIN)) {
//            List<String> list = menuMapper.selectPermsByUserId(user.getId());
//            return new LoginUser(user, list);
//        }
        // Todo：查询权限信息封装
        return new LoginUser(user, null);
    }
}
