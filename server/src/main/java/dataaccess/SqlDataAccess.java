package dataaccess;

import model.*;

import java.util.*;

public class SqlDataAccess implements DataAccess{
    public SqlDataAccess() throws DataAccessException{
        DatabaseManager.createDatabase();
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
