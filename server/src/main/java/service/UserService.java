package service;


import dataaccess.*;


public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
    public RegisterResult register(RegisterRequest registerRequest) {
//        return new RegisterResult();
        return null;

    }
    public LoginRequest login(LoginRequest loginRequest) { return null;}
    public void logout(LogoutRequest logoutRequest) {}

}
