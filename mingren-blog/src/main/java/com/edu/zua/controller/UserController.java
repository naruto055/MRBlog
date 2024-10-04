package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.User;
import com.edu.zua.service.BlogUserService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户", description = "前台用户相关接口")
public class UserController {

    @Resource
    private BlogUserService userService;

    /**
     * 获取用户信息
     * @return
     */
    @SystemLog(businessName = "查询用户信息")
    @ApiOperation("获取用户信息")
    @GetMapping("/userInfo")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @SystemLog(businessName = "更新用户信息")
    @ApiOperation("修改用户信息")
    @ApiImplicitParam(value = "修改用户信息上传的dto", name = "user")
    @ApiParam("user")
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @SystemLog(businessName = "用户注册")
    @ApiOperation("用户注册")
    @ApiParam(name = "注册用户上传的dto", value = "user")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return userService.register(user);
    }
}
