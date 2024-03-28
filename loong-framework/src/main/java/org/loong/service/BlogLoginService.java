package org.loong.service;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
