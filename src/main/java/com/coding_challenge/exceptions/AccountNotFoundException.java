package com.coding_challenge.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNr) {
        super("Account not found: " + accountNr);
    }
}