package io.github.seujorgenochurras.p2pApi.domain.exception;

import java.time.LocalDateTime;

public class GenericErrorResponse {
    private int statusCode;
    private String description;
    private LocalDateTime issuedAt;

    public GenericErrorResponse(String description, LocalDateTime issuedAt, int statusCode) {
        this.statusCode = statusCode;
        this.issuedAt = issuedAt;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public GenericErrorResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public GenericErrorResponse setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GenericErrorResponse setDescription(String description) {
        this.description = description;
        return this;
    }
}