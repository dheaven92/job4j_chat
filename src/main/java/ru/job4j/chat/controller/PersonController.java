package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.ErrorResponse;
import ru.job4j.chat.model.Operation;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.security.JwtTokenProvider;
import ru.job4j.chat.service.PersonService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/person")
@RestController
public class PersonController {

    private final PersonService personService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Person> register(@RequestBody @Validated(Operation.OnAuthentication.class) Person person) {
        return new ResponseEntity<>(
                personService.create(person),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated(Operation.OnAuthentication.class) Person personRequest) {
        try {
            String username = personRequest.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, personRequest.getPassword()));
            Person person = personService.findByUsername(username);
            if (person == null) {
                throw new UsernameNotFoundException("User with username " + username + " not found");
            }
            String token = jwtTokenProvider.createToken(username);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(Exception e) {
        log.error("Authentication failed {}", e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        "Authentication failed",
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value()
                ),
                HttpStatus.FORBIDDEN
        );
    }
}
