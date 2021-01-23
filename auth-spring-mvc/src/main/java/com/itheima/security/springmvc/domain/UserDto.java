package com.itheima.security.springmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {
    public static final String SESSION_USER_KEY = "_user";
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String mobile;
    private Set<String> authorities;
}
