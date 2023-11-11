package com.vukimphuc.handler;

import com.phucvukimcore.base.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException() {
        return new Result(403,"Access Denied");
    }
}
