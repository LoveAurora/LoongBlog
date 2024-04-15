package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.constants.SystemConstants;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Role;
import org.loong.domain.entity.RoleMenu;
import org.loong.domain.vo.PageVo;
import org.loong.mapper.RoleMapper;
import org.loong.service.RoleMenuService;
import org.loong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 21:01:57
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public List<Role> selectRoleAll() {
        //查询状态为正常的角色
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>();
        queryWrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        List<Role> roleList = list(queryWrapper);
        return roleList;
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }

    @Override
    public void updateRole(Role role) {
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMenu(role);
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        save(role);
        System.out.println(role.getId());
        if (role.getMenuIds() != null && role.getMenuIds().length > 0) {
            insertRoleMenu(role);
        }
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }

    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName());
        queryWrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.successResult(pageVo);
    }
}
