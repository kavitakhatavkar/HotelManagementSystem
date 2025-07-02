package org.example.services;

import org.example.exceptions.UnAuthorisedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.Room;
import org.example.models.RoomType;
import org.example.models.User;
import org.example.models.UserType;
import org.example.repositories.RoomRepository;
import org.example.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    public RoomServiceImpl(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Room addRoom(long userId, String roomName, double price, String rootType, String description) throws UserNotFoundException, UnAuthorisedAccess {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        if (user.getUserType() != UserType.ADMIN) {
            throw new UnAuthorisedAccess("Admin access denied for user");
        }
        Room room = new Room();
        room.setDescriptions(description);
        room.setName(roomName);
        room.setPrice(price);
        room.setRoomType(RoomType.valueOf(rootType.toUpperCase()));
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getRooms(String roomType) {
        if (roomType == null) {
            return roomRepository.getRooms();
        }
        if (!roomType.equalsIgnoreCase(RoomType.DELUXE.name()) && !roomType.equalsIgnoreCase(RoomType.SUITE.name()) && !roomType.equalsIgnoreCase(RoomType.SUPER_DELUXE.name())) {
            return null;
        }
        return roomRepository.getRoomsByRoomTypes(RoomType.valueOf(roomType));
    }
}
