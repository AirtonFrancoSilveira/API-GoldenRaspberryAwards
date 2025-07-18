package com.texoit.airton.movieapi.presentation.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.texoit.airton.movieapi.presentation.dto.ErrorResponse;
import com.texoit.airton.movieapi.shared.exception.InvalidIntervalException;

/**
 * Handler global de exceções seguindo práticas de senior engineer.
 * Centraliza tratamento de erros com logs estruturados e respostas
 * padronizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata exceções de validação de dados
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        logger.warn("Validation error on request: {}", request.getDescription(false));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .details(errors)
                .timestamp(Instant.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata exceções de constraint validation
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        logger.warn("Constraint violation: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("CONSTRAINT_VIOLATION")
                .message("Constraint validation failed")
                .details(errors)
                .timestamp(Instant.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata exceções de domínio - intervalo inválido
     */
    @ExceptionHandler(InvalidIntervalException.class)
    public ResponseEntity<ErrorResponse> handleInvalidIntervalException(
            InvalidIntervalException ex, WebRequest request) {

        logger.warn("Invalid interval: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INVALID_INTERVAL")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata exceções de argumentos ilegais
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        logger.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("ILLEGAL_ARGUMENT")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata exceções gerais não capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        logger.error("Unexpected error occurred", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .timestamp(Instant.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Trata exceções de runtime
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        logger.error("Runtime error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("RUNTIME_ERROR")
                .message("A runtime error occurred")
                .timestamp(Instant.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}