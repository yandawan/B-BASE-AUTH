package com.youfan.jwt.config;

import com.youfan.jwt.annotation.JwtIgnore;
import com.youfan.jwt.common.exception.CustomException;
import com.youfan.jwt.common.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter{

    @Resource
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 忽略带JwtIgnore注解的请求,不做后续token认证校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
            if (jwtIgnore != null) {
                return true;
            }
        }

        // 跨域请求放行
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取token
        final String authHeader = request.getHeader(jwtConfig.getTokenHeaderKey());
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
            throw new CustomException(ResultCode.TOKEN_HEADER_IS_ERROR);
        }

        // 解析Token
        final String token = authHeader.substring(7);
        jwtConfig.getClaim(token);
        return true;
    }
}
