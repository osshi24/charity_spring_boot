package com.example.charitybe.exceptions; 

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Tự động trả về HTTP 400 khi ngoại lệ này được ném ra
@ResponseStatus(HttpStatus.BAD_REQUEST) 
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}