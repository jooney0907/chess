package dataaccess;
import model. *;
import java.util.*;
import chess.*;
import org.mindrot.jbcrypt.BCrypt;

public class MemoryDataAccess implements DataAccess{
    private Map<String, UserData> users = new HashMap<>();
    private Map<String, AuthData> auths = new HashMap<>();
    private Map<Integer, GameData> games = new HashMap<>();
    @Override
    public void register(UserData userData, AuthData authData){
        String hashedPassword = BCrypt.hashpw(userData.password(),BCrypt.gensalt());
        UserData hashedUser = new UserData(userData.username(), hashedPassword, userData.email());
        users.put(hashedUser.username(), hashedUser);
        if (authData != null) {
            auths.put(authData.authToken(), authData);
        }
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
    public void updateGame(GameData gameData){games.put(gameData.gameID(), gameData);}

    @Override
    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
    }

}
