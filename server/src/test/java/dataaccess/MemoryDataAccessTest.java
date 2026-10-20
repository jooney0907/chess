package dataaccess;

import org.junit.jupiter.api.Test;
import model.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class MemoryDataAccessTest {
    @Test
    void clear() {
        var user = new UserData("cow", "cow@byu.edu", "moo");
        var da = new MemoryDataAccess();
        da.register(user, null);
        //var user = registerRandomUser(da);
        assertEquals(user,da.getUser(user.username()));
        da.clear();
        assertNull(da.getUser(user.username()));
    }

    void getUser(){
        var user = new UserData("cow", "cow@byu.edu", "moo");
        var da = new MemoryDataAccess();
        da.register(user, null);
        assertEquals(user, da.getUser(user.username()));
    }

}
