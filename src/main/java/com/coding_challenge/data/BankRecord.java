package com.coding_challenge.data;
import java.time.LocalDateTime;


public record BankRecord (
    String accountNr,
    LocalDateTime date,
    String beneficiaryAcc,
    String comment,
    double amount,
    String currency
){}