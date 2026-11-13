package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade client;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        client = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void registerSuccess() throws Exception {
        var result = assertDoesNotThrow(() -> client.register("cow", "moo", "cow@byu.edu"));
        assertEquals("cow", result.username());
        assertNotNull(result.authToken());
    }
    @Test
    void loginSuccess() throws Exception{
        var result = assertDoesNotThrow(() -> client.login("cow", "moo"));
        assertNotNull(result.authToken());
    }

    @Test
    void clearDatabase() throws Exception{
        client.clear();
    }

    @Test
    void logoutSuccess() throws Exception{
        var user = client.register("cow", "moo", "cow@byu.edu");
        assertDoesNotThrow(() -> client.logout(user.authToken()));
    }

    @Test
    void createGameSuccess() throws Exception{
        var user = client.register("cow", "moo", "cow@byu.edu");
        int gameID = client.creategame(user.authToken(),"game1");
        assertTrue(gameID > 0);
    }
    @Test
    void listGamesSuccess() throws Exception {
        var user = client.register("cow", "moo", "cow@byu.edu");
        client.creategame(user.authToken(), "game1");
        client.creategame(user.authToken(), "game2");
        var result = client.listGames(user.authToken());
        assertEquals(2, result.games().size());
    }
    @Test
    void joinGameSuccess() throws Exception {
        var user = client.register("cow", "moo", "cow@byu.edu");
        int gameID = client.creategame(user.authToken(), "game1");
        assertDoesNotThrow(() -> client.joinGame(user.authToken(), "WHITE", gameID));
    }
}
