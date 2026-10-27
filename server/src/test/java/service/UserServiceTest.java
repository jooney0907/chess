package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;
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
        assertThrows(IllegalArgumentException.class,() -> userService.register(request));

    }
}
