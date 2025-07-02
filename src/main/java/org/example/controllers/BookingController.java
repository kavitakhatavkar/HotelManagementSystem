package org.example.controllers;

import org.example.dtos.*;
import org.example.models.Booking;
import org.example.models.Invoice;
import org.example.services.BookingService;

public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    public GenerateInvoiceResponseDTO generateInvoice(GenerateInvoiceRequestDTO generateInvoiceRequestDTO) {
        Invoice invoice = null;
        GenerateInvoiceResponseDTO generateInvoiceResponseDTO = new GenerateInvoiceResponseDTO();
        try {
            invoice = bookingService.generateInvoice(generateInvoiceRequestDTO.getUserId());
            generateInvoiceResponseDTO.setInvoice(invoice);
            generateInvoiceResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            generateInvoiceResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return generateInvoiceResponseDTO;
    }

    public MakingBookingResponseDTO makeBooking(MakingBookingRequestDTO makingBookingRequestDTO) {
        MakingBookingResponseDTO makingBookingResponseDTO = new MakingBookingResponseDTO();
        try {
            Booking booking = bookingService.makeBooking(makingBookingRequestDTO.getUserId(), makingBookingRequestDTO.getBookedRooms());
            makingBookingResponseDTO.setBooking(booking);
            makingBookingResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            makingBookingResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return makingBookingResponseDTO;
    }
}
