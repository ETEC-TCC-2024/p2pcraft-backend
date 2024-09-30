package io.github.seujorgenochurras.p2pApi.domain.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailException(Throwable cause) {
        super(cause);
    }

    public InvalidEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
