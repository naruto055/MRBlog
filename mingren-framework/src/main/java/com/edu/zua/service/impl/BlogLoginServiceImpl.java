package com.edu.zua.service.impl;

import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.LoginUser;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.vo.BlogLoginVO;
import com.edu.zua.domain.vo.UserInfoVO;
import com.edu.zua.service.BlogLoginService;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.JwtUtil;
import com.edu.zua.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    /**
     * 用户登录
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
        redisCache.setCacheObject("bloglogin:"+userId, loginUser);

        // 把user转换成userInfoVO
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);

        // 将token和userInfoVO封装起来返回
        BlogLoginVO vo = new BlogLoginVO(token, userInfoVO);
        return ResponseResult.okResult(vo);
    }

    /**
     * 退出登录
     * @return
     */
    @Override
    public ResponseResult logout() {
        // 获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        // 删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
