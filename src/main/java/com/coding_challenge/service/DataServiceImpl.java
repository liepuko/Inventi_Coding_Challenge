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

public class DataServiceImpl implements DataService {

    private final FileImporter importer   = new FileImporter();
    private final FileExporter exporter   = new FileExporter();
    private final BalanceCalc  calculator = new BalanceCalc();

    @Override
    public List<BankRecord> importData(String filePath) throws Exception {
        var file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        return importer.readCsv(new FileInputStream(filePath));
    }

    @Override
    public double calculateBalance(List<BankRecord> transactions, String accountNr, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return calculator.calculate(transactions, accountNr, dateFrom, dateTo);
    }

    @Override
    public void exportData(List<BankRecord> transactions, LocalDateTime dateFrom, LocalDateTime dateTo) throws Exception {
        exporter.writeCsv(transactions, dateFrom, dateTo);
    }
}