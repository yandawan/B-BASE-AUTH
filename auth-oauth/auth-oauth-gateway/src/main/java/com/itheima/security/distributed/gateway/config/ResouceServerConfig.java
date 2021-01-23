package com.itheima.security.distributed.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class ResouceServerConfig  {

    // 资源id
    public static final String RESOURCE_SSO_ID = "auth-sso-api";
    public static final String RESOURCE_ORDER_ID = "order-api";

    // auth-oauth-api 资源服务配置
    @Configuration
    @EnableResourceServer
    public class AuthOauthServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources){
            resources.tokenStore(tokenStore).resourceId(RESOURCE_SSO_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/**").permitAll();
        }
    }

    // order-api 资源
    @Configuration
    @EnableResourceServer
    public class OrderServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources){
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ORDER_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/order/**").access("#oauth2.hasScope('app')");
        }
    }
}
