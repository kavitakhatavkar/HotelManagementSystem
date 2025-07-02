package org.example.dtos;

import org.example.models.Invoice;

public class GenerateInvoiceResponseDTO {
    private ResponseStatus responseStatus;
    private Invoice invoice;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
