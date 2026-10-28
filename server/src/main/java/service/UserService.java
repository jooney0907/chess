package service;
import java.util.*;
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
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        var user = dataAccess.getUser(loginRequest.username());
        if(loginRequest.username()==null || loginRequest.password() == null){
            throw new IllegalArgumentException("400 Error");
        }
        if(user==null){
            throw new IllegalStateException("401 Error");
        }
        if(!user.password().equals(loginRequest.password())){
            throw new IllegalStateException("401 Error");
        }

        String authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken,user.username());
        dataAccess.createAuth(authData);
        return new LoginResult(user.username(), authToken);}

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        var authData = dataAccess.getAuth(logoutRequest.authToken());
        if(authData==null){
            throw new IllegalStateException("401 Error");
        }
        dataAccess.deleteAuth(authData.authToken());
        }

    public List<GameData> listGames(String authToken) throws DataAccessException{
        var authData = dataAccess.getAuth(authToken);

        if(authData==null){
            throw new IllegalStateException("401 Error");
        }
        return dataAccess.getAllGames();
    }

}
