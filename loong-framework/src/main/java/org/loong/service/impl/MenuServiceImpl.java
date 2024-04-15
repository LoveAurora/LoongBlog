package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.constants.SystemConstants;
import org.loong.domain.entity.Menu;
import org.loong.mapper.MenuMapper;
import org.loong.service.MenuService;
import org.loong.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(SysMenu)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 20:56:47
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 根据用户ID选择权限。如果用户是管理员，返回所有权限。否则，返回用户具有的权限。
     *
     * @param id 用户ID。
     * @return 权限列表。如果用户是管理员，返回所有权限。否则，返回用户具有的权限。
     */
    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON); // 选择菜单类型为MENU或BUTTON的菜单
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL); // 选择状态为正常的菜单
            List<Menu> menus = list(wrapper); // 获取满足条件的菜单列表
            List<String> perms = menus.stream().map(Menu::getPerms) // 获取每个菜单的权限
                    .collect(Collectors.toList()); // 收集结果到一个新的列表中
            return perms;
        }
        // 否则返回所具有的权限
        return getBaseMapper().selectMenuPermsById(id); // 调用mapper方法获取用户具有的权限
    }

    /**
     * 根据用户ID选择路由菜单树。如果用户是管理员，返回所有菜单。否则，返回用户具有的菜单。
     *
     * @param userId 用户ID。
     * @return 菜单列表。如果用户是管理员，返回所有菜单。否则，返回用户具有的菜单。
     */
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper(); // 获取mapper
        List<Menu> menus = null;

        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRouterMenu(); // 如果是管理员，选择所有的路由菜单
        } else {
            menus = menuMapper.selectRouterMenuTreeByUserId(userId); // 否则，选择用户具有的路由菜单
        }
        List<Menu> menuTree = buildMenuTree(menus, 0L); // 构建菜单树

        return menuTree; // 返回菜单树
    }


    /**
     * 构建菜单树的方法。这个方法接收一个菜单列表和一个父菜单ID作为参数，然后返回一个新的菜单列表，其中只包含父菜单ID等于给定ID的菜单项，并且每个菜单项的子菜单已经被设置。
     *
     * @param menus    菜单列表。这个列表包含了所有的菜单项。
     * @param parentId 父菜单ID。这个方法会返回一个新的菜单列表，其中只包含父菜单ID等于这个ID的菜单项。
     * @return 一个新的菜单列表，其中只包含父菜单ID等于给定ID的菜单项，并且每个菜单项的子菜单已经被设置。
     */
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream().filter(menu -> menu.getParentId().equals(parentId)) // 过滤出父菜单ID等于给定ID的菜单项
                .map(menu -> menu.setChildren(getChildren(menu, menus))) // 为每个菜单项设置子菜单
                .collect(Collectors.toList()); // 收集结果到一个新的列表中
        return menuTree;
    }

    /**
     * 获取子菜单的方法。这个方法接收一个菜单和一个菜单列表作为参数，然后返回一个新的菜单列表，其中只包含父菜单ID等于给定菜单的ID的菜单项，并且每个菜单项的子菜单已经被设置。
     *
     * @param menu  菜单。这个方法会返回一个新的菜单列表，其中只包含父菜单ID等于这个菜单的ID的菜单项。
     * @param menus 菜单列表。这个列表包含了所有的菜单项。
     * @return 一个新的菜单列表，其中只包含父菜单ID等于给定菜单的ID的菜单项，并且每个菜单项的子菜单已经被设置。
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        // 使用Java 8的流操作，对菜单列表进行处理
        List<Menu> childrenList = menus.stream()
                // 过滤出父菜单ID等于给定菜单的ID的菜单项
                .filter(m -> m.getParentId().equals(menu.getId()))
                // 为每个菜单项设置子菜单
                .map(m -> m.setChildren(getChildren(m, menus)))
                // 收集结果到一个新的列表中
                .collect(Collectors.toList());
        // 返回处理后的菜单列表
        return childrenList;
    }

    /**
     * 根据给定的菜单信息查询菜单列表。查询条件包括菜单名（模糊查询）和状态，结果按照父菜单ID和排序号进行升序排序。
     *
     * @param menu 菜单信息。包括菜单名和状态，用于构建查询条件。
     * @return 符合条件的菜单列表。
     */
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        // 创建一个LambdaQueryWrapper对象，用于构建查询条件
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 如果菜单名不为空，进行模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        // 如果状态不为空，进行状态查询
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        // 按照父菜单ID和排序号进行升序排序
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        // 执行查询并返回结果
        List<Menu> menus = list(queryWrapper);
        return menus;
    }

    /**
     * 判断给定的菜单ID是否有子菜单。
     *
     * @param menuId 菜单ID。用于查询父菜单ID为给定值的菜单数量。
     * @return 如果数量大于0，返回true，表示有子菜单；否则返回false，表示没有子菜单。
     */
    @Override
    public boolean hasChild(Long menuId) {
        // 创建一个LambdaQueryWrapper对象，用于构建查询条件
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 查询父菜单ID为给定值的菜单数量
        queryWrapper.eq(Menu::getParentId, menuId);
        // 如果数量大于0，返回true，否则返回false
        if (count(queryWrapper) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据角色ID查询菜单列表。
     *
     * @param roleId 角色ID。用于查询角色对应的菜单列表。
     * @return 角色对应的菜单ID列表。
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        // 调用mapper方法，根据角色ID查询菜单列表，并返回结果
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

}
