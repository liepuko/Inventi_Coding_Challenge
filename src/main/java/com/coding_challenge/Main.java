package com.coding_challenge;

import com.coding_challenge.routes.StatementRoutes;
import com.coding_challenge.service.DataServiceImpl;
import com.coding_challenge.exceptions.InvalidCsvFormatException;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            // Allows the React frontend to call this backend
            config.bundledPlugins.enableCors(cors -> cors.addRule(it ->{
                it.anyHost();
                it.exposeHeader("Content-Disposition"); // for frontend to read the exported CSV filename
            } ));
        }).start(7000);

        //exception handling (suggested by claude)
    
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400).result("Bad request: " + e.getMessage());
        });

        app.exception(java.io.FileNotFoundException.class, (e, ctx) -> {
            ctx.status(404).result("File not found: " + e.getMessage());
        });

        app.exception(java.time.format.DateTimeParseException.class, (e, ctx) -> {
            ctx.status(400).result("Invalid date format. Use: yyyy-MM-dd HH:mm:ss");
        });

        app.exception(InvalidCsvFormatException.class, (e, ctx) -> {
            ctx.status(400).result("Invalid CSV format: " + e.getMessage());
        });

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500).result("Server error: " + e.getMessage());
        });

        var service = new DataServiceImpl();

        // Register all statement-related API routes
        new StatementRoutes(service).register(app);
    }
}