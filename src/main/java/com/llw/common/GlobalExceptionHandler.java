package com.llw.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String msg = ex.getMessage().split(" ")[2] + "已存在";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    private Result<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());

        return Result.error(ex.getMessage());
    }
}
