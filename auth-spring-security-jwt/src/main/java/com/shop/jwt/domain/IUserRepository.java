package com.shop.jwt.domain;

import org.springframework.stereotype.Component;

@Component
public interface IUserRepository{
    User findByUsername(String username);

    void insert(User user);
}