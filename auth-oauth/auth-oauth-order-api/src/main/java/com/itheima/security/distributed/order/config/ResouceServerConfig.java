package com.itheima.security.distributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    // 资源id
    public static final String RESOURCE_ORDER_ID = "order-api";
    // scope
    public static final String SCOPE = "app";

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ORDER_ID)
                .tokenStore(tokenStore)
                 //.tokenServices(tokenService()) // 验证令牌的服务
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").access("#oauth2.hasScope('app')")
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // 资源服务令牌解析服务:使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
    // @Bean
    // public ResourceServerTokenServices tokenService() {
    //  RemoteTokenServices service=new RemoteTokenServices();
    //  service.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
    //  service.setClientId("client_self_id");
    //  service.setClientSecret("client_self_secret");
    //  return service;
    // }

}
