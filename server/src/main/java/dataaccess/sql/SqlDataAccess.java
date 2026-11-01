package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

public class SqlDataAccess implements DataAccess {
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
            throw new DataAccessException("Failed");
        }
    }


    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed");
        }

    }

    @Override
    public void register(UserData userData, AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
            }
            createAuth(authData);
        } catch (SQLException e) {
            throw new DataAccessException("Failed");
        }
    }
    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        var statement = "SELECT * from games";
        Gson gson = new Gson();
        List<GameData> gameList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                var rs = preparedStatement.executeQuery();
                while(rs.next()){
                    ChessGame chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);;
                    GameData game = new GameData(rs.getInt("gameID"),rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),rs.getString("gameName"),
                            chessGame);
                    gameList.add(game);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed");
        }
        return gameList;
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game)" +
                " VALUES (?, ?, ?, ?, ?)";
        Gson gson = new Gson();
        String game = gson.toJson(gameData.game());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameData.gameID());
                preparedStatement.setString(2, gameData.whiteUsername());
                preparedStatement.setString(3, gameData.blackUsername());
                preparedStatement.setString(4, gameData.gameName());
                preparedStatement.setString(5, game);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE gameID = ?";
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                GameData game = null;
                if (rs.next()) {
                    ChessGame chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                    game = new GameData(rs.getInt("gameID"),rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),rs.getString("gameName"),
                            chessGame);
                }
                return game;
            }
        }
        catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Failed");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                UserData user = null;
                if (rs.next()) {
                    user = new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
                return user;
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed");
        }
    }
    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ? ";
        Gson gson = new Gson();
        String gameJson = gson.toJson(gameData.game());
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, gameData.whiteUsername());
            preparedStatement.setString(2, gameData.blackUsername());
            preparedStatement.setString(3, gameData.gameName());
            preparedStatement.setString(4, gameJson);
            preparedStatement.setInt(5, gameData.gameID());
            preparedStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Failed to update game: " + e.getMessage());
        }
    }
}
