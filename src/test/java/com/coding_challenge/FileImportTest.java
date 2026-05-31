package com.coding_challenge;

import com.coding_challenge.data.BankRecord;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import com.coding_challenge.io.FileImporter;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileImportTest {
    private FileImporter importer = new FileImporter();

    private ByteArrayInputStream sampleCsv() {
        var csv = """
                accountNr,date,beneficiaryAcc,comment,amount,currency
                LT678909876,2026-09-13 12:00:00,LT456789,Groceries,-300.00,EUR
                LT676609873,2026-05-11 14:07:30,LT450089,Snacks,-10.00,EUR
                LT600628943,2026-02-09 10:00:00,LT450563,Salary,1000.00,EUR
                """;
        return new ByteArrayInputStream(csv.getBytes());
    }

    @Test
    void importReturnsCorrectNumberOfRecords() throws Exception {
        var result = importer.readCsv(sampleCsv());
        assertEquals(3, result.size());
    }

    @Test
    void importParsesFirstRecordCorrectly() throws Exception {
        var result = importer.readCsv(sampleCsv());
        var first = result.get(0);
        assertEquals("LT678909876", first.accountNr());
        assertEquals(-300.00, first.amount(), 0.01);
        assertEquals("EUR", first.currency());
        assertEquals(LocalDateTime.of(2026, 9, 13, 12, 0, 0), first.date());
    }

    @Test
    void importSkipsEmptyLines() throws Exception {
        var csv = """
                accountNr,date,beneficiaryAcc,comment,amount,currency
                LT678909876,2026-09-13 12:00:00,LT456789,Groceries,-300.00,EUR
                
                LT676609873,2026-05-11 14:07:30,LT450089,Snacks,-10.00,EUR
                """;
        var result = importer.readCsv(new ByteArrayInputStream(csv.getBytes()));
        assertEquals(2, result.size());
    }




}