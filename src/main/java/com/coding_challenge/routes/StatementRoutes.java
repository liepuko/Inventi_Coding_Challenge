package com.coding_challenge.routes;

import com.coding_challenge.data.BankRecord;
import com.coding_challenge.service.DataService;
import io.javalin.Javalin;

import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class StatementRoutes {

    private final DataService service;

    private static final DateTimeFormatter FORMATTER = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatementRoutes(DataService service) {
        this.service = service;
    }

    public void register(Javalin app) {

        // POST /api/import?filePath=path/to/file.csv
        app.post("/api/import", ctx -> {
            var filePath = ctx.queryParam("filePath");
            if (filePath == null || filePath.isBlank()) {
                throw new IllegalArgumentException("filePath is required");
            }
            var transactions = service.importData(filePath);
            ctx.json(transactions);
        });

        // GET /api/calculate?accountNr=...&dateFrom=...&dateTo=...
        app.get("/api/calculate", ctx -> {
            var accountNr = ctx.queryParam("accountNr");
            if (accountNr == null || accountNr.isBlank()) {
                throw new IllegalArgumentException("accountNr is required");
            }
            var dateFrom = ctx.queryParam("dateFrom") != null ? LocalDateTime.parse(ctx.queryParam("dateFrom"), FORMATTER)  : null;
            var dateTo = ctx.queryParam("dateTo") != null ? LocalDateTime.parse(ctx.queryParam("dateTo"), FORMATTER)    : null;
            var transactions = service.importData(ctx.queryParam("filePath"));
            var balance = service.calculateBalance(transactions, accountNr, dateFrom, dateTo);
            ctx.json(balance);
        });

        // GET /api/export?dateFrom=...&dateTo=...
        app.get("/api/export", ctx -> {
            var dateFrom = ctx.queryParam("dateFrom") != null ? LocalDateTime.parse(ctx.queryParam("dateFrom"), FORMATTER) : null;
            var dateTo = ctx.queryParam("dateTo") != null ? LocalDateTime.parse(ctx.queryParam("dateTo"), FORMATTER)   : null;
            var transactions = service.importData(ctx.queryParam("filePath"));
            service.exportData(transactions, dateFrom, dateTo);
            ctx.result("Export successful");
        });
    }
}