package dataaccess;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlDataAccessTest {
    @BeforeEach
    public void clearDB() throws DataAccessException {
        DataAccess db = new SqlDataAccess();
        db.clear();
    }

    @Test
    public void clearTest() throws DataAccessException {
    }

    @Test
    public void createAuthTest() throws DataAccessException {
        DataAccess db = new SqlDataAccess();
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertEquals(authData, db.getAuth("zzz"));
    }
    @Test
    public void getAuthTest() {
    }
}