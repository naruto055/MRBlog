package com.edu.zua.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.AddUserDto;
import com.edu.zua.domain.dto.EditStatusDto;
import com.edu.zua.domain.dto.UserDto;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.EditUserDetailVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.exception.SystemException;
import com.edu.zua.service.BlogUserService;
import com.edu.zua.utils.BeanCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private BlogUserService userService;

    /**
     * 获取所有用户的信息
     * @param userDto
     * @return
     */
    @SystemLog(businessName = "查询所有的用户")
    @GetMapping("/list")
    public ResponseResult getAllUser(UserDto userDto) {
        PageVO pageVO = userService.listAllUser(userDto);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 添加用户
     * @param addUserDto
     * @return
     */
    @SystemLog(businessName = "新增用户")
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto) {
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(addUserDto)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(addUserDto)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        userService.addUser(addUserDto);
        return ResponseResult.okResult();
    }

    /**
     * 逻辑删除用户
     * @param ids
     * @return
     */
    @SystemLog(businessName = "删除用户")
    @DeleteMapping("/{id}")
    public ResponseResult delUser(@PathVariable(value = "id") String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        List<Long> idList = list.stream().map(id -> Long.parseLong(id))
                .collect(Collectors.toList());
        userService.removeBatchByIds(idList);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询用户信息回显
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询用户详情")
    @GetMapping("/{id}")
    public ResponseResult getUserDetail(@PathVariable Long id) {
        EditUserDetailVO editUserDetailVO = userService.getUserInfo(id);
        return ResponseResult.okResult(editUserDetailVO);
    }

    /**
     * 编辑用户
     * @param editUserDto
     * @return
     */
    @SystemLog(businessName = "编辑用户")
    @PutMapping
    public ResponseResult editUserDetail(@RequestBody AddUserDto editUserDto) {
        userService.editUserDetail(editUserDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改用户的状态
     * @param editStatusDto
     * @return
     */
    @SystemLog(businessName = "修改用户状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody EditStatusDto editStatusDto) {
        User user = BeanCopyUtils.copyBean(editStatusDto, User.class);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, editStatusDto.getUserId());
        userService.update(user, queryWrapper);
        return ResponseResult.okResult();
    }
}
