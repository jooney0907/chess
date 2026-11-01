package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {
    @Test
    void clear () throws DataAccessException {
    var da = new MemoryDataAccess();
    var adminService = new AdminService(da);
    var user = new UserData("cow", "moo", "cow@byu.edu");
    da.register(user, null);
    assertNotNull(da.getUser(user.username()));
    adminService.clear();
    assertNull(da.getUser(user.username()));
    }

}
