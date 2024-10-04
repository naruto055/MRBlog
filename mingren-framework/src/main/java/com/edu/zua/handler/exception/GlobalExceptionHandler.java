package com.edu.zua.handler.exception;

import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult exceptionHandler(SystemException systemException) {
        // 打印异常信息
        log.error("出现了异常！ { }", systemException);
        // 从异常信息中获取提示信息
        return ResponseResult.errorResult(systemException.getCode(), systemException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult otherExceptionHandler(Exception e) {
        // 打印异常信息
        log.error("出现了异常 { }", e);
        // 从异常信息中获取提示信息
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
