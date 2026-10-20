package dataaccess;
import model.UserData;
import java.util.*;

public class MemoryDataAccess implements DataAccess{
    private Map<String, UserData> users = new HashMap<>();

    @Override
    public void register(UserData userData, AuthData authData){
        users.put(userData.username(),);
    }

    public void getUser(String userName){
        return user.userName;
    }

    @Override
    public void clear(){
        users.clear();
    }

}
