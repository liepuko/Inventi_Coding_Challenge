package com.coding_challenge.service;

import com.coding_challenge.calc.BalanceCalc;
import com.coding_challenge.data.BankRecord;
import com.coding_challenge.io.FileExporter;
import com.coding_challenge.io.FileImporter;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;

public class DataServiceImpl implements DataService {

    private final FileImporter importer   = new FileImporter();
    private final FileExporter exporter   = new FileExporter();
    private final BalanceCalc  calculator = new BalanceCalc();

    private List<BankRecord> storedTransactions = new ArrayList<>();

    @Override
    public List<BankRecord> importData(InputStream inputStream) throws Exception {
        // var file = new File(filePath);
        // if (!file.exists()) {
        //     throw new FileNotFoundException("File not found: " + filePath);
        // }
        // if (!filePath.endsWith(".csv")) {
        //     throw new IllegalArgumentException("Only CSV files are supported");
        // }
        storedTransactions = importer.readCsv(inputStream);
        return storedTransactions;
    }

    @Override
    public double calculateBalance(String accountNr, LocalDateTime dateFrom, LocalDateTime dateTo) {
         if (storedTransactions.isEmpty()) {
            throw new IllegalArgumentException("No transactions imported yet");
        }
        return calculator.calculate(storedTransactions, accountNr, dateFrom, dateTo);
    }

    @Override
    public Path exportData(LocalDateTime dateFrom, LocalDateTime dateTo) throws Exception {
        if (storedTransactions.isEmpty()) {
            throw new IllegalArgumentException("No transactions imported yet");
        }
        return exporter.writeCsv(storedTransactions, dateFrom, dateTo);
    }
}