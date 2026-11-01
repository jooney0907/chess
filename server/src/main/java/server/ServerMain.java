package server;


import dataaccess.*;

public class ServerMain {
    public static void main(String[] args) throws DataAccessException {
        var port = 8080;
        Server server = new Server();
        server.run(port);
        SqlDataAccess da = new SqlDataAccess();
        da.clear();
        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}