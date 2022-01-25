package ru.job4j.chat.dto;

import lombok.Getter;
import lombok.Setter;
import ru.job4j.chat.model.Room;

@Getter
@Setter
public class RoomDto {

    private int id;

    private String name;

    public Room toRoom() {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        return room;
    }

    public static RoomDto fromRoom(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setName(room.getName());
        return roomDto;
    }
}
