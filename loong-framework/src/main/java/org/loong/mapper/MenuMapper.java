package org.loong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.loong.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 * 这个接口继承自MyBatis Plus的BaseMapper，提供了一些基本的数据库操作方法。
 * 我们也可以在这个接口中定义自己的方法。
 *
 * @author loong
 * @since 2024-03-17 20:49:56
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询菜单权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsById(Long userId);

    /**
     * 查询所有的路由菜单
     * @return 菜单列表
     */
    List<Menu> selectAllRouterMenu();

    /**
     * 根据用户ID查询路由菜单树
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuListByRoleId(Long roleId);
}