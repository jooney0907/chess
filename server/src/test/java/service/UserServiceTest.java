package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @Test
    void registerSuccess() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", "moo", "cow@byu.edu");
        var result = userService.register(request);
        assertEquals(request.username(),result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void registerDupUsername() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", "moo", "cow@byu.edu");
        userService.register(request);
        var request2 = new RegisterRequest("cow", "woof", "dog@byu.edu");
        assertThrows(IllegalStateException.class, () -> userService.register(request2));

    }

    @Test
    void registerMissingUsername() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest(null, "moo", "cow@byu.edu");
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }
    @Test
    void registerMissingPassword() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", null, "cow@byu.edu");
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }
    @Test
    void registerMissingEmail() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", "moo", null);
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }

    @Test
    void wrongPassword() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", "moo", "cow@byu.edu");
        userService.register(request);
        var loginRequest = new LoginRequest(request.username(), "wrong");
        assertThrows(IllegalStateException.class, () -> userService.login(loginRequest));
    }

    @Test
    void unknownUsername() throws DataAccessException{
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow","moo","cow@byu.edu");
        userService.register(request);
        var loginRequest = new LoginRequest("dog", "woof");
        assertThrows(IllegalStateException.class, () -> userService.login(loginRequest));
    }

    @Test
    void missingPassword() throws DataAccessException{
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow","moo","cow@byu.edu");
        userService.register(request);
        var loginRequest = new LoginRequest("cow", null);
        assertThrows(IllegalArgumentException.class, () -> userService.login(loginRequest));
    }

    @Test
    void missingUsername() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow","moo","cow@byu.edu");
        userService.register(request);
        var loginRequest = new LoginRequest(null, "moo");
        assertThrows(IllegalArgumentException.class, () -> userService.login(loginRequest));
    }

    @Test
    void loginWorks() throws DataAccessException {
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", "moo", "cow@byu.edu");
        userService.register(request);
        var loginRequest= new LoginRequest(request.username(), request.password());
        var result = userService.login(loginRequest);
        assertEquals(loginRequest.username(),result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void logoutWorks() throws DataAccessException{
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var request = new RegisterRequest("cow", "moo", "cow@byu.edu");
        var result = userService.register(request);
        String authToken = result.authToken();
        var logoutRequest = new LogoutRequest(authToken);
        userService.logout(logoutRequest);
        assertNull(da.getAuth(authToken));
    }

    @Test
    void invalidToken() throws DataAccessException{
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var logoutRequest = new LogoutRequest("invalid");
        assertThrows(IllegalStateException.class, () -> userService.logout(logoutRequest));
    }

}
