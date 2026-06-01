package com.coding_challenge.io;

import com.coding_challenge.data.BankRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;

public class FileExporter{

    private static final String OUTPUT_DIR = "exports";

     public Path writeCsv(List<BankRecord> transactions,  LocalDateTime dateFrom, LocalDateTime dateTo) throws Exception {

        Files.createDirectories(Path.of(OUTPUT_DIR)); //creates a folder for exports if it does not exist

        var timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        var filePath = Path.of(OUTPUT_DIR, "statement_" + timestamp + ".csv");

        var from = dateFrom != null ? dateFrom : LocalDateTime.MIN;
        var to   = dateTo   != null ? dateTo   : LocalDateTime.MAX;

        var filtered = transactions.stream()
            .filter(t -> t.date().isAfter(from) && t.date().isBefore(to))
            .toList();

        try (var writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write("Account number,Operation time,Beneficiary,Comment,Amount,Currency");
            writer.newLine();
            for (var t : filtered) {
                writer.write("%s,%s,%s,%s,%.2f,%s".formatted(
                        t.accountNr(),
                        t.date(),
                        t.beneficiaryAcc(),
                        t.comment(),
                        t.amount(),
                        t.currency()
                ));
                writer.newLine();
            }
        }
        return filePath;
    }

}