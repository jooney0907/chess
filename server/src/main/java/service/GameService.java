package service;

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
}