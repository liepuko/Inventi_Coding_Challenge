package com.coding_challenge.service;

import com.coding_challenge.data.BankRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;

public interface DataService {
    List<BankRecord> importData(InputStream inputStream) throws Exception;
    double calculateBalance(String accountNr, LocalDateTime dateFrom, LocalDateTime dateTo);
    Path exportData(LocalDateTime dateFrom, LocalDateTime dateTo) throws Exception;
}