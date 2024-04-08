package org.loong.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(SysMenu)表服务接口
 *
 * @author loong
 * @since 2024-03-17 20:56:46
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
