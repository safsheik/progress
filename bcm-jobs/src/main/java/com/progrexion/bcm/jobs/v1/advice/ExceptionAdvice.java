package com.progrexion.bcm.jobs.v1.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.models.v1.ErrorDto;



@RestControllerAdvice
public class ExceptionAdvice{

    @ExceptionHandler(BCMModuleException.class)
    public ResponseEntity<ErrorDto> handlePlatformException(BCMModuleException e){
        return ResponseEntity.status(e.getHttpStatusCode()).body(new ErrorDto(false,e.getHttpStatusCode(),e.getErrorCode(), e.getErrorMessage()));
    }
}
