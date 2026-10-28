package dataaccess;

import model.*;
import java.util.*;
import javax.xml.crypto.Data;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void register(UserData userData, AuthData authData) throws DataAccessException;
    List<GameData> getAllGames() throws DataAccessException;
    UserData getUser(String userName) throws DataAccessException;
}
