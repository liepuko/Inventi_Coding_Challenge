package com.coding_challenge.calc;

import com.coding_challenge.data.BankRecord;
import java.time.LocalDateTime;
import java.util.List;

// Stream-based balance calculation & comments approach suggested by Claude
public class BalanceCalc {
    public double calculate (List<BankRecord> transactions, String accountNr,
                             LocalDateTime dateFrom, LocalDateTime dateTo){
         if (transactions == null) {
            throw new IllegalArgumentException("Transactions list cannot be null");
        }
        if (accountNr == null || accountNr.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be blank");
        }
        // If no start date is provided, use the earliest possible date
        var from = dateFrom != null ? dateFrom : LocalDateTime.MIN;
        // If no end date is provided, use the latest possible date
        var to = dateTo != null ? dateTo : LocalDateTime.MAX;

        // Filter transactions by date and account number, then sum their amounts
        return transactions.stream()
            .filter(t -> !t.date().isBefore(from) && !t.date().isAfter(to))
            .filter(t -> t.accountNr().equals(accountNr))
            .mapToDouble(BankRecord::amount)
            .sum();
    }
}