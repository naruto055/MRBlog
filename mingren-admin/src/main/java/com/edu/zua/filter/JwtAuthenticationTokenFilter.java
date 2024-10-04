package com.edu.zua.filter;

import com.alibaba.fastjson.JSON;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.LoginUser;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.utils.JwtUtil;
import com.edu.zua.utils.RedisCache;
import com.edu.zua.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的token
        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 说明该接口需要登录，直接放行
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // 解析获取userId
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // token 超时    token 非法
            // 响应告诉前端
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return ;
        }
        String userId = claims.getSubject();

        // 从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);

        // 如果获取不到redis中的token
        if (Objects.isNull(loginUser)) {
            // token过期，提示用户重新登录 ，响应告诉前端
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return ;
        }

        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
