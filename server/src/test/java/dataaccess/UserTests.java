package dataaccess;

import com.google.gson.Gson;
import data.AuthData;
import data.UserData;
import handler.GeneralHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final DatabaseUserDAO dao = new DatabaseUserDAO();
    private final Gson serializer = new Gson();

    public UserTests() throws DataAccessException {
    }

    @BeforeEach
    public void clear() throws DataAccessException {
        dao.clear();
    }

    @Test
    @DisplayName("Add to a database")
    public void addUser() throws DataAccessException {
        assertDoesNotThrow(() -> dao.createUser(new UserData("test-user-1","test-pass","test@email.com")),
                "DataAccessException!");
        assertNotNull(dao.getUser("test-user-1"));
        assertEquals("test-user-1", dao.getUser("test-user-1").username());
    }

    @Test
    @DisplayName("Get list of all AuthData")
    public void listAuth() throws DataAccessException{
        dao.createAuth(new AuthData("test-token-1","test-user-1"));
        dao.createAuth(new AuthData("test-token-2","test-user-2"));
        dao.createAuth(new AuthData("test-token-3","test-user-3"));
        var list = dao.listAuth();
        assertNotNull(list);
        assertEquals("test-token-2",list.get(1).authToken());
        assertEquals("test-user-3",list.get(2).username());
    }

    @Test
    @DisplayName("Delete auth data")
    public void deleteAuth() throws DataAccessException {
        dao.createAuth(new AuthData("test-token-1","delete-me-pls"));
        assertEquals(1,dao.listAuth().size());
        dao.deleteAuth("test-token-1");
        assertEquals(0,dao.listAuth().size());
    }

    @Test
    @DisplayName("Verify AuthToken")
    public void verifyAuth() throws DataAccessException{
        dao.createAuth(new AuthData("test-token-1","verify-me-pls"));
        assertTrue(dao.verifyAuth("test-token-1"));
        assertFalse(dao.verifyAuth("test-token-2"));
    }
}
