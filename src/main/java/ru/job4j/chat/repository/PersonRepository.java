package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findByUsername(String username);

    boolean existsByUsername(String username);
}
