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
    }
    @Override
    public UserData getUser(String userName){
        return users.get(userName);
    }

    @Override
    public void clear(){
        users.clear();
    }

}
