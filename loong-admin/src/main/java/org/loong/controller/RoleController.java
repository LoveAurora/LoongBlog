package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.domain.dto.ChangeRoleStatusDto;
import org.loong.domain.entity.Role;
import org.loong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> roles = roleService.selectRoleAll();
        return ResponseResult.successResult(roles);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    public ResponseResult getInfo(@PathVariable Long roleId) {
        Role role = roleService.getById(roleId);
        return ResponseResult.successResult(role);
    }

    /**
     * 修改保存角色
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseResult.successResult();
    }

    /**
     * 删除角色
     *
     * @param ids
     */
    @DeleteMapping("/{ids}")
    public ResponseResult remove(@PathVariable List<Long> ids) {
        roleService.removeByIds(ids);
        return ResponseResult.successResult();
    }

    /**
     * 新增角色
     */
    @PostMapping
    public ResponseResult addRole(@RequestBody Role role) {
        roleService.addRole(role);

        return ResponseResult.successResult();
    }

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(role, pageNum, pageSize);
    }

    /**
     * 修改角色状态
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto) {
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        return ResponseResult.successResult(roleService.updateById(role));
    }

}
