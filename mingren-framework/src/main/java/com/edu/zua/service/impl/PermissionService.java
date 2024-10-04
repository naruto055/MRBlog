package com.edu.zua.service.impl;

import com.edu.zua.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission) {
        // 如果是超级管理员，直接返回数据
        if (SecurityUtil.isAdmin()) {
            return true;
        }
        // 否则获取当前登录用户所具有的的权限列表，然后判断是否存在permission
        List<String > permissions = SecurityUtil.getLoginUser().getPermission();
        return permissions.contains(permission);
    }
}
