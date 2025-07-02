package org.example.repositories;

import org.example.models.Room;
import org.example.models.RoomType;

import java.util.*;
import java.util.stream.Collectors;

public class RoomRepositoryImpl implements RoomRepository {
    private Map<Long, Room> map = new HashMap<>();
    private List<Room> totalRooms = new ArrayList<>();
    private static int counter;

    public RoomRepositoryImpl() {
        counter = 1;
    }

    @Override
    public Room add(Room room) {
        map.put(room.getId(), room);
        return room;
    }

    @Override
    public List<Room> getRooms() {
        totalRooms.addAll(map.values());
        return totalRooms;
    }

    @Override
    public List<Room> getRoomsByRoomTypes(RoomType roomType) {
        System.out.println(map);
        return map.values().stream().filter(rooms -> rooms.getRoomType().equals(roomType)).collect(Collectors.toList());
    }

    @Override
    public Room save(Room room) {
        room.setId(counter++);
        map.put(room.getId(), room);
        return room;
    }

    @Override
    public Optional<Room> findById(long roomId) {
        return Optional.ofNullable(map.get(roomId));
    }
}
