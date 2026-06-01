package com.coding_challenge.io;

import com.coding_challenge.data.BankRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Auto-generated filename with timestamp pattern suggested by Claude
public class FileExporter{

    private static final String OUTPUT_DIR = "exports";

     public Path writeCsv(List<BankRecord> transactions,  LocalDateTime dateFrom,
                                         LocalDateTime dateTo) throws Exception {

        Files.createDirectories(Path.of(OUTPUT_DIR)); //creates a folder for exports if it does not exist

        // Add current time to the file name for better orientation and so exports do not overwrite each other
        var timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        var filePath = Path.of(OUTPUT_DIR, "statement_" + timestamp + ".csv");

        // If dateFrom/dateTo is not provided, export from/until the earliest/latest possible date
        var from = dateFrom != null ? dateFrom : LocalDateTime.MIN;
        var to = dateTo != null ? dateTo : LocalDateTime.MAX;

        var filtered = transactions.stream()
            // The date range is inclusive, so transactions exactly on the boundary are included.
            .filter(t -> !t.date().isBefore(from) && !t.date().isAfter(to))
            .toList();

        // write filtered transactions into a csv
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
        // return the generated file path so the route can send it to the frontend
        return filePath;
    }

}