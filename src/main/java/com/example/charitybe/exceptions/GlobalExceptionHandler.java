package com.example.charitybe.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Tạo một Map để chứa tất cả các lỗi vi phạm
        Map<String, String> errors = new HashMap<>();
        
        // Lặp qua tất cả các lỗi trong ngoại lệ và thêm vào Map
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        // Trả về HTTP 400 Bad Request cùng với chi tiết lỗi
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        
        Map<String, String> customError = new HashMap<>();

        customError.put("email", "Email đã tồn tại"); 
        
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    
}
