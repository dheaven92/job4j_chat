package ru.job4j.chat.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.job4j.chat.exception.NotFoundException;
import ru.job4j.chat.model.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Bad request {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        "Bad request",
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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<Map<String, String>>> handleMethodArgumentNotValidExceptionException(MethodArgumentNotValidException e) {
        log.error("Invalid request {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(field -> Map.of(
                                field.getField(),
                                String.format("%s. Actual value: %s", field.getDefaultMessage(), field.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
