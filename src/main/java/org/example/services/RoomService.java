package org.example.services;

import org.example.exceptions.UnAuthorisedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.Room;

public interface RoomService {
    Room addRoom(long userId, String roomName, double price, String rootType, String description) throws UserNotFoundException, UnAuthorisedAccess;
}
