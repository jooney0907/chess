package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    @Test
    void listUnauthGames(){
        var da = new MemoryDataAccess();
        var gameService = new GameService(da);
        assertThrows(IllegalStateException.class, () -> {gameService.listGames("invalidToken");});
    }
    @Test
    void createGameSuccess() throws DataAccessException {
        var dataAccess = new MemoryDataAccess();
        var gameService = new GameService(dataAccess);
        var authData = new AuthData("validToken", "josh");
        dataAccess.createAuth(authData);
        var request = new CreateGameRequest("Test Game");
        var result = gameService.createGame("validToken", request);
        assertNotNull(result);
        assertEquals(1, result.gameID());
    }
    @Test
    void listGamesSuccess() throws DataAccessException {
        var da = new MemoryDataAccess();
        var gameService = new GameService(da);
        var authData = new AuthData("validToken", "josh");
        da.createAuth(authData);
        var games = gameService.listGames("validToken");
        assertNotNull(games);
        assertEquals(0, games.size());
    }
    @Test
    void createGameUnauthorized() {
        var da = new MemoryDataAccess();
        var gameService = new GameService(da);
        var request = new CreateGameRequest("Test Game");
        assertThrows(IllegalStateException.class, () -> {gameService.createGame("invalidToken", request);});
    }
}
