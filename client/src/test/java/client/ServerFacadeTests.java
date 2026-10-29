package client;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handler.GeneralHandler;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static GeneralHandler handler;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        handler = new GeneralHandler();
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearBeforeAll(){
        facade.clear();
    }

    @Test
    @DisplayName("Register")
    public void registerTest() throws DataAccessException {
        assertEquals(0,handler.listUsers().size());
        facade.register("TestUser","mypass","user@email.com");
        assertEquals(1,handler.listUsers().size());
        assertNotEquals("",facade.authToken);
    }

    @Test
    @DisplayName("Logout")
    public void logoutTest() throws DataAccessException {
        facade.register("TestUser","mypass","user@email.com");
        facade.logout();
        assertEquals("",facade.authToken);
        assertEquals(1,handler.listUsers().size());
    }

    @Test
    @DisplayName("Login")
    public void loginTest() throws DataAccessException {
        facade.register("TestUser","mypass","user@email.com");
        facade.logout();
        assertEquals("",facade.authToken);
        facade.login("TestUser","mypass");
        assertNotEquals("",facade.authToken);
    }

    @Test
    @DisplayName("Create game")
    public void createTest() throws DataAccessException {
        facade.register("TestUser","mypass","user@email.com");
        facade.createGame("Test Game");
        var jsonList = handler.listGames(facade.authToken);
        var mapList = new Gson().fromJson(jsonList, Map.class);
        assertEquals(1,mapList.size());
    }

    @Test
    @DisplayName("List games")
    public void listTest() throws DataAccessException {
        facade.register("TestUser","mypass","user@email.com");
        facade.createGame("Test Game");
        var list = facade.listGames();
        assertEquals(1,list.size());
        facade.createGame("Test Game 2");
        list = facade.listGames();
        assertEquals(2,list.size());
        facade.createGame("Test Game 3");
        list = facade.listGames();
        assertEquals(3,list.size());
        facade.clear();
        list = facade.listGames();
        assertEquals(0,list.size());
    }

    @Test
    @DisplayName("Join game")
    public void joinTest() throws DataAccessException{
        facade.register("TestUser","mypass","user@email.com");
        int id = facade.createGame("Test Game");
        facade.join("white",id);
        var list = facade.listGames();
    }

}
