package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {

    private final RoomService roomService;
    private final PersonService personService;

    @GetMapping("/")
    public List<Room> getAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") int id) {
        Room room = roomService.findById(id);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(
                roomService.create(room),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") int id,
            @RequestParam(value = "user_id") int userId
    ) {
        Room room = roomService.findById(id);
        Person user = personService.findById(userId);
        if (room == null || user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!personService.isAdmin(user)) {
            return ResponseEntity.badRequest().build();
        }
        roomService.delete(room);
        return ResponseEntity.ok().build();
    }
}
