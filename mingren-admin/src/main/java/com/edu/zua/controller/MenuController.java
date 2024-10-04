package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.Menu;
import com.edu.zua.domain.vo.MenuTreeVO;
import com.edu.zua.domain.vo.MenuVO;
import com.edu.zua.domain.vo.RoleMenuTreeSelectVo;
import com.edu.zua.service.MenuService;
import com.edu.zua.utils.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 根据条件查询菜单列表
     * @param status
     * @param menuName
     * @return
     */
    @SystemLog(businessName = "查询菜单")
    @GetMapping("/list")
    public ResponseResult listAllMenu(String  status, String menuName) {
        List<MenuVO> menuVOS = menuService.getMenu(status, menuName);
        return ResponseResult.okResult(menuVOS);
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @SystemLog(businessName = "新增菜单")
    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return ResponseResult.okResult();
    }

    /**
     * 根据id获取菜单详情
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询菜单详情")
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id) {
        MenuVO menu = menuService.getMenuById(id);
        return ResponseResult.okResult(menu);
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @SystemLog(businessName = "修改菜单")
    @PutMapping
    public ResponseResult editMenu(@RequestBody Menu menu) {
        menuService.editMenu(menu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     */
    @SystemLog(businessName = "删除菜单")
    @DeleteMapping("/{id}")
    public ResponseResult delMenu(@PathVariable Long id) {
        menuService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 获取菜单下拉树列表
     * @return
     */
    @SystemLog(businessName = "获取下拉菜单树形列表")
    @GetMapping("/treeselect")
    public ResponseResult treeSelect() {
        List<Menu> menus = menuService.selectMenuTree(new Menu());
        List<MenuTreeVO> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    /**
     * 加载对应角色菜单列表树
     * @param id
     * @return
     */
    @SystemLog(businessName = "加载对应角色菜单列表树")
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult loadRoleMenuTree(@PathVariable Long id) {
        List<Menu> menus = menuService.selectMenuTree(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(id);
        List<MenuTreeVO> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }
}
