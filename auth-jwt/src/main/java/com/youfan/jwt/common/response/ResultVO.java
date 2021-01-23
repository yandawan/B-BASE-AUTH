package com.youfan.jwt.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResultVO<T> {
    int code;
    String message;
    T data;

    public ResultVO(ResultCode resultCode){
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public ResultVO(ResultCode resultCode, T data){
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    public ResultVO(String message){
        this.message = message;
    }

    public static ResultVO SUCCESS(){
        return new ResultVO(ResultCode.SUCCESS);
    }

    public static <T> ResultVO SUCCESS(T data){
        return new ResultVO(ResultCode.SUCCESS, data);
    }

    public static ResultVO FAIL(){
        return new ResultVO(ResultCode.FAIL);
    }

    public static ResultVO FAIL(String message){
        return new ResultVO(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
