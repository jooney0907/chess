package dataaccess;

import model.*;

import java.sql.*;
import java.util.*;

public class SqlDataAccess implements DataAccess{
    public SqlDataAccess() throws DataAccessException{
        DatabaseManager.createDatabase();();
    }

    void configureDatabase() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var createUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                                username VARCHAR(255) NOT NULL,
                                password VARCHAR(255) NOT NULL,
                                email VARCHAR(255) NOT NULL,
                                PRIMARY KEY (username)
                            );
                """;
            try (var createTableStatement = conn.prepareStatement(createUserTable)){
                createTableStatement.executeUpdate();
            }
            var createAuthTable = """
                CREATE TABLE IF NOT EXISTS auth (
                                authToken VARCHAR(255) NOT NULL,
                                username VARCHAR(255) NOT NULL,
                                PRIMARY KEY (authToken)
                            );
                """;
            try (var createTableStatement = conn.prepareStatement(createAuthTable)){
                createTableStatement.executeUpdate(createAuthTable);
            }
            var createGameTable = """
                CREATE TABLE IF NOT EXISTS games (
                                username VARCHAR(255) NOT NULL,
                                password VARCHAR(255) NOT NULL,
                                email VARCHAR(255) NOT NULL,
                                PRIMARY KEY (username)
                            );
                """;
            try (var createTableStatement = conn.prepareStatement(createGameTable)){
                createTableStatement.executeUpdate();
            } catch(SQLException ex){
                throw new DataAccessException("Failed");
            }
        }
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void register(UserData userData, AuthData authData) throws DataAccessException {

    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public GameData getGame(Integer gameID) {
        return null;
    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }
}
