package dataaccess;
import model. *;
import java.util.*;

public class MemoryDataAccess implements DataAccess{
    private Map<String, UserData> users = new HashMap<>();

    @Override
    public void register(UserData userData, AuthData authData){
        users.put(userData.username(), userData);
    }

    public UserData getUser(String userName){
        return users.get(userName);
    }

    @Override
    public void clear(){
        users.clear();
    }

}
