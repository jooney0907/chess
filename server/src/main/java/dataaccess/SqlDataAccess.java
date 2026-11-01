package dataaccess;

import model.*;

import java.sql.*;
import java.util.*;

public class SqlDataAccess implements DataAccess{
    public SqlDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        configureDatabase();
    }

    void configureDatabase() throws DataAccessException {
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
                createTableStatement.executeUpdate();
            }
            var createGameTable = """
                CREATE TABLE IF NOT EXISTS games (
                                gameID INT NOT NULL, 
                                whiteUsername VARCHAR(255),
                                blackUsername VARCHAR(255),
                                gameName VARCHAR(255) NOT NULL,
                                game TEXT NOT NULL,
                                PRIMARY KEY (gameID)
                            );
                """;
            try (var createTableStatement = conn.prepareStatement(createGameTable)){
                createTableStatement.executeUpdate();
            }
            }
        catch(SQLException ex){
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var clearAuth = "DELETE FROM auth";
            try(var authStatement = conn.prepareStatement(clearAuth)){
                authStatement.executeUpdate();
            }
            var clearGames = "DELETE FROM games";
            try(var gameStatement = conn.prepareStatement(clearGames)){
                gameStatement.executeUpdate();
            }
            var clearUser = "DELETE FROM users";
            try(var userStatement = conn.prepareStatement(clearUser)){
                userStatement.executeUpdate();
            }
        } catch(SQLException ex){
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                AuthData auth = null;
                if (rs.next()) {
                    auth = new AuthData(rs.getString("authToken"), rs.getString("username"));
                }
                return auth;
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
