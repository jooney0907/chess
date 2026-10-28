package service;

import chess.*;
import dataaccess.*;
import model.*;
import java.util.*;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }
    public List<GameData> listGames(String authToken) throws DataAccessException {
        var authData = dataAccess.getAuth(authToken);

        if (authData == null) {
            throw new IllegalStateException("401 Error");
        }

        return dataAccess.getAllGames();
    }
    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws DataAccessException {
        var authData = dataAccess.getAuth(authToken);

        if (authData == null) {
            throw new IllegalStateException("401 Error");
        }

        if (request == null || request.gameName() == null || request.gameName().isBlank()) {
            throw new IllegalArgumentException("400 Error");
        }
        int gameID = 1;
        while (dataAccess.getGame(gameID) != null) {
            gameID++;
        }
        GameData gameData = new GameData(gameID, null, null, request.gameName(),
                new ChessGame()
        );
        dataAccess.createGame(gameData);
        return new CreateGameResult(gameID);
    }
}