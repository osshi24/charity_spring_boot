package com.example.charitybe.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.charitybe.dto.ApiResponseDTO;


@ControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleUserNotFound(UsernameNotFoundException e) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(404, null, e.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleBadCredentials(BadCredentialsException e) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(401, null, e.getMessage());
        return ResponseEntity.status(401).body(response);
    }

    
}
