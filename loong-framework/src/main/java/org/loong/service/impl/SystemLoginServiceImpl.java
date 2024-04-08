package org.loong.service.impl;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.LoginUser;
import org.loong.domain.entity.User;
import org.loong.domain.vo.BlogUserLoginVo;
import org.loong.domain.vo.UserInfoVo;
import org.loong.service.SystemLoginService;
import org.loong.utils.BeanCopyUtils;
import org.loong.utils.JwtUtil;
import org.loong.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service("loginService")
public class SystemLoginServiceImpl implements SystemLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject("login" + userId, loginUser);
        //把token返回给前端
        Map<String, String> map =new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.successResult(map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取userid
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login" + userid);
        return ResponseResult.successResult();
    }
}
