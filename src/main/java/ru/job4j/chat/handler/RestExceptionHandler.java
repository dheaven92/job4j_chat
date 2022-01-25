package ru.job4j.chat.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.job4j.chat.exception.NotFoundException;
import ru.job4j.chat.model.ErrorResponse;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Invalid request {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        "Invalid request",
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        log.error("Resource not found {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        "Resource not found",
                        e.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
        log.error("Not authorized {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        "Not authorized",
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value()
                ),
                HttpStatus.FORBIDDEN
        );
    }
}
