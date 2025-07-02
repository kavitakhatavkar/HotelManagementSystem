package org.example.services;

import org.example.exceptions.CustomerSessionNotFoundException;
import org.example.models.*;
import org.example.repositories.BookingRepository;
import org.example.repositories.CustomerSessionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerSessionRepository customerSessionRepository;
    public BookingServiceImpl(BookingRepository bookingRepository, CustomerSessionRepository customerSessionRepository) {
        this.bookingRepository = bookingRepository;
        this.customerSessionRepository = customerSessionRepository;
    }

    @Override
    public Invoice generateInvoice(long userId) throws CustomerSessionNotFoundException {
        Optional<CustomerSession> customerSessionOptional = customerSessionRepository.findActiveCustomerSessionByUserId(userId);
        if (customerSessionOptional.isEmpty()) {
            throw new CustomerSessionNotFoundException("CustomerSession not found");
        }
        CustomerSession customerSession = customerSessionOptional.get();
        Invoice invoice = new Invoice();
        invoice.setId(userId);
        customerSession.setCustomerSessionStatus(CustomerSessionStatus.ENDED);
        customerSessionRepository.save(customerSession);

        double totalPrice = 0.0;
        double totalGST = 0.0;
        double totalService = 0.0;

        Map<Room, Integer> bookedRoomsMap = new HashMap<>();
        /*merging all booked rooms in multiple booking into a single map while
        calculating total price of rooms*/
        List<Booking> bookings = bookingRepository.findBookingByCustomerSession(customerSession.getId());
        for(Booking b : bookings) {
            for(Map.Entry<Room, Integer> m : b.getBookedRooms().entrySet()) {
                totalPrice += m.getValue() * m.getKey().getPrice();
                if(bookedRoomsMap.containsKey(m.getKey())) {
                    bookedRoomsMap.put(m.getKey(),bookedRoomsMap.get(m.getKey())+m.getValue());
                } else {
                    bookedRoomsMap.put(m.getKey(),m.getValue());
                }
            }
        }

        totalGST = 0.18 * totalPrice;
        totalService = 0.1 * totalPrice;

        invoice.setBookedRooms(bookedRoomsMap);
        invoice.setGst(totalGST);
        invoice.setServiceCharge(totalService);
        invoice.setTotalAmount(totalPrice+totalGST+totalService);
        return invoice;
    }
}
