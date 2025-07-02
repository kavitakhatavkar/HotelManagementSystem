package org.example.repositories;

import org.example.models.Room;
import org.example.models.RoomType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomRepositoryImpl implements RoomRepository {
    private Map<RoomType, List<Room>> map = new HashMap<>();
    private List<Room> totalRooms = new ArrayList<>();

    @Override
    public Room add(Room room) {
        RoomType roomType = room.getRoomType();
        List<Room> roomList = null;
        if(map.containsKey(roomType)) {
            roomList = map.get(roomType);
        } else {
            roomList = new ArrayList<>();
        }
        roomList.add(room);
        map.put(roomType, roomList);
        return room;
    }

    @Override
    public List<Room> getRooms() {
        for(List<Room> rooms : map.values()){
            totalRooms.addAll(rooms);
        }
        return totalRooms;
    }

    @Override
    public List<Room> getRoomsByRoomTypes(RoomType roomType) {
        return map.get(roomType);
    }

    @Override
    public Room save(Room room) {
        RoomType roomType = room.getRoomType();
        List<Room> roomList = null;

        if(map.containsKey(roomType)) {
            roomList = map.get(roomType);
        } else {
            roomList = new ArrayList<>();
        }
        roomList.add(room);
        map.put(roomType, roomList);
        return room;
    }
}
