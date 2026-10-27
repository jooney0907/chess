package server;

import com.google.gson.Gson;
import service.*;
import io.javalin.*;
import io.javalin.http.*;
import model.*;
import dataaccess.*;
public class Server {

    private final Javalin javalin;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        var da = new MemoryDataAccess();
        var adminService = new AdminService(da);
        var userService = new UserService(da);
        javalin.post("/user",this::register);
        javalin.delete("/db", this::clear);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game",this::joinGame);
    }

    private void register(Context context){
        var serializer = new Gson();
    }

    private void clear(Context context){

    }
    private void login(Context context){

    }
    private void logout(Context context){

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
