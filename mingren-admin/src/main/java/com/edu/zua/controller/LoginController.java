package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.LoginUser;
import com.edu.zua.domain.entity.Menu;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.AdminUserInfoVO;
import com.edu.zua.domain.vo.RoutersVO;
import com.edu.zua.domain.vo.UserInfoVO;
import com.edu.zua.exception.SystemException;
import com.edu.zua.service.LoginService;
import com.edu.zua.service.MenuService;
import com.edu.zua.service.RoleService;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.RedisCache;
import com.edu.zua.utils.SecurityUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    @SystemLog(businessName = "登录")
    public ResponseResult login(@RequestBody User user) {

        if (!StringUtils.hasText(user.getUserName())) {
            // 提示要传递用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return loginService.login(user);
    }

    /**
     * 后台dashboard权限控制和路由跳转
     * @return
     */
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVO> getInfo() {
        // 1. 查询当前登录的用户
        LoginUser loginUser = SecurityUtil.getLoginUser();

        // 2. 根据用户id查询权限信息
        List<String> permission = menuService.selectPermissionByUserId(loginUser.getUser().getId());

        // 3. 根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        // 获取用户信息
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);

        // 4. 封装数据返回
        AdminUserInfoVO adminUserInfoVO = new AdminUserInfoVO(permission, roleKeyList, userInfoVO);
        return ResponseResult.okResult(adminUserInfoVO);
    }

    /**
     * 获取动态路由的信息
     * @return
     */
    @GetMapping("getRouters")
    public ResponseResult<RoutersVO> getRouters() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        Long userId = loginUser.getUser().getId();
        // 查询menu
        List<Menu> menuList = menuService.selectRouterMenuTreeByUserId(userId);

        // 封装数据返回
        return ResponseResult.okResult(new RoutersVO(menuList));
    }

    /**
     * 退出登录
     * @return
     */
    @SystemLog(businessName = "退出登录")
    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }
}
