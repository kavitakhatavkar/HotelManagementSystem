package org.example.repositories;

import org.example.models.Room;
import org.example.models.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomRepositoryImpl implements RoomRepository {
    List<Room> rooms = new ArrayList<>();

    @Override
    public Room add(Room room) {
        rooms.add(room);
        return room;
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public List<Room> getRoomsByRoomTypes(RoomType roomType) {
        return rooms.stream().filter(room -> room.getRoomType().equals(roomType)).collect(Collectors.toList());
    }

    @Override
    public Room save(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (room.getId() == rooms.get(i).getId()) {
                rooms.set(i, room);
            }
        }
        return room;
    }
}
