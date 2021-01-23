package com.shop.jwt.common.response;


public enum ResultCode {

                 SUCCESS(200,"操作成功"),
                    FAIL(500,"操作失败"),
        PARAM_IS_INVALID(5001, "参数无效"),
      SYSTEM_INNER_ERROR(5002, "系统繁忙,请稍后重试"),

     TOKEN_HEADER_IS_ERROR(10001, "token请求头部错误"),
             TOKEN_EXPIRED(10002, "token已过期"),
             TOKEN_INVALID(10003, "无效token"),
     TOKEN_SIGNATURE_ERROR(10004, "签名失败");

    int code;
    String message;
    private ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
