package org.loong.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 角色和菜单关联表(SysRoleMenu)表实体类
 *
 * @author loong
 * @since 2024-03-17 20:50:37
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu {
//角色ID@TableId
private Long roleId;
//菜单ID@TableId
private Long menuId;
}
