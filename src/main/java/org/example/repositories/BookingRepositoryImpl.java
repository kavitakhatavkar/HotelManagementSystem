package org.example.repositories;

import org.example.models.Booking;

import java.util.*;

public class BookingRepositoryImpl implements BookingRepository {
    private Map<Long,List<Booking>> bookingMap = new HashMap<>();
    @Override
    public Booking save(Booking booking) {
        Long customerSessionId = booking.getCustomerSession().getId();
        if(bookingMap.containsKey(customerSessionId)) {
            List<Booking> newBookings = bookingMap.get(customerSessionId);
            newBookings.add(booking);
            bookingMap.put(customerSessionId, newBookings);
        } else {
            bookingMap.put(customerSessionId, new ArrayList<>(List.of(booking)));
        }
        return booking;
    }

    @Override
    public List<Booking> findBookingByCustomerSession(long customerSessionId) {
        return bookingMap.get(customerSessionId);
    }
}