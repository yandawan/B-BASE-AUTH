package com.itheima.security.distributed.order.controller;

import com.itheima.security.distributed.order.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    // 拥有 getOrderById 权限方可访问此url
    @PreAuthorize("hasAuthority('getOrderById')")
    @GetMapping(value = "/getOrderById")
    public String getOrderById(){
        // 获取用户身份信息
        UserDTO  userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDTO.getFullName()+"访问资源getOrderById";
    }
}