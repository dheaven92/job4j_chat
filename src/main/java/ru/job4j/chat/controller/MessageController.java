package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.dto.MessageDto;
import ru.job4j.chat.exception.NotFoundException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;
import ru.job4j.chat.util.PatchUtil;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class MessageController {

    private final RoomService roomService;
    private final MessageService messageService;
    private final PersonService personService;

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestParam("room_id") int roomId, @RequestBody @Valid Message message) {
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new NotFoundException("Could not find a room with id " + roomId);
        }
        message.setAuthor(personService.getCurrentUser());
        message.setRoom(room);
        return new ResponseEntity<>(
                messageService.save(message),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/")
    public ResponseEntity<MessageDto> patch(
            @RequestParam("room_id") int roomId,
            @RequestBody MessageDto messageDto
    ) throws InvocationTargetException, IllegalAccessException {
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new NotFoundException("Could not find a room with id " + roomId);
        }
        if (messageDto.getId() == 0) {
            throw new IllegalArgumentException("Message id is required!");
        }
        Message original = messageService.findById(messageDto.getId());
        if (original == null) {
            throw new NotFoundException("Could not find a message with id " + messageDto.getId());
        }
        Message patched = PatchUtil.patch(messageDto.toMessage(), original);
        return new ResponseEntity<>(
                MessageDto.fromMessage(messageService.save(patched)),
                HttpStatus.OK
        );
    }
}
