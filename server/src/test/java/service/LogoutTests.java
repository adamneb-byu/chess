package service;

import com.google.gson.Gson;
import data.AuthData;
import data.UserData;
import handler.BadInputException;
import handler.GeneralHandler;
import handler.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogoutTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("Log out of an account")
    public void loginTest(){
        var data1 = new UserData("ralsei","ihateflowery","prince.ralsei@delta.rune");
        String data2 = handler.register(serializer.toJson(data1));
        handler.logout(serializer.fromJson(data2, AuthData.class).authToken());
        assertEquals(0, handler.listAuth().size());
    }

    @Test
    @DisplayName("Cannot log out with the wrong authToken")
    public void loginToEmpty(){
        assertThrows(NotFoundException.class,() -> {
            var data1 = new UserData("lancer","lancer","lancer@lancer.lancer");
            String data2 = handler.register(serializer.toJson(data1));
            handler.logout("lancer");
        });
    }

    @Test
    @DisplayName("Cannot log out if no auth data exists")
    public void wrongPassword(){
        assertThrows(NotFoundException.class,() -> {
            handler.logout("fitnessgram pacer test");
        });
    }
}
