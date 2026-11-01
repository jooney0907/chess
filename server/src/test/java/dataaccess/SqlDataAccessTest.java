package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SqlDataAccessTest {
    private DataAccess db;
    @BeforeEach
    public void setup() throws DataAccessException {
        db = new SqlDataAccess();
        db.clear();
    }

    @Test
    public void clearTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertNotNull(db.getAuth("zzz"));
        db.clear();
        assertNull(db.getAuth("zzz"));
    }

    @Test
    public void createAuthTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertEquals(authData, db.getAuth("zzz"));
    }
    @Test
    public void getAuthTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        AuthData result = db.getAuth("zzz");
        assertEquals("zzz", result.authToken());
        assertEquals("cow", result.username());
    }
    @Test
    public void getAuthNotFound() throws DataAccessException{
        assertNull(db.getAuth("zzz"));
    }
    @Test
    public void deleteAuthTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertNotNull(db.getAuth("zzz"));
        db.deleteAuth("zzz");
        assertNull(db.getAuth("zzz"));
    }
    @Test
    public void registerTest() throws DataAccessException{
        var user = new UserData("cow", "moo", "cow@byu.edu");
        var authData = new AuthData("zzz", "cow");
        db.register(user,authData);
        assertNotNull(db.getUser("cow"));
        assertEquals(authData, db.getAuth("zzz"));
        assertTrue(BCrypt.checkpw("moo", db.getUser("cow").password()));
    }
    @Test
    public void getUserTest() throws DataAccessException{
        var user = new UserData("cow", "moo", "cow@byu.edu");
        var authData = new AuthData("zzz", "cow");
        db.register(user, authData);
        UserData result = db.getUser("cow");
        assertEquals("cow", result.username());
        assertEquals("cow@byu.edu", result.email());
        assertTrue(BCrypt.checkpw("moo", db.getUser("cow").password()));
    }
    @Test
    public void getUserNotFound() throws DataAccessException{
        assertNull(db.getUser("cow"));
    }

    @Test
    public void getAllGamesTest() throws DataAccessException{
        var game1 = new GameData(1, null, null, "game", new ChessGame());
        db.createGame(game1);
        var game2 = new GameData(2, null, null, "game2", new ChessGame());
        db.createGame(game2);
        var game3 = new GameData(3, null, null, "game3", new ChessGame());
        db.createGame(game3);
        var game4 = new GameData(4, null, null, "game4", new ChessGame());
        db.createGame(game4);
        List<GameData> listOfGames = new ArrayList<>();
        listOfGames.add(game1);
        listOfGames.add(game2);
        listOfGames.add(game3);
        listOfGames.add(game4);
        var gameList = db.getAllGames();
        assertEquals(listOfGames, gameList);
    }
    @Test
    public void getAllGamesEmptyTest() throws DataAccessException {
        assertTrue(db.getAllGames().isEmpty());
    }
    @Test
    public void getGameTest() throws DataAccessException{
        GameData game = new GameData(1, null, null, "Test Game", new ChessGame());
        db.createGame(game);
        assertEquals(game, db.getGame(game.gameID()));
    }

    @Test
    void getGameBadTest() throws DataAccessException {
        GameData game = new GameData(1, null, null, "game6", new ChessGame());
        db.createGame(game);
        assertNull(db.getGame(100));
    }
    @Test
    public void createGameTest() throws DataAccessException{
        var game1 = new GameData(1, null, null, "Test Game", new ChessGame());
        var game2 = new GameData(2, null, null, "Test Game", new ChessGame());
        db.createGame(game1);
        db.createGame(game2);
        assertEquals(game1, db.getGame(1));
        assertEquals(game2, db.getGame(2));
    }
}