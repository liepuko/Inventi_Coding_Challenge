package com.coding_challenge.calc;

import com.coding_challenge.data.BankRecord;
import java.time.LocalDateTime;
import java.util.List;

public class BalanceCalc {
    public double calculate (List<BankRecord> transactions, String accountNr, LocalDateTime dateFrom, LocalDateTime dateTo){
        var from = dateFrom != null ? dateFrom : LocalDateTime.MIN;
        var to   = dateTo   != null ? dateTo   : LocalDateTime.MAX;

         return transactions.stream()
            .filter(t -> t.date().isAfter(from) && t.date().isBefore(to))
            .filter(t -> t.accountNr().equals(accountNr))
            .mapToDouble(BankRecord::amount)
            .sum();
    }
}