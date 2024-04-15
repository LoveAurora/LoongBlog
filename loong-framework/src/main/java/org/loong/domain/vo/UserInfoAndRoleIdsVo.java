package org.loong.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.loong.domain.entity.Role;
import org.loong.domain.entity.User;

import java.util.List;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private User user;
    private List<Role> roles;
    private List<Long> roleIds;



}
