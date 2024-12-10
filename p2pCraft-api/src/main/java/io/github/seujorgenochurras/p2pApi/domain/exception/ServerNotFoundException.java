package io.github.seujorgenochurras.p2pApi.domain.exception;

public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException() {
    }

    public ServerNotFoundException(String message) {
        super(message);
    }

    public static ServerNotFoundException defaultMessage(String serverName) {
        return new ServerNotFoundException("No server with name: '" + serverName + "' found");
    }

    public ServerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServerNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
