# OAuth 2.0

## 授权码模式
```
# 1 获取授权码
http://localhost:3100/sso/oauth/authorize?client_id=app&response_type=code&scope=app&redirect_uri=https://www.baidu.com
结果: https://www.baidu.com/?code=e3dAkh

# 2 申请令牌
授权码模式申请token.png
```

## 简化模式
```
# 1 请求token
http://localhost:3100/sso/oauth/authorize?client_id=app&response_type=token&scope=app&redirect_uri=https://www.baidu.com
结果: https://www.baidu.com/#access_token=
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJleHAiOjE1OTA5MTg1MTEsInVzZXJfbmFtZSI6ImFkbWluQHNpbmEuY29tIiwiYXV0aG9yaXRpZXMiOlsiMSJdLCJqdGkiOiI5Zjg1ZjBkNi01NDQ3LTQ1MTMtYTJhZC0zOWUwYjJiNzU0ZDUiLCJjbGllbnRfaWQiOiJhcHAiLCJzY29wZSI6WyJhcHAiXX0.
sFcWM9nB2-yBvMv3lB2xSdDGPwrCkCYZefA_szvL2CY&token_type=bearer&expires_in=2591999&jti=9f85f0d6-5447-4513-a2ad-39e0b2b754d5
```

## 密码模式
```
密码模式申请token.png
```

## 客户端模式获取授权码
```
客户端模式获取token.png
```

## 扩展User 增加额外的信息
```
// 将userDto转成json
String principal = JSON.toJSONString(userDto);
UserDetails userDetails = User.withUsername(principal)
```
