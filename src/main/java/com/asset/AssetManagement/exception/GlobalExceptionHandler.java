package com.asset.AssetManagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path, String errorCode) {
        return new ErrorResponse(LocalDateTime.now(), errorCode, message, path);
    }

    //Duplicate Asset
    @ExceptionHandler(DuplicateAssetException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateAssetException ex, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), "DUPLICATE_ASSET"),
                HttpStatus.CONFLICT
        );
    }

    //invalid DatesI
    @ExceptionHandler(InvalidAssetDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDates(
            InvalidAssetDateException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), "INVALID_DATE"),
                HttpStatus.BAD_REQUEST
        );
    }

    // Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), "RESOURS_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), "VALIDATION_ERROR"),
                HttpStatus.BAD_REQUEST
        );
    }
}
