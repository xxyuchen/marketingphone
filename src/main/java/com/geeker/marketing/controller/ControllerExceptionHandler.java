package com.geeker.marketing.controller;

import com.geeker.marketing.response.Response;
import com.geeker.marketing.response.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author caoquanlong
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response exceptionHandler(Throwable throwable) {
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mane = (MethodArgumentNotValidException) throwable;
            BindingResult bindingResult = mane.getBindingResult();
            return ResponseUtils.error(404, bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else if (throwable instanceof BindException) {
            BindException bindException = (BindException) throwable;
            BindingResult bindingResult = bindException.getBindingResult();
            return ResponseUtils.error(500, bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            log.error("未知错误", throwable);
            String errorMsg = null == throwable.getMessage() || throwable.getMessage().length() > 100 ? "系统故障！" : throwable.getMessage();
            return ResponseUtils.error(500, errorMsg);
        }
    }
}