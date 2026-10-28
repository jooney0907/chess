package server;

import com.google.gson.Gson;
import jdk.management.jfr.RecordingInfo;
import service.*;
import io.javalin.*;
import io.javalin.http.*;
import model.*;
import dataaccess.*;
public class Server {

    private final Javalin javalin;
    private final AdminService adminService;
    private final UserService userService;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        var da = new MemoryDataAccess();
        adminService = new AdminService(da);
        userService = new UserService(da);
        javalin.post("/user",this::register);
        javalin.delete("/db", this::clear);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game",this::joinGame);
    }

    private void register(Context context) {
        var serializer=new Gson();
        try {
            String reqJson=context.body();
            var user=serializer.fromJson(reqJson, RegisterRequest.class);
            var authData=userService.register(user);
            context.result(serializer.toJson(authData));
        } catch (Exception ex) {
            var message = String.format("{\"message\": \"Error: %s\" }", ex.getMessage());
            if (ex.getMessage().equals("403 Error")) {
                context.status(403).result(message);
            }
            else {
                context.status(400).result(message);
            }
        }
    }

    private void clear(Context context) throws DataAccessException {
        adminService.clear();
        context.result("{}");
    }
    private void login(Context context){
        var serializer = new Gson();
        try {
            String reqJson = context.body();
            var request = serializer.fromJson(reqJson, LoginRequest.class);
            var authData = userService.login(request);
            context.contentType("application/json");
            context.result(serializer.toJson(authData));
        } catch (Exception ex) {
            var message = String.format(
                    "{\"message\": \"Error: %s\" }", ex.getMessage()
            );
            if (ex.getMessage().equals("401 Error")) {
                context.status(401).result(message);
            } else {
                context.status(400).result(message);
            }
        }
    }
    private void logout(Context context){
        try {
            String authToken = context.header("authorization");
            var request = new LogoutRequest(authToken);
            userService.logout(request);
            context.contentType("application/json");
            context.result("{}");
        } catch (Exception ex) {
            var message = String.format(
                    "{\"message\": \"Error: %s\" }", ex.getMessage()
            );
            if ("401 Error".equals(ex.getMessage())) {
                context.status(401).result(message);
            } else {
                context.status(400).result(message);
            }
        }

    }

    private void listGames(Context context){

    }
    private void createGame(Context context){

    }
    private void joinGame(Context context){

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
