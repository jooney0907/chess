package server;

import com.google.gson.Gson;
import dataaccess.sql.SqlDataAccess;
import service.*;
import io.javalin.*;
import io.javalin.http.*;
import dataaccess.*;

import java.util.*;

public class Server {

    private final Javalin javalin;
    private final AdminService adminService;
    private final UserService userService;
    private final GameService gameService;

    public Server(){
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        DataAccess da;
        try {
            da = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new IllegalStateException("Failed");
        }
        adminService = new AdminService(da);
        userService = new UserService(da);
        gameService = new GameService(da);
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
            exceptions(ex,context);
        }
    }

    private void clear(Context context) {
        try {
            adminService.clear();
            context.result("{}");
        } catch (DataAccessException ex) {
            exceptions(ex,context);
        }
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
            exceptions(ex,context);
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
            exceptions(ex,context);
        }

    }

    private void listGames(Context context){
        var serializer = new Gson();
        try {
            String authToken = context.header("authorization");
            var result = gameService.listGames(authToken);
            context.contentType("application/json");
            context.result(serializer.toJson(Map.of("games", result)));
        } catch (Exception ex) {
            exceptions(ex,context);
        }
    }
    private void createGame(Context context){
        var serializer = new Gson();
        try {
            String authToken = context.header("authorization");
            String reqJson = context.body();
            var request = serializer.fromJson(reqJson, CreateGameRequest.class);
            var result = gameService.createGame(authToken, request);
            context.contentType("application/json");
            context.result(serializer.toJson(result));
        } catch (Exception ex) {
            exceptions(ex, context);
        }
    }

    private void joinGame(Context context){
        var serializer = new Gson();
        try {
            String authToken = context.header("authorization");
            String reqJson = context.body();
            var request = serializer.fromJson(reqJson, JoinGameRequest.class);
            gameService.joinGame(authToken, request);

            context.contentType("application/json");
            context.result("{}");

        } catch (Exception ex) {
            exceptions(ex,context);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptions(Exception ex, Context context) {
        var message = String.format(
                "{\"message\": \"Error: %s\" }", ex.getMessage()
        );
        if (ex instanceof DataAccessException) {
            context.status(500).result(message);
        } else if ("401 Error".equals(ex.getMessage())) {
            context.status(401).result(message);
        } else if ("403 Error".equals(ex.getMessage())) {
            context.status(403).result(message);
        } else {
            context.status(400).result(message);
        }
    }
}
