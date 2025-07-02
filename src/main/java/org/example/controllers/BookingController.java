package org.example.controllers;

import org.example.dtos.GenerateInvoiceRequestDTO;
import org.example.dtos.GenerateInvoiceResponseDTO;
import org.example.dtos.ResponseStatus;
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
}
