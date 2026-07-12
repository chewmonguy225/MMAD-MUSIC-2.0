package com.MMAD.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.MMAD.dto.MessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<MessageResponse> handleInvalidCode(
            InvalidCodeException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new MessageResponse(
                                false,
                                ex.getMessage()));

    }
}