package com.itheima.security.distributed.server.model;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String mobile;
}
