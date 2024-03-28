package org.loong.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.loong.domain.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 用户表(User)表数据库访问层
 *
 * @author loong
 * @since 2024-03-17 21:29:27
 */
public interface UserMapper extends BaseMapper<User> {
}
