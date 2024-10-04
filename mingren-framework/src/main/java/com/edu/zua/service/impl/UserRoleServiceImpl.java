package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.domain.entity.UserRole;
import com.edu.zua.service.UserRoleService;
import com.edu.zua.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author wenqunsheng
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2024-04-19 22:57:47
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

    @Override
    public List<UserRole> getUserRoles(Long id) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> list = list(queryWrapper);
        return list;
    }
}




