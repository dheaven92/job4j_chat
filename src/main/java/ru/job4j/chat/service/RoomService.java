package ru.job4j.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final MessageService messageService;

    public List<Room> findAll() {
        return (List<Room>) roomRepository.findAll();
    }

    public Room findById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room create(Room room) {
        if (roomRepository.existsByName(room.getName())) {
            throw new IllegalArgumentException("Can't create room. Name is already used with another room.");
        }
        return roomRepository.save(room);
    }

    @Transactional
    public void delete(Room room) {
        messageService.deleteAllByRoomId(room.getId());
        roomRepository.delete(room);
    }
}
