package com.itheima.security.springmvc.domain;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    public UserDto login(UserLoginDto authenticationRequest) {
        // 校验参数是否为空
        if(authenticationRequest == null
            || StringUtils.isEmpty(authenticationRequest.getUsername())
            || StringUtils.isEmpty(authenticationRequest.getPassword())){
            throw new RuntimeException("账号和密码为空");
        }
        // 根据账号去查询数据库,这里测试程序采用模拟方法
        UserDto user = getUserDto(authenticationRequest.getUsername());
        // 判断用户是否为空
        if(user == null){
            throw new RuntimeException("查询不到该用户");
        }
        // 校验密码
        if(!authenticationRequest.getPassword().equals(user.getPassword())){
            throw new RuntimeException("账号或密码错误");
        }
        // 认证通过，返回用户身份信息
        return user;
    }

    // 根据账号查询用户信息
    private UserDto getUserDto(String userName){
        return userDtoMap.get(userName);
    }

    // 用户信息
    private final Map<String,UserDto> userDtoMap = new HashMap<>();
    {
        Set<String> authorities1 = new HashSet<>();
        authorities1.add("p1");//这个p1我们人为让它和/r/r1对应
        Set<String> authorities2 = new HashSet<>();
        authorities2.add("p2");//这个p2我们人为让它和/r/r2对应
        userDtoMap.put("zhangsan",new UserDto("1010","zhangsan","123","张三","133443",authorities1));
        userDtoMap.put("lisi",new UserDto("1011","lisi","456","李四","144553",authorities2));
    }
}
