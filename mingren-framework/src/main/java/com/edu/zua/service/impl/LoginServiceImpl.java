package com.edu.zua.service.impl;

import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.LoginUser;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.vo.BlogLoginVO;
import com.edu.zua.domain.vo.UserInfoVO;
import com.edu.zua.service.BlogLoginService;
import com.edu.zua.service.LoginService;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.JwtUtil;
import com.edu.zua.utils.RedisCache;
import com.edu.zua.utils.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 后台管理系统的登录实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取userid生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);

        // 把用户信息存入redis
        redisCache.setCacheObject(Constants.REDIS_BACKEND_LOGIN_KEY + userId, loginUser);

        // 把token封装，返回
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        // 获取当前登录的用户信息
        Long loginUserId = SecurityUtil.getUserId();
        redisCache.deleteObject(Constants.REDIS_BACKEND_LOGIN_KEY + loginUserId);
        return ResponseResult.okResult();
    }
}
