package com.devsuperior.bds04.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandle {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> Validation(MethodArgumentNotValidException e ,HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError();
        err.setStatus(status.value());
        err.setPath(request.getRequestURI());
        err.setTimestamp(Instant.now());
        err.setError("Validation Exception");
        err.setMessage(e.getMessage());

        for(FieldError f:e.getBindingResult().getFieldErrors()){
            err.addErrors(f.getField(),f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

}
