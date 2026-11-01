package dataaccess;

import org.junit.jupiter.api.Test;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;


public class MemoryDataAccessTest {
    @Test
    void clear() {
        var user = new UserData("cow", "moo", "cow@byu.edu");
        var da = new MemoryDataAccess();
        da.register(user, null);
        //var user = registerRandomUser(da);
        assertNotNull(da.getUser(user.username()));
        da.clear();
        assertNull(da.getUser(user.username()));
    }
    @Test
    void getUser(){
        var user = new UserData("cow", "moo", "cow@byu.edu");
        var da = new MemoryDataAccess();
        da.register(user, null);
        var result = da.getUser(user.username());
        assertNotNull(result);
        assertEquals(user.username(), result.username());
        assertEquals(user.email(), result.email());
        assertTrue(BCrypt.checkpw(user.password(), result.password()));
    }

    @Test
    void getUserNotFound() {
        var da = new MemoryDataAccess();
        assertNull(da.getUser("unknown"));
    }
}
