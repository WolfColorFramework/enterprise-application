package com.mit.mission.common.handler;

import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.web.ResponseMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @autor: gaoy
 * @date: 2021/4/19 13:29
 * @description: 全局异常处理器
 */
@ControllerAdvice
@RestController
public class CustomExceptionHandler {

    //处理程序员主动转换的自定义异常
    @ExceptionHandler(CustomException.class)
    public ResponseMessage customerException(CustomException e) {
        return ResponseMessage.Error(e.getCode(), e.getMessage());
    }

    //处理程序员在程序中未能捕获（遗漏的）异常
    @ExceptionHandler(Exception.class)
    public ResponseMessage exception(Exception e) {
        return ResponseMessage.Error(new CustomException(CustomExceptionType.OTHER_ERROR, e.getMessage()));
    }
}
