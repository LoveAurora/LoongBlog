package org.loong.controller;

import org.loong.annotation.SystemLog;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.User;
import org.loong.service.UploadService;
import org.loong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UploadService uploadService;

    @GetMapping("/userInfo")
    @SystemLog(businessName = "获取用户信息")
    public ResponseResult userInfo() {
        return userService.getUserInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return userService.register(user);
    }

}
