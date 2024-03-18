package org.loong.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.Menu;
import org.loong.mapper.MenuMapper;
import org.loong.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * 菜单权限表(SysMenu)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 20:56:47
 */
@Service("sysMenuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
