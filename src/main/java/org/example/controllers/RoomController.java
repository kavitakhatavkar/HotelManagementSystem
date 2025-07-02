package org.example.controllers;

import org.example.dtos.AddRoomRequestDTO;
import org.example.dtos.AddRoomResponseDto;
import org.example.dtos.ResponseStatus;
import org.example.models.Room;
import org.example.services.RoomService;

public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public AddRoomResponseDto addRoom(AddRoomRequestDTO addRoomRequestDTO) {
        AddRoomResponseDto addRoomResponseDto = new AddRoomResponseDto();
        try {
            Room room = roomService.addRoom(addRoomRequestDTO.getUserId(), addRoomRequestDTO.getName(), addRoomRequestDTO.getPrice(), addRoomRequestDTO.getRoomType(), addRoomRequestDTO.getDescription());
            addRoomResponseDto.setRoom(room);
            addRoomResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            addRoomResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return addRoomResponseDto;
    }
}
