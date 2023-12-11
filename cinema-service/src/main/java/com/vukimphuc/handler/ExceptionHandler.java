package com.vukimphuc.handler;

import com.phucvukimcore.base.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException() {
        return new Result(403,"Access Denied");
    }
}
