package server;

import service.AdminService;
import io.javalin.*;
import model.*;
import dataaccess.*;
public class Server {

    private final Javalin javalin;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        var da = new MemoryDataAccess();
        var adminService = new AdminService(da);
        javalin.post("/user",ctx->ctx.result("{}"));
        javalin.delete("/db", ctx -> {adminService.clear();ctx.result("{}");});
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
