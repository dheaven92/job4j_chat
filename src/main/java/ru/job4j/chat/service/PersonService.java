package ru.job4j.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoleRepository;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    private final static String ROLE_ADMIN = "ADMIN";
    private final static String ROLE_USER = "USER";

    public Person findById(int id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person create(Person person) {
        Role role = roleRepository.findByName(ROLE_USER);
        if (role == null) {
            throw new IllegalArgumentException(String.format("Can't create user. Could not find role %s.", ROLE_USER));
        }
        if (personRepository.existsByUsername(person.getUsername())) {
            throw new IllegalArgumentException("Can't create user. Username is already used by another user.");
        }
        person.setRole(role);
        return personRepository.save(person);
    }

    public boolean isAdmin(Person person) {
        return ROLE_ADMIN.equals(person.getRole().getName());
    }
}
