package org.example.dtos;

import java.util.Map;

public class MakingBookingRequestDTO {
    private long userId;
    private Map<Long, Integer> bookedRooms;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Map<Long, Integer> getBookedRooms() {
        return bookedRooms;
    }

    public void setBookedRooms(Map<Long, Integer> bookedRooms) {
        this.bookedRooms = bookedRooms;
    }
}
