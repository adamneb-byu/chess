package dataaccess;

import com.google.gson.Gson;
import data.AuthData;
import handler.AlreadyTakenException;
import handler.BadInputException;
import handler.GeneralHandler;
import handler.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final DatabaseGameDAO dao = new DatabaseGameDAO();
    //private final MemoryGameDAO dao = new MemoryGameDAO();
    private final Gson serializer = new Gson();

    public GameTests() throws DataAccessException {
    }

    @BeforeEach
    public void clear() throws DataAccessException {
        dao.clear();
    }

    @Test
    @DisplayName("Add to a database")
    public void addGame() throws DataAccessException {
        assertDoesNotThrow(() -> dao.createGame("TestGame1"),
                "DataAccessException!");
        int testID = dao.createGame("TestGame2");
        assertNotNull(dao.getGame(testID));
        assertEquals("TestGame2", dao.getGame(testID).gameName());
    }

    @Test
    @DisplayName("Get list of all GameData")
    public void listGames() throws DataAccessException{
        dao.createGame("TestGame1");
        dao.createGame("TestGame2");
        int testID = dao.createGame("TestGame3");
        var list = dao.listGames();
        assertEquals(3, list.size());
        assertEquals("TestGame2",list.get(1).gameName());
        assertEquals(testID,list.get(2).gameID());
    }

    @Test
    @DisplayName("Add users to game")
    public void addUsers() throws DataAccessException{
        int testID = dao.createGame("TestGame1");
        var whiteReq = new JoinRequest("white", testID);
        var blackReq = new JoinRequest("black", testID);
        var invalidReq = new JoinRequest("gray", testID);
        dao.addUser(whiteReq,"test-user-w");
        dao.addUser(blackReq,"test-user-b");
        assertEquals("test-user-w",dao.getGame(testID).whiteUsername());
        assertEquals("test-user-b",dao.getGame(testID).blackUsername());
        assertThrows(BadInputException.class, () -> dao.addUser(invalidReq, "test-user-g"));
        assertThrows(AlreadyTakenException.class, () -> dao.addUser(whiteReq, "test-user-w2"));
    }

    @Test
    @DisplayName("Checking if game exists")
    public void gameExists() throws DataAccessException{
        dao.createGame("TestGame1");
        assertTrue(dao.gameExists("TestGame1"));
        assertFalse(dao.gameExists("TestGame2"));
    }

    @Test
    @DisplayName("Get list of games (hidden data included)")
    public void trueGameList() throws DataAccessException {
        dao.createGame("Test Game 1");
        dao.createGame("Test Game 2");
        var list = dao.getGameList();
        assertEquals(2, list.length);
    }
}
