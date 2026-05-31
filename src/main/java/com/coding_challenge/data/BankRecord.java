package com.coding_challenge.data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


public record BankRecord (
    String accountNr,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime date,
    String beneficiaryAcc,
    String comment,
    double amount,
    String currency
){}