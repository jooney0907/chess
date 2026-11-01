package dataaccess;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlDataAccessTest {
    private DataAccess db;
    @BeforeEach
    public void setup() throws DataAccessException {
        db = new SqlDataAccess();
        db.clear();
    }

    @Test
    public void clearTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertNotNull(db.getAuth("zzz"));
        db.clear();
        assertNull(db.getAuth("zzz"));
    }

    @Test
    public void createAuthTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        assertEquals(authData, db.getAuth("zzz"));
    }
    @Test
    public void getAuthTest() throws DataAccessException {
        var authData = new AuthData("zzz", "cow");
        db.createAuth(authData);
        AuthData result = db.getAuth("zzz");
        assertEquals("zzz", result.authToken());
        assertEquals("cow", result.username());
    }
    @Test
    public void getAuthNotFound() throws DataAccessException{
        assertNull(db.getAuth("zzz"));
    }
}