package com.coding_challenge;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create().start(7000);

        app.get("/", ctx -> ctx.result("Account Balance Service is running"));
    }
}