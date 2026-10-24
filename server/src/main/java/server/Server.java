package server;

import io.javalin.*;
import model.*;
import dataaccess.*;
public class Server {

    private final Javalin javalin;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        var dataAccess = new MemoryDataAccess();
        javalin.post("/user",ctx->ctx.result("{}"));
        javalin.delete("/db",ctx->ctx.result("{}"));
        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
