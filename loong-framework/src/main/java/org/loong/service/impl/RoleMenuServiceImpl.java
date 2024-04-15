package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.RoleMenu;
import org.loong.mapper.RoleMenuMapper;
import org.loong.service.RoleMenuService;
import org.springframework.stereotype.Service;
/**
 * 角色和菜单关联表(SysRoleMenu)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 20:58:19
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    @Override
    public void deleteRoleMenuByRoleId(Long id) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        remove(queryWrapper);
    }
}
