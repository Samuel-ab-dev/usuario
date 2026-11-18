package com.seleste.usuario.controller;

import com.seleste.usuario.infrastructure.exceptions.ConflictException;
import com.seleste.usuario.infrastructure.exceptions.DTO.ErrorResponseDTO;
import com.seleste.usuario.infrastructure.exceptions.IllegalArgumentException;
import com.seleste.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.seleste.usuario.infrastructure.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex,
                                                                   HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                req.getRequestURI(),
                "Not found"));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(ConflictException ex,
                                                           HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                req.getRequestURI(),
                "Conflict"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(UnauthorizedException ex,
                                                               HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildError(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                req.getRequestURI(),
                "Unauthorized access attempt"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegal(IllegalArgumentException ex,
                                                          HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                req.getRequestURI(),
                "Unauthorized access attempt"));
    }


    private ErrorResponseDTO buildError(int status, String message, String path,
                                        String error) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                status,
                error,
                message,
                path
        );
    }
}
