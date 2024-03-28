package org.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.User;
import org.loong.mapper.UserMapper;
import org.loong.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 21:00:31
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
