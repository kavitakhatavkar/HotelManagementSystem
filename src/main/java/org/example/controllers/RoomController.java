package org.example.controllers;

import org.example.dtos.*;
import org.example.models.Room;
import org.example.services.RoomService;

import java.util.List;

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

    public GetRoomResponseDTO getRooms(GetRoomsRequestDTO getRoomsRequestDTO) {
        GetRoomResponseDTO getRoomResponseDTO = new GetRoomResponseDTO();
        String roomType = getRoomsRequestDTO.getRoomType();
        List<Room> roomList = roomService.getRooms(roomType);

        if (roomList != null) {
            getRoomResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } else {
            getRoomResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        getRoomResponseDTO.setRooms(roomList);
        return getRoomResponseDTO;
    }
}
