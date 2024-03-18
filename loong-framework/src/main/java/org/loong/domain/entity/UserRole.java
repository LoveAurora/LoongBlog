package org.loong.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户和角色关联表(SysUserRole)表实体类
 *
 * @author loong
 * @since 2024-03-17 20:50:11
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_role")
public class UserRole {
//用户ID@TableId
private Long userId;
//角色ID@TableId
private Long roleId;
}
