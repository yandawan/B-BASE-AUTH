package com.itheima.security.distributed.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    // 对称秘钥，资源服务器使用该秘钥来验证
    private String SIGNING_KEY = "self_jwt_token_key";

    // JWT令牌存储方案
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

    // 使用内存存储令牌（普通令牌）
    // @Bean
    // public TokenStore tokenStore() {
    //    return new InMemoryTokenStore();
    // }
}
