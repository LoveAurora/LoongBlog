package org.loong.service.impl;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.LoginUser;
import org.loong.domain.entity.User;
import org.loong.domain.vo.BlogUserLoginVo;
import org.loong.domain.vo.UserInfoVo;
import org.loong.service.BlogLoginService;
import org.loong.utils.BeanCopyUtils;
import org.loong.utils.JwtUtil;
import org.loong.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("blogLoginService")
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        // UsernamePasswordAuthenticationToken是Authentication接口的一个实现
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        // authenticate()接收一个Authentication对象作为参数，然后尝试对其进行身份验证
        // 如果身份验证成功，authenticate()方法将返回一个完全填充的Authentication对象；
        // 如果身份验证失败，它将抛出一个AuthenticationException。
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // Authentication 是一个接口，代表了一个身份验证请求或身份验证结果。
        // 在身份验证请求中，Authentication对象通常包含请求的主体（如用户名）和凭证（如密码）。

        // 获取userid 生成token
        /*
        在Spring Security中，UsernamePasswordAuthenticationToken的构造函数接收两个参数：principal和credentials。
        这里的principal通常是用户名，credentials通常是密码。
        这个Authentication对象被传递给AuthenticationManager的authenticate方法进行身份验证。
        当身份验证成功后，AuthenticationManager返回一个填充完整的Authentication对象。
        这个对象的getPrincipal方法返回的是身份验证的主体，也就是已经验证的用户信息，而不仅仅是用户名。
        在你的代码中，LoginUser对象就是这个主体，它包含了用户的详细信息，如用户名、密码、权限等。
        所以，authenticate.getPrincipal()返回的是一个LoginUser对象，而不是用户名。
        这是因为在身份验证过程中，UserDetailsService的loadUserByUsername方法返回了一个UserDetails对象（在你的代码中是LoginUser对象），
        这个对象包含了用户的详细信息，并被设置为Authentication对象的主体。
         */
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject("bloglogin" + userId, loginUser);
        // 把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //把token和 userinfo 返回给前端
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.successResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取userid
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("bloglogin" + userid);
        return ResponseResult.successResult();
    }
}
