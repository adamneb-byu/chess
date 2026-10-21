package dataaccess;

import com.google.gson.Gson;
import data.AuthData;
import data.UserData;
import handler.GeneralHandler;
import handler.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.IncorrectPasswordException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    private final UserService service = new UserService();
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
    @DisplayName("Get list of all user data")
    public void listUsers() throws DataAccessException{
        dao.createUser(new UserData("test-user-1","test-pass1","test1@email.com"));
        dao.createUser(new UserData("test-user-2","test-pass2","test2@email.com"));
        dao.createUser(new UserData("test-user-3","test-pass3","test3@email.com"));
        var list = dao.listUsers();
        assertNotEquals(0,list.size());
        assertEquals("test2@email.com",list.get(1).email());
        assertEquals("test-user-3",list.get(2).username());
    }

    @Test
    @DisplayName("Verify a password")
    public void verifyPass() throws DataAccessException{
        dao.createUser(new UserData("test-user-1", "test-pass1", "test1@email.com"));
        var goodRequest = new LoginRequest("test-user-1","test-pass1");
        var badRequest = new LoginRequest("test-user-1","mwehehehImHACKER");
        assertDoesNotThrow(() -> service.verifyPassword(goodRequest), "Verification error");
        assertThrows(IncorrectPasswordException.class, () -> service.verifyPassword(badRequest));
    }
}
