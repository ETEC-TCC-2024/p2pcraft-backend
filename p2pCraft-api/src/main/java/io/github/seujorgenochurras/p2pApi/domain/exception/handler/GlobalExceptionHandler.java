package io.github.seujorgenochurras.p2pApi.domain.exception.handler;

import io.github.seujorgenochurras.p2pApi.domain.exception.*;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(httpStatus)
            .body(genResponse(ex));
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(ClientNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(InvalidEmailException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<?> handleEmailExistsException(EmailExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleEmailExistsException(InvalidTokenException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    @ExceptionHandler(InvalidIpAddressException.class)
    public ResponseEntity<?> handleIpAddressException(InvalidIpAddressException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    @ExceptionHandler(ServerNotFoundException.class)
    public ResponseEntity<?> handleServerNotFoundException(ServerNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(ServerNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
            .body(genResponse(ex));
    }

    private GenericErrorResponse genResponse(RuntimeException ex) {
        return new GenericErrorResponse(ex.getMessage(), LocalDateTime.now());
    }
}
