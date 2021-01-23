package com.youfan.jwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.youfan.jwt.annotation.JwtIgnore;
import com.youfan.jwt.common.response.ResultVO;
import com.youfan.jwt.config.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class TokenController {

    @Resource
    private JwtConfig jwtConfig ;

    @JwtIgnore
    @PostMapping("login")
    public ResultVO login (@RequestParam("userName") String userName, @RequestParam("passWord") String passWord){
        // 1 验证userName，passWord和数据库中是否一致，如不一致，直接return ResultTool.errer();
        // 2 创建token
        String userId = "UserId";
        String username = "Username";
        String role = "role";
        String token = jwtConfig.createToken(username, userId, role);
        // 3 将结果返回给前端
        JSONObject result = new JSONObject();
        result.put("token", token);
        return ResultVO.SUCCESS(result);
    }

    /**
     * 需要 Token 验证的接口
     */
    @PostMapping("info")
    public ResultVO info (){
        return ResultVO.SUCCESS("info") ;
    }

    /**
     * 根据请求头的token获取userId
     */
    @GetMapping("getUserInfo")
    public ResultVO getUserInfo(HttpServletRequest request){

        final String authHeader = request.getHeader(jwtConfig.getTokenHeaderKey());
        final String token = authHeader.substring(7);

        String name = jwtConfig.getUsername(token);
        String id = jwtConfig.getUserId(token);
        String role = jwtConfig.getUserRole(token);

        JSONObject result = new JSONObject();
        result.put("name", name);
        result.put("id", id);
        result.put("role", role);
        return ResultVO.SUCCESS(result);
    }
}
