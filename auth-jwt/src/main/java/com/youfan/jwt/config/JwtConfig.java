package com.youfan.jwt.config;

import com.youfan.jwt.common.exception.CustomException;
import com.youfan.jwt.common.response.ResultCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Date;

@ConfigurationProperties(prefix = "config.jwt")
@Component
public class JwtConfig {

    private String clientId;
    private String secret;
    private String name;
    private long   expire;
    private String tokenHeaderKey;
    private String tokenPrefix;

    /**
     * 构建jwt
     *
     * @param userId   用户id
     * @param username 用户名
     * @param role     角色
     * @return jwt     令牌
     */
    public String createToken(String username, String userId, String role) {
        try {
            Date nowDate = new Date();
            Date expireDate = new Date(nowDate.getTime() + expire * 1000);
            return Jwts.builder().setHeaderParam("typ", "JWT")
                    .claim("role", role)                     // 可以将基本不重要的对象信息放到claims
                    .claim("userId", userId)                 // 可以将基本不重要的对象信息放到claims
                    .setSubject(username)                       // 代表这个JWT的主体，即它的所有人
                    .setIssuer(clientId)                        // 代表这个JWT的签发主体
                    .setAudience(name)                          // 代表这个JWT的接收对象；
                    .setIssuedAt(nowDate)                       // 代表这个JWT的签发时间
                    .setNotBefore(nowDate)                      // 代表这个JWT生效的开始时间
                    .setExpiration(expireDate)                  // 设置过期时间
                    .signWith(SignatureAlgorithm.HS512, secret).compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.TOKEN_SIGNATURE_ERROR);
        }
    }

    /**
     * 解析jwt
     *
     * @param token 令牌
     * @return claims
     */
    public Claims getClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.TOKEN_INVALID);
        }
    }


    /**
     * 获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsername(String token) {
        return getClaim(token).getSubject();
    }

    /**
     * 获取用户id
     *
     * @param token 令牌
     * @return 用户id
     */
    public String getUserId(String token) {
        return getClaim(token).get("userId", String.class);
    }

    /**
     * 获取用户角色
     *
     * @param token 令牌
     * @return 用户id
     */
    public String getUserRole(String token) {
        return getClaim(token).get("role", String.class);
    }

    /**
     * 验证token是否过期失效
     *
     * @param token 令牌
     * @return true 过期 / false 没过期
     */
    public boolean isExpired(String token) {
        return getClaim(token).getExpiration().before(new Date());
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getTokenHeaderKey() {
        return tokenHeaderKey;
    }

    public void setTokenHeaderKey(String tokenHeaderKey) {
        this.tokenHeaderKey = tokenHeaderKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }
}
