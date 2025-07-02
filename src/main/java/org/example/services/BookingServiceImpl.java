package org.example.services;

import org.example.exceptions.CustomerSessionNotFoundException;
import org.example.models.Invoice;

public class BookingServiceImpl implements BookingService {

    @Override
    public Invoice generateInvoice(long userId) throws CustomerSessionNotFoundException {
        return null;
    }
}
