package com.itheima.security.distributed.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // 安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/order/v1").hasAuthority("p2")
                //.antMatchers("/order/v2").hasAuthority("p2")
                .antMatchers("/order/**").authenticated()     // 所有/order/** 的请求必须认证通过
                .anyRequest().permitAll()                                  // 除了/order/** 其它的请求可以访问
        ;
    }
}
