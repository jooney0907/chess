package service;
import java.util.UUID;
import dataaccess.*;
import model.* ;


public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{
        var user = dataAccess.getUser(registerRequest.username());
        if(registerRequest.username() == null || registerRequest.email() == null
        || registerRequest.password() == null){
            throw new IllegalArgumentException("400 Error");
        }
        if(user != null){
            throw new IllegalStateException("403 Error");
        }
        var newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        String authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken,newUser.username());
        dataAccess.register(newUser,authData);
        return new RegisterResult(newUser.username(),authToken);
    }
    public LoginRequest login(LoginRequest loginRequest) { return null;}
    public void logout(LogoutRequest logoutRequest) {}

}
