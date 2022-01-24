package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.security.JwtTokenProvider;
import ru.job4j.chat.service.PersonService;

@RequiredArgsConstructor
@RequestMapping("/person")
@RestController
public class PersonController {

    private final PersonService personService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                personService.create(person),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Person personRequest) {
        String username = personRequest.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, personRequest.getPassword()));
        Person person = personService.findByUsername(username);
        if (person == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        String token = jwtTokenProvider.createToken(username);
        return ResponseEntity.ok(token);
    }
}
