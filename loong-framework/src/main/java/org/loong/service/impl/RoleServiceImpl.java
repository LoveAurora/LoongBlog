package org.loong.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.Role;
import org.loong.mapper.RoleMapper;
import org.loong.service.RoleService;
import org.springframework.stereotype.Service;
/**
 * 角色信息表(Role)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 21:01:57
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
