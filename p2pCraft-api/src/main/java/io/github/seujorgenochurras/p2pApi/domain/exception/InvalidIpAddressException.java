package io.github.seujorgenochurras.p2pApi.domain.exception;

public class InvalidIpAddressException extends RuntimeException {
    public InvalidIpAddressException() {
    }

    public InvalidIpAddressException(String message) {
        super(message);
    }

    public static InvalidIpAddressException defaultMessage(String serverIp) {
        return new InvalidIpAddressException("No server with ip: '" + serverIp + "' was found");
    }

    public InvalidIpAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIpAddressException(Throwable cause) {
        super(cause);
    }

    public InvalidIpAddressException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
