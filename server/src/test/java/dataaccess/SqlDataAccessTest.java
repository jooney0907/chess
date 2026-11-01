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
        DataAccess db = new SqlDataAccess();
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertNotNull(db.getAuth("zzz"));
        db.clear();
        assertNull(db.getAuth("zzz"));
    }

    @Test
    public void createAuthTest() throws DataAccessException {
        DataAccess db = new SqlDataAccess();
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertEquals(authData, db.getAuth("zzz"));
    }
    @Test
    public void getAuthTest() throws DataAccessException {
        DataAccess db = new SqlDataAccess();
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        AuthData result = db.getAuth("zzz");
        assertEquals("zzz", result.authToken());
        assertEquals("cow", result.username());
    }
    @Test
    public void getAuthNotFound() throws DataAccessException{
        DataAccess db = new SqlDataAccess();
        assertNull(db.getAuth("zzz"));
    }
}