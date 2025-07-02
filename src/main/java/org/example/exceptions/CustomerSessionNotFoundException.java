package org.example.exceptions;

public class CustomerSessionNotFoundException extends Exception {
    public CustomerSessionNotFoundException(String message) {
        super(message);
    }
}
