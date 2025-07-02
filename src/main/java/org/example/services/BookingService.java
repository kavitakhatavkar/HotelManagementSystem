package org.example.services;

import org.example.exceptions.CustomerSessionNotFoundException;
import org.example.models.Invoice;

public interface BookingService {
    Invoice generateInvoice(long userId) throws CustomerSessionNotFoundException;
}
