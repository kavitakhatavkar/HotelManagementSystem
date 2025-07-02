package org.example.repositories;

import org.example.models.Booking;

import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    List<Booking> findBookingByCustomerSession(long customerSessionId);
}
