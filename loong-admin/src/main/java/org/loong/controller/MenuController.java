package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Menu;
import org.loong.domain.vo.MenuTreeVo;
import org.loong.domain.vo.MenuVo;
import org.loong.domain.vo.RoleMenuTreeSelectVo;
import org.loong.service.MenuService;
import org.loong.utils.BeanCopyUtils;
import org.loong.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.successResult(options);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys, menuTreeVos);
        return ResponseResult.successResult(vo);
    }

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public ResponseResult getMenuList(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.successResult(menuVos);
    }

    /**
     * 新增菜单
     */
    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.successResult();
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public ResponseResult getInfo(@PathVariable Long menuId) {
        return ResponseResult.successResult(menuService.getById(menuId));
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuIds}")
    public ResponseResult remove(@PathVariable List<Long> menuIds) {
        for (Long menuId : menuIds) {
            if (menuService.hasChild(menuId)) {
                return ResponseResult.errorResult(500, "存在子菜单不允许删除");
            }
            menuService.removeById(menuId);
        }

        return ResponseResult.successResult();
    }

    /**
     * 编辑菜单
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.successResult();
    }

}
