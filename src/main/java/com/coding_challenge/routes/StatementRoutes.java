package com.coding_challenge.routes;

import com.coding_challenge.data.BankRecord;
import com.coding_challenge.service.DataService;
import io.javalin.Javalin;

import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Files;

public class StatementRoutes {

    private final DataService service;

    private static final DateTimeFormatter FORMATTER = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatementRoutes(DataService service) {
        this.service = service;
    }

    public void register(Javalin app) {

        // POST /api/import
        app.post("/api/import", ctx -> {
            var file = ctx.uploadedFile("file");
            if (file == null) {
                throw new IllegalArgumentException("file is required");
            }
            if (!file.filename().endsWith(".csv")) {
                throw new IllegalArgumentException("Only CSV files are supported");
            }
            var transactions = service.importData(file.content());
            ctx.json(transactions);
        });

        // GET /api/calculate?accountNr=...&dateFrom=...&dateTo=...
        app.get("/api/calculate", ctx -> {
            var accountNr = ctx.queryParam("accountNr");
            if (accountNr == null || accountNr.isBlank()) {
                throw new IllegalArgumentException("accountNr is required");
            }
            var dateFrom = ctx.queryParam("dateFrom") != null && 
            !ctx.queryParam("dateFrom").isBlank() ? LocalDateTime.parse(ctx.queryParam("dateFrom"), FORMATTER) : null;

            var dateTo = ctx.queryParam("dateTo") != null &&
             !ctx.queryParam("dateTo").isBlank() ? LocalDateTime.parse(ctx.queryParam("dateTo"), FORMATTER) : null;
            
            var balance = service.calculateBalance(accountNr, dateFrom, dateTo);
            ctx.json(balance);
        });

        // POST /api/export
        app.post("/api/export", ctx -> {
            var dateFrom = ctx.queryParam("dateFrom") != null &&
            !ctx.queryParam("dateTo").isBlank()
            ? LocalDateTime.parse(ctx.queryParam("dateFrom"), FORMATTER) : null;

            var dateTo = ctx.queryParam("dateTo") != null &&
            !ctx.queryParam("dateTo").isBlank() ? LocalDateTime.parse(ctx.queryParam("dateTo"), FORMATTER)   : null;

            var path = service.exportData(dateFrom, dateTo);

            ctx.contentType("text/csv")
                .header("Content-Disposition", "attachment; filename=" + path.getFileName())
                .result(Files.readAllBytes(path));
        });
    }
}