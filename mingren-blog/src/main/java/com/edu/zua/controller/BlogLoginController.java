package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.exception.SystemException;
import com.edu.zua.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "登录", description = "登录相关接口")
public class BlogLoginController {

    @Resource
    private BlogLoginService blogLoginService;

    /**
     * 登录
     * @param user
     * @return
     */
    @SystemLog(businessName = "前台用户登录")
    @ApiOperation(value = "登录", notes = "前台登录")
    @ApiParam(value = "user")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {

        if (!StringUtils.hasText(user.getUserName())) {
            // 提示要传递用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return blogLoginService.login(user);
    }

    /**
     * 退出登录
     * @return
     */
    @SystemLog(businessName = "前台用户退出登录")
    @ApiOperation(value = "退出登录", notes = "前台退出登录")
    @PostMapping("/logout")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
