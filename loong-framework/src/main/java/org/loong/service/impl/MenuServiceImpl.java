package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.constants.SystemConstants;
import org.loong.domain.entity.Menu;
import org.loong.mapper.MenuMapper;
import org.loong.service.MenuService;
import org.loong.utils.SecurityUtils;
import org.springframework.stereotype.Service;

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
        List<String> perms = menus.stream()
                .map(Menu::getPerms) // 获取每个菜单的权限
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
    List<Menu> menus =  null;

    if (SecurityUtils.isAdmin()) {
        menus= menuMapper.selectAllRouterMenu(); // 如果是管理员，选择所有的路由菜单
    }else {
        menus = menuMapper.selectRouterMenuTreeByUserId(userId); // 否则，选择用户具有的路由菜单
    }
    List<Menu> menuTree = buildMenuTree(menus,0L); // 构建菜单树

    return menuTree; // 返回菜单树
}

/**
 * 构建菜单树的方法。这个方法接收一个菜单列表和一个父菜单ID作为参数，然后返回一个新的菜单列表，其中只包含父菜单ID等于给定ID的菜单项，并且每个菜单项的子菜单已经被设置。
 *
 * @param menus 菜单列表。这个列表包含了所有的菜单项。
 * @param parentId 父菜单ID。这个方法会返回一个新的菜单列表，其中只包含父菜单ID等于这个ID的菜单项。
 * @return 一个新的菜单列表，其中只包含父菜单ID等于给定ID的菜单项，并且每个菜单项的子菜单已经被设置。
 */
private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
    List<Menu> menuTree = menus.stream()
            .filter(menu -> menu.getParentId().equals(parentId)) // 过滤出父菜单ID等于给定ID的菜单项
            .map(menu -> menu.setChildren(getChildren(menu, menus))) // 为每个菜单项设置子菜单
            .collect(Collectors.toList()); // 收集结果到一个新的列表中
    return menuTree;
}

/**
 * 获取子菜单的方法。这个方法接收一个菜单和一个菜单列表作为参数，然后返回一个新的菜单列表，其中只包含父菜单ID等于给定菜单的ID的菜单项，并且每个菜单项的子菜单已经被设置。
 *
 * @param menu 菜单。这个方法会返回一个新的菜单列表，其中只包含父菜单ID等于这个菜单的ID的菜单项。
 * @param menus 菜单列表。这个列表包含了所有的菜单项。
 * @return 一个新的菜单列表，其中只包含父菜单ID等于给定菜单的ID的菜单项，并且每个菜单项的子菜单已经被设置。
 */
private List<Menu> getChildren(Menu menu, List<Menu> menus) {
    List<Menu> childrenList = menus.stream()
            .filter(m -> m.getParentId().equals(menu.getId())) // 过滤出父菜单ID等于给定菜单的ID的菜单项
            .map(m -> m.setChildren(getChildren(m, menus))) // 为每个菜单项设置子菜单
            .collect(Collectors.toList()); // 收集结果到一个新的列表中
    return childrenList;
}
}
