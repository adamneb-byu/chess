package service;

import com.google.gson.Gson;
import data.AuthData;
import data.UserData;
import dataaccess.AuthDAO;
import handler.BadInputException;
import handler.GeneralHandler;
import handler.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("Login to an existing account")
    public void loginTest(){
        var data1 = new UserData("ralsei","ihateflowery","prince.ralsei@delta.rune");
        String data2 = handler.register(serializer.toJson(data1));
        var data3 = new LoginRequest("ralsei", "ihateflowery");
        String data4 = handler.login(serializer.toJson(data3));
        AuthData data5 = serializer.fromJson(data2, AuthData.class);
        AuthData data6 = serializer.fromJson(data4, AuthData.class);
        assertEquals(data5.username(), data6.username());
    }

    @Test
    @DisplayName("Cannot log in to an account that doesn't exist")
    public void loginToEmpty(){
        assertThrows(NotFoundException.class,() -> {
            var data1 = new LoginRequest("kris", "mosseater");
            handler.login(serializer.toJson(data1));
        });
    }

    @Test
    @DisplayName("Cannot log in to an account with the wrong password")
    public void wrongPassword(){
        var data1 = new UserData("mike", "meow", "pluey@delta.rune");
        handler.register(serializer.toJson(data1));
        assertThrows(IncorrectPasswordException.class,() -> {
            var data2 = new LoginRequest("mike", "yeehaw");
            handler.login(serializer.toJson(data2));
        });
    }

    @Test
    @DisplayName("Cannot log in to an account with the wrong input json")
    public void badInput(){
        var data1 = new UserData("burghley", "gamer9999", "bluebirdgamez@delta.rune");
        handler.register(serializer.toJson(data1));
        assertThrows(BadInputException.class,() -> {
            var data2 = Map.of("usernam", "burghley", "password", "gamer9999");
            handler.register(serializer.toJson(data2));
        });
    }
}
