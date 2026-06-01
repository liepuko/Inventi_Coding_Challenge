package com.coding_challenge.exceptions;

// Custom exception used when the uploaded CSV file has invalid data
public class InvalidCsvFormatException extends RuntimeException {
    public InvalidCsvFormatException(String message) {
        super(message);
    }
}