package io.github.seujorgenochurras.p2pApi.domain.exception.handler;

import io.github.seujorgenochurras.p2pApi.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(genResponse(ex, httpStatus));
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(ClientNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(genResponse(ex, status));
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(InvalidEmailException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(genResponse(ex, status));
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<?> handleEmailExistsException(EmailExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleEmailExistsException(InvalidTokenException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    private GenericErrorResponse genResponse(RuntimeException ex, HttpStatus status) {
        return new GenericErrorResponse(ex.getMessage(), LocalDateTime.now(), status.value());
    }
}
