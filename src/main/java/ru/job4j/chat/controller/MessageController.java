package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class MessageController {

    private final RoomService roomService;
    private final PersonService personService;
    private final MessageService messageService;

    @PostMapping("/")
    public ResponseEntity<Message> create(
            @RequestParam("author_id") int authorId,
            @RequestParam("room_id") int roomId,
            @RequestBody Message message
    ) {
        Person author = personService.findById(authorId);
        Room room = roomService.findById(roomId);
        if (author == null || room == null) {
            return ResponseEntity.notFound().build();
        }
        message.setAuthor(author);
        message.setRoom(room);
        return new ResponseEntity<>(
                messageService.create(message),
                HttpStatus.CREATED
        );
    }
}
