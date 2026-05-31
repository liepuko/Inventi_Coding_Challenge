package com.coding_challenge.exceptions;

public class InvalidCsvFormatException extends RuntimeException {
    public InvalidCsvFormatException(String message) {
        super(message);
    }
}