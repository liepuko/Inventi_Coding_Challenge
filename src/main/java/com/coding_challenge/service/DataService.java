package com.coding_challenge.service;

import com.coding_challenge.data.BankRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface DataService {
    List<BankRecord> importData(String filePath) throws Exception;
    double calculateBalance(List<BankRecord> transactions, String accountNr, LocalDateTime dateFrom, LocalDateTime dateTo);
    void exportData(List<BankRecord> transactions, LocalDateTime dateFrom, LocalDateTime dateTo) throws Exception;
}