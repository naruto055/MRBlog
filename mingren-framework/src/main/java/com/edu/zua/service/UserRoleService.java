package com.edu.zua.service;

import com.edu.zua.domain.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wenqunsheng
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service
* @createDate 2024-04-19 22:57:47
*/
public interface UserRoleService extends IService<UserRole> {

    List<UserRole> getUserRoles(Long id);
}
