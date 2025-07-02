package org.example.services;

import org.example.exceptions.CustomerSessionNotFoundException;
import org.example.exceptions.InvalidRoomException;
import org.example.exceptions.UserNotFoundException;
import org.example.models.*;
import org.example.repositories.BookingRepository;
import org.example.repositories.CustomerSessionRepository;
import org.example.repositories.RoomRepository;
import org.example.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final CustomerSessionRepository customerSessionRepository;
    public BookingServiceImpl(UserRepository userRepository, RoomRepository roomRepository, BookingRepository bookingRepository, CustomerSessionRepository customerSessionRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
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

    @Override
    public Booking makeBooking(long userId, Map<Long, Integer> roomsToBeBooked) throws UserNotFoundException, InvalidRoomException {
        Optional<CustomerSession> customerSessionOptional = customerSessionRepository.findActiveCustomerSessionByUserId(userId);
        CustomerSession customerSession;
        if (customerSessionOptional.isEmpty()) {
            customerSession = new CustomerSession();
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            User user = userOptional.get();
            customerSession.setUser(user);
            customerSession.setCustomerSessionStatus(CustomerSessionStatus.ACTIVE);
            customerSessionRepository.save(customerSession);
        } else {
            customerSession = customerSessionOptional.get();
        }

        Map<Room, Integer> bookedRooms = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : roomsToBeBooked.entrySet()) {
            long roomId = entry.getKey();
            int numRooms = entry.getValue();

            Optional<Room> roomOptional = roomRepository.findById(roomId);
            if (roomOptional.isEmpty()) throw new InvalidRoomException("Room not found");
            Room room = roomOptional.get();
            bookedRooms.put(room, numRooms);
        }
        Booking booking = new Booking();
        booking.setBookedRooms(bookedRooms);
        booking.setCustomerSession(customerSession);
        return bookingRepository.save(booking);
    }

}
