package com.desouza.dscommerce.controllers.handlers;

import java.time.Instant;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.desouza.dscommerce.dto.error.CustomError;
import com.desouza.dscommerce.dto.error.ValidationError;
import com.desouza.dscommerce.services.exceptions.DataBaseException;
import com.desouza.dscommerce.services.exceptions.EmailException;
import com.desouza.dscommerce.services.exceptions.ForbiddenException;
import com.desouza.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<CustomError> dataBase(DataBaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(e.getMessage(), status.value(), request.getRequestURI(), Instant.now());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomError err = new CustomError(e.getMessage(), status.value(), request.getRequestURI(), Instant.now());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError("Invalid data", status.value(), request.getRequestURI(),
                Instant.now());

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomError> constraintViolation(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError("Invalid data", status.value(), request.getRequestURI(),
                Instant.now());

        e.getConstraintViolations().forEach(x -> {
            String fieldName = x.getPropertyPath().toString();
            fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            String message = x.getMessage();
            err.addError(fieldName, message);
        });
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<CustomError> propertyReference(PropertyReferenceException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(e.getMessage(), status.value(), request.getRequestURI(), Instant.now());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(e.getMessage(), status.value(), request.getRequestURI(), Instant.now());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<CustomError> email(EmailException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(e.getMessage(), status.value(), request.getRequestURI(), Instant.now());
        return ResponseEntity.status(status).body(err);
    }

}
