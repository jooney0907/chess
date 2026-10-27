package server;

import com.google.gson.Gson;
import service.*;
import io.javalin.*;
import model.*;
import dataaccess.*;
public class Server {

    private final Javalin javalin;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        var da = new MemoryDataAccess();
        var adminService = new AdminService(da);
        var userService = new UserService(da);
        javalin.post("/user",ctx->ctx.result("{}"));
        javalin.delete("/db", ctx -> {adminService.clear();ctx.result("{}");});

    }

    private void register(){
        var serializer = new Gson();
    }

    private void clear(){

    }
    private void login(){

    }
    private void logout(){

    }

    private void listGames(){

    }
    private void createGame(){

    }
    private void joinGame(){

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
