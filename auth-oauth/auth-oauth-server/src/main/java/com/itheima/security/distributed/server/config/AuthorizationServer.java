package com.itheima.security.distributed.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权服务配置:
 *
 * 1 谁来申请令牌 哪些客户端来申请 - ClientDetailsServiceConfigurer
 * 2 令牌怎么发放 对应的服务和URL - AuthorizationServerEndpointsConfigurer
 * 3 令牌发放的安全约束 谁可以来获取令牌 - AuthorizationServerSecurityConfigurer
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 客户端详情服务
     * resourceIds: 相当于-可以访问哪些api服务
     * scopes: 相当于-大分类是手机端还是pc端的client
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
        // 使用in-memory存储
        // clients.inMemory()
        //        .withClient("client_self_id")                                                                                 // client_id
        //        .secret(new BCryptPasswordEncoder().encode("client_self_secret"))                                             // 客户端密钥
        //        .resourceIds("request_resource_id")                                                                           // 资源列表
        //        .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")       // 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
        //        .scopes("all")                                                                                                // 允许的授权范围
        //        .autoApprove(false)                                                                                           // false跳转到授权页面
        //        .redirectUris("http://www.baidu.com");                                                                        // 加上验证回调地址-微信使用此方法
    }

    /**
     * 令牌访问端点
     * 1 /oauth/authorize:      授权端点
     * 2 /oauth/token:          令牌端点
     * 3 /oauth/confirm_access: 用户确认授权提交端点
     * 4 /oauth/error:          授权服务错误信息端点
     * 5 /oauth/check_token:    用于资源服务访问的令牌解析端点
     * 6 /oauth/token_key:      提供公有密钥的端点,如果使用JWT令牌的话
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)          // 认证管理器 - 密码授权类型
                .authorizationCodeServices(authorizationCodeServices)  // 授权码服务 - 授权码模式
                .tokenServices(tokenService())                         // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security.tokenKeyAccess("permitAll()")                    // oauth/token_key是公开
                .checkTokenAccess("permitAll()")                  // oauth/check_token公开
                .allowFormAuthenticationForClients()		      // 表单认证（申请令牌）
        ;
    }

    // 将客户端信息存储到数据库
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    // 令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); // 客户端详情服务
        service.setSupportRefreshToken(true);                  // 支持刷新令牌
        service.setTokenStore(tokenStore);                     // 令牌存储策略
        // 令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200);          // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200);       // 刷新令牌默认有效期3天
        return service;
    }

    // 设置授权码模式的授权码如何存取，暂时采用内存方式
    // @Bean
    // public AuthorizationCodeServices authorizationCodeServices() {
    //    return new InMemoryAuthorizationCodeServices();
    // }

    // 设置授权码模式的授权码如何存取
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }
}
