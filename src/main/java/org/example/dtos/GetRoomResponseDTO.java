package org.example.dtos;

import org.example.models.Room;

import java.util.List;

public class GetRoomResponseDTO {
    private ResponseStatus responseStatus;
    private List<Room> rooms;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
