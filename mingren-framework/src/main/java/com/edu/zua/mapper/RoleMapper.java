package com.edu.zua.mapper;

import com.edu.zua.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author wenqunsheng
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2024-04-11 16:17:50
* @Entity com.edu.zua.domain.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}




