package com.coding_challenge.calc;

import com.coding_challenge.data.BankRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.coding_challenge.exceptions.AccountNotFoundException;

// Stream-based balance calculation & comments approach suggested by Claude
public class BalanceCalc {
    public Map<String, Double> calculate (List<BankRecord> transactions, String accountNr,
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

        // Filter transactions by date and account number, then sum their amounts by currency
        var result = transactions.stream()
            .filter(t -> !t.date().isBefore(from) && !t.date().isAfter(to))
            .filter(t -> t.accountNr().equals(accountNr))
            .collect(Collectors.groupingBy(
                        BankRecord::currency,
                        Collectors.summingDouble(BankRecord::amount)
                ));
        if (result.isEmpty()) {
            throw new AccountNotFoundException(accountNr);
        }

        return result;
    }
}