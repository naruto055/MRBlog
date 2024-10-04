package com.edu.zua.service;

import com.edu.zua.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.vo.MenuVO;

import java.util.List;

/**
* @author wenqunsheng
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2024-04-11 16:12:15
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermissionByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<MenuVO> getMenu(String status, String menuName);

    void addMenu(Menu menu);

    MenuVO getMenuById(Long id);

    void editMenu(Menu menu);

    List<Menu> selectMenuTree(Menu menu);

    List<Long> selectMenuListByRoleId(Long id);
}
