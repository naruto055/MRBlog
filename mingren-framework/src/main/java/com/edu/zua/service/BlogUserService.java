package com.edu.zua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.AddUserDto;
import com.edu.zua.domain.dto.UserDto;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.vo.EditUserDetailVO;
import com.edu.zua.domain.vo.PageVO;

import java.util.List;

public interface BlogUserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    PageVO listAllUser(UserDto userDto);

    void addUser(AddUserDto addUserDto);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(AddUserDto user);

    boolean checkEmailUnique(AddUserDto user);

    EditUserDetailVO getUserInfo(Long id);

    void editUserDetail(AddUserDto editUserDto);
}
