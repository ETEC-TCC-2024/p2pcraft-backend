package io.github.seujorgenochurras.p2pApi.domain.exception;

import java.time.LocalDateTime;

public class GenericErrorResponse {

    private String description;
    private LocalDateTime issuedAt;


    public GenericErrorResponse(String description, LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
        this.description = description;
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
