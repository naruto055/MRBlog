package com.edu.zua.service;

import com.edu.zua.domain.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wenqunsheng
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service
* @createDate 2024-04-17 15:24:57
*/
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}
