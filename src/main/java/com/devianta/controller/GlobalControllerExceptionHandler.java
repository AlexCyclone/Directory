package com.devianta.controller;

import com.devianta.exception.ObjectNotFoundException;
import com.devianta.model.ExceptionInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseBody
    public ExceptionInfo handleObjectNotFound(HttpServletRequest request, Exception ex) {
        return ExceptionInfo.getNew(request, NOT_FOUND, ex);
    }
}