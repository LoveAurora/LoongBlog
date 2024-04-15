package org.loong.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.User;

/**
 * 用户表(SysUser)表服务接口
 *
 * @author loong
 * @since 2024-03-17 20:50:24
 */
public interface UserService extends IService<User> {
    ResponseResult getUserInfo( );

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(User user);

    boolean checkEmailUnique(User user);

    ResponseResult addUser(User user);

    void updateUser(User user);
}
