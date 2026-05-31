package com.coding_challenge;

import java.io.File;
import com.coding_challenge.io.FileExporter;
import org.junit.jupiter.api.Test;
import com.coding_challenge.data.BankRecord;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileExportTest {
    private FileExporter exporter = new FileExporter();

    private List<BankRecord> sampleTransactions() {
        return List.of(
                new BankRecord("LT678909876", LocalDateTime.of(2026, 1, 15, 10, 0), "LT456789", "Groceries", -300.00, "EUR"),
                new BankRecord("LT678909876", LocalDateTime.of(2026, 2, 10, 10, 0), "LT987653", "Salary",    2000.00, "EUR"),
                new BankRecord("LT678909876", LocalDateTime.of(2026, 3, 5,  10, 0), "LT996627", "Rent",      -500.00, "EUR")
        );
    }

    @Test
    void exportCreatesFile() throws Exception {
        exporter.writeCsv(sampleTransactions(), null, null);
        var exports = Files.list(Path.of("exports")).toList();
        assertTrue(exports.size() > 0);
    }

    
    @Test
    void exportWritesCorrectNumberOfRows() throws Exception {
        exporter.writeCsv(sampleTransactions(), LocalDateTime.of(2026, 1, 1, 0, 0), LocalDateTime.of(2027, 1, 1, 0, 0));
        var exported = Files.list(Path.of("exports"))
                .sorted()
                .toList()
                .getLast(); // get most recently created file
        var lines = Files.readAllLines(exported);
        assertEquals(4, lines.size()); // 3 rows + 1 header
    }

    @Test
    void exportFiltersCorrectly() throws Exception {
        exporter.writeCsv(sampleTransactions(),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 2, 1, 0, 0)); // only January
        var exported = Files.list(Path.of("exports"))
                .sorted()
                .toList()
                .getLast();
        var lines = Files.readAllLines(exported);
        assertEquals(2, lines.size()); 
    }

}