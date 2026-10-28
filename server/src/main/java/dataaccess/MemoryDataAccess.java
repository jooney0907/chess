package dataaccess;
import model. *;
import java.util.*;
import chess.*;

public class MemoryDataAccess implements DataAccess{
    private Map<String, UserData> users = new HashMap<>();
    private Map<String, AuthData> auths = new HashMap<>();
    private Map<Integer, GameData> games = new HashMap<>();
    @Override
    public void register(UserData userData, AuthData authData){
        users.put(userData.username(), userData);
        auths.put(authData.authToken(), authData);
    }
    @Override
    public UserData getUser(String userName){
        return users.get(userName);
    }
    @Override
    public void createAuth(AuthData authData){
        auths.put(authData.authToken(), authData);
    }
    @Override
    public AuthData getAuth(String authToken){
        return auths.get(authToken);
    }
    @Override
    public void deleteAuth(String authToken){
        auths.remove(authToken);
    }
    @Override
    public List<GameData> getAllGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void createGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }
    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    @Override
    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
    }

}
