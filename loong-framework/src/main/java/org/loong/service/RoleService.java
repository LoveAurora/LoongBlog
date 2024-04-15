package org.loong.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(SysRole)表服务接口
 *
 * @author loong
 * @since 2024-03-17 20:51:00
 */
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);

    void updateRole(Role role);

    void addRole(Role role);

    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);
}
