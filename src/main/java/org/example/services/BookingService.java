package org.example.services;

import org.example.exceptions.CustomerSessionNotFoundException;
import org.example.exceptions.InvalidRoomException;
import org.example.exceptions.UserNotFoundException;
import org.example.models.Booking;
import org.example.models.Invoice;

import java.util.Map;

public interface BookingService {
    Invoice generateInvoice(long userId) throws CustomerSessionNotFoundException;
    Booking makeBooking(long userId, Map<Long, Integer> roomsToBeBooked) throws UserNotFoundException, InvalidRoomException;
}
