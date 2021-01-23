package com.itheima.security.springmvc.controller;

import com.itheima.security.springmvc.domain.UserLoginDto;
import com.itheima.security.springmvc.domain.UserDto;
import com.itheima.security.springmvc.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login",produces = "text/plain;charset=utf-8")
    public String login(UserLoginDto userLoginDto, HttpSession session){
        UserDto userDto = userService.login(userLoginDto);
        //存入session
        session.setAttribute(UserDto.SESSION_USER_KEY,userDto);
        return userDto.getUsername() +"登录成功";
    }

    @GetMapping(value = "/logout",produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession session){
        session.invalidate();
        return "退出成功";
    }

    @GetMapping(value = "/r/r1",produces = {"text/plain;charset=UTF-8"})
    public String r1(HttpSession session){
        String fullName = null;
        Object object = session.getAttribute(UserDto.SESSION_USER_KEY);
        if(object == null){
            fullName = "匿名";
        }else{
            UserDto userDto = (UserDto) object;
            fullName = userDto.getFullName();
        }
        return fullName+"访问资源r1";
    }
    @GetMapping(value = "/r/r2",produces = {"text/plain;charset=UTF-8"})
    public String r2(HttpSession session){
        String fullName = null;
        Object userObj = session.getAttribute(UserDto.SESSION_USER_KEY);
        if(userObj != null){
            fullName = ((UserDto)userObj).getFullName();
        }else{
            fullName = "匿名";
        }
        return fullName + " 访问资源2";
    }
}
