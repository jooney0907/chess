package dataaccess;

import model.*;

public interface DataAccess {
    void clear() throws DataAccessException;
    void register(UserData userData, AuthData authData) throws DataAccessException;
    UserData getUser(String userName) throws DataAccessException;
}
