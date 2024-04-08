package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.LoginUser;
import org.loong.domain.entity.Menu;
import org.loong.domain.vo.AdminUserInfoVo;
import org.loong.domain.entity.User;
import org.loong.domain.vo.RoutersVo;
import org.loong.domain.vo.UserInfoVo;
import org.loong.enums.AppHttpCodeEnum;
import org.loong.service.MenuService;
import org.loong.service.RoleService;
import org.loong.service.SystemLoginService;
import org.loong.service.UserService;
import org.loong.utils.BeanCopyUtils;
import org.loong.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.loong.handler.exception.SystemException;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private SystemLoginService systemLoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/user/login")
    public ResponseResult login(@RequestBody User user) throws Exception {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        //获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        // 根据用户id获取权限信息
        List<String> permissions = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户id获取角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 封装成AdminUserInfoVo
        AdminUserInfoVo resultVo = new AdminUserInfoVo(permissions, roles, userInfoVo);
        return ResponseResult.successResult(resultVo);
    }


    @GetMapping("/user/logout")
    public ResponseResult logout() {
        return systemLoginService.logout();
    }


   @GetMapping("getRouters")
    public ResponseResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        // 查询所有菜单信息
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 封装数据返回
        return ResponseResult.successResult(new RoutersVo(menus));
    }
}
