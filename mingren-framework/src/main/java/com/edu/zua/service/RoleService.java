package com.edu.zua.service;

import com.edu.zua.domain.dto.RoleDto;
import com.edu.zua.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.vo.PageVO;

import java.util.List;

/**
* @author wenqunsheng
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2024-04-11 16:17:50
*/
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    PageVO listAllRolesByPage(RoleDto roleDto);

    void changeStatus(RoleDto roleDto);

    void addRole(Role role);

    void editRole(Role role);
}
