package ru.job4j.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.dto.RoomDto;
import ru.job4j.chat.exception.NotFoundException;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.RoomService;
import ru.job4j.chat.util.PatchUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/")
    public ResponseEntity<List<Room>> getAll() {
        return new ResponseEntity<>(roomService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") int id) {
        Room room = roomService.findById(id);
        if (room == null) {
            throw new NotFoundException("Could not find a room with id " + id);
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

    @PatchMapping("/")
    public ResponseEntity<RoomDto> patch(@RequestBody RoomDto roomDto) throws InvocationTargetException, IllegalAccessException {
        if (roomDto.getId() == 0) {
            throw new IllegalArgumentException("Room id is required!");
        }
        Room original = roomService.findById(roomDto.getId());
        if (original == null) {
            throw new NotFoundException("Could not find a room with id " + roomDto.getId());
        }
        Room patched = PatchUtil.patch(roomDto.toRoom(), original);
        return new ResponseEntity<>(
                RoomDto.fromRoom(roomService.update(patched)),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        Room room = roomService.findById(id);
        if (room == null) {
            throw new NotFoundException("Could not find a room with id " + id);
        }
        roomService.delete(room);
        return ResponseEntity.ok().build();
    }
}
