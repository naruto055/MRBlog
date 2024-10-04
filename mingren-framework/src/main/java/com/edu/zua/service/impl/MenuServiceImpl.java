package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.entity.Menu;
import com.edu.zua.domain.vo.MenuVO;
import com.edu.zua.service.MenuService;
import com.edu.zua.mapper.MenuMapper;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenqunsheng
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2024-04-11 16:12:15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Override
    public List<String> selectPermissionByUserId(Long id) {
        // 如果是管理员，返回所有所有的权限
        if (SecurityUtil.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, Constants.MENU, Constants.BUTTON);
            queryWrapper.eq(Menu::getStatus, Constants.STATUS_NORMAL);
            // 查到管理员的菜单类型
            List<Menu> list = list(queryWrapper);
            // 把管理员的权限列表存入集合中
            List<String> permission = list.stream().map(Menu::getPerms)
                    .collect(Collectors.toList());
            return permission;
        }
        // 否则返回其所具有的的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        // 判断是否是管理员
        if (SecurityUtil.isAdmin()) {
            // 如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 否则 返回当前所具有的Menu
            menus = menuMapper.selectRouterMenuTressByUserId(userId);
        }
        // 构建tree
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public List<MenuVO> getMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        return BeanCopyUtils.copyBeanList(list, MenuVO.class);
    }

    @Override
    public void addMenu(Menu menu) {
        save(menu);
    }

    @Override
    public MenuVO getMenuById(Long id) {
        Menu menu = getById(id);
        return BeanCopyUtils.copyBean(menu, MenuVO.class);
    }

    @Override
    public void editMenu(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, menu.getId());
        update(menu, queryWrapper);
    }

    @Override
    public List<Menu> selectMenuTree(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long id) {
        return getBaseMapper().selectMenuListByRoleId(id);
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        // 先找出第一层的菜单，然后去找他们的子菜单，设置到到children属性中。
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的  子Menu集合
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}




