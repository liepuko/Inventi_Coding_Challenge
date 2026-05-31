package com.coding_challenge;

import com.coding_challenge.routes.StatementRoutes;
import com.coding_challenge.service.DataServiceImpl;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create().start(7000);

        var service = new DataServiceImpl();
        new StatementRoutes(service).register(app);
    }
}