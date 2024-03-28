package org.loong.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.UserRole;
import org.loong.mapper.UserRoleMapper;
import org.loong.service.UserRoleService;
import org.springframework.stereotype.Service;
/**
 * 用户和角色关联表(SysUserRole)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 21:01:09
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
