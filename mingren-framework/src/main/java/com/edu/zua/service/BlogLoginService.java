package com.edu.zua.service;

import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
