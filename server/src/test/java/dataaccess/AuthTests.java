package dataaccess;

import com.google.gson.Gson;
import data.AuthData;
import dataaccess.sql.DatabaseAuthDAO;
import handler.GeneralHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final DatabaseAuthDAO dao = new DatabaseAuthDAO();
    private final Gson serializer = new Gson();

    public AuthTests() throws DataAccessException {
    }

    @BeforeEach
    public void clear() throws DataAccessException {
        dao.clear();
    }

    @Test
    @DisplayName("Add to a database")
    public void addAuth() throws DataAccessException {
        assertDoesNotThrow(() -> dao.createAuth(new AuthData("test-token","testuser")),
                "DataAccessException!");
        assertNotNull(dao.getAuth("test-token"));
        assertEquals("testuser", dao.getAuth("test-token").username());
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
