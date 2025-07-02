package org.example.repositories;

import org.example.models.Room;
import org.example.models.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room add(Room room);
    List<Room> getRooms();
    List<Room> getRoomsByRoomTypes(RoomType roomType);
    Room save(Room room);
    Optional<Room> findById(long roomId);
}
