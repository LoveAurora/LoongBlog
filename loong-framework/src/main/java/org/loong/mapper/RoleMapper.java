package org.loong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.loong.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author loong
 * @since 2024-03-17 21:12:32
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID查询对应的角色键
     * @param userId 用户ID
     * @return 角色键
     */
    List<String> selectRoleKeyByUserId(Long userId);

    /**
     * 根据用户ID查询对应的角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    List<Long> selectRoleIdByUserId(Long userId);
}