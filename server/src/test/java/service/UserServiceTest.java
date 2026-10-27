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
}
