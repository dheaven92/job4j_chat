package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.exception.NotFoundException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class MessageController {

    private final RoomService roomService;
    private final MessageService messageService;
    private final PersonService personService;

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestParam("room_id") int roomId, @RequestBody Message message) {
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new NotFoundException("Could not find a room with id " + roomId);
        }
        message.setAuthor(personService.getCurrentUser());
        message.setRoom(room);
        return new ResponseEntity<>(
                messageService.create(message),
                HttpStatus.CREATED
        );
    }
}
