package com.shop.jwt.common.exception;

import com.shop.jwt.common.response.ResultCode;
import com.shop.jwt.common.response.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     */
	@ExceptionHandler(CustomException.class)
	public ResultVO handleException(CustomException e) {
        e.getMessage();
        return new ResultVO(e.getResultCode());
	}

    /**
     * 参数错误异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResultVO handleException(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            BindingResult result = validException.getBindingResult();
            StringBuffer errorMsg = new StringBuffer();
            if (result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                errors.forEach(p ->{
                    FieldError fieldError = (FieldError) p;
                    errorMsg.append(fieldError.getDefaultMessage()).append(",");
                    log.error("请求参数错误:【"+fieldError.getObjectName()+"】,field【"+fieldError.getField()+ "】,errorMessage【"+fieldError.getDefaultMessage()+"】"); });
            }
        } else if (e instanceof BindException) {
            BindException bindException = (BindException)e;
            if (bindException.hasErrors()) {
                log.error("请求参数错误:{}", bindException.getAllErrors());
            }
        }
        return new ResultVO(ResultCode.PARAM_IS_INVALID);
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    public ResultVO handleOtherException(Exception e){
        e.printStackTrace();
        return new ResultVO(ResultCode.SYSTEM_INNER_ERROR);
    }
}
