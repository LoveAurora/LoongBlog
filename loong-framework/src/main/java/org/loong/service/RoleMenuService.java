package org.loong.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.entity.RoleMenu;

/**
 * 角色和菜单关联表(SysRoleMenu)表服务接口
 *
 * @author loong
 * @since 2024-03-17 20:58:18
 */
public interface RoleMenuService extends IService<RoleMenu> {
    void deleteRoleMenuByRoleId(Long id);
}
