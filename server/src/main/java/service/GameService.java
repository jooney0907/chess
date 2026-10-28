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
    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException {
        var authData = dataAccess.getAuth(authToken);
        if (authData == null) {
            throw new IllegalStateException("401 Error");
        }
        if (request == null || request.gameID() == null || request.playerColor() == null) {
            throw new IllegalArgumentException("400 Error");
        }
        var gameData = dataAccess.getGame(request.gameID());
        if (gameData == null) {
            throw new IllegalArgumentException("400 Error");
        }
        if (request.playerColor().equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new IllegalStateException("403 Error");
            }
            gameData = new GameData(gameData.gameID(), authData.username(),
                    gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (request.playerColor().equals("BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new IllegalStateException("403 Error");
            }
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    authData.username(), gameData.gameName(), gameData.game());
        } else {
            throw new IllegalArgumentException("400 Error");
        }
        dataAccess.createGame(gameData);
    }


}