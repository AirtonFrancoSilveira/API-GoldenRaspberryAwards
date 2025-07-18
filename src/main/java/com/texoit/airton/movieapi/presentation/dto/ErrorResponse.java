package com.texoit.airton.movieapi.presentation.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO para respostas de erro padronizadas.
 * Segue as práticas de senior engineer para tratamento de erros.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String message;
    private Map<String, String> details;
    private Instant timestamp;
    private String path;

    // Construtor padrão
    public ErrorResponse() {
    }

    // Construtor com builder
    private ErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.details = builder.details;
        this.timestamp = builder.timestamp;
        this.path = builder.path;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private String message;
        private Map<String, String> details;
        private Instant timestamp;
        private String path;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(Map<String, String> details) {
            this.details = details;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}