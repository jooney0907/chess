package dataaccess;
import model. *;
import java.util.*;

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
    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
    }

}
