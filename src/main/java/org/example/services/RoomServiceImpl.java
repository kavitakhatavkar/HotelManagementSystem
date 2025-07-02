package org.example.services;

import org.example.exceptions.UnAuthorisedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.Room;
import org.example.models.RoomType;
import org.example.models.User;
import org.example.models.UserType;
import org.example.repositories.RoomRepository;
import org.example.repositories.UserRepository;

import java.util.Optional;

public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private UserRepository userRepository;

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
        return roomRepository.add(room);
    }
}
