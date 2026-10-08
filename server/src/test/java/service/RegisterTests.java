package service;

import com.google.gson.Gson;
import data.UserData;
import handler.AlreadyTakenException;
import handler.BadInputException;
import handler.GeneralHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.Server;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("Registering different users works as intended")
    public void registerUsers(){
        var data1 = new UserData("kris","mosseater","human123@delta.rune");
        handler.register(serializer.toJson(data1));
        var data2 = new UserData("susie","sk8board","susiezilla@delta.rune");
        handler.register(serializer.toJson(data2));
        var data3 = new UserData("ralsei","0ih2d8y2mpp","prince.ralsei@delta.rune");
        handler.register(serializer.toJson(data3));
        assertEquals(3, handler.listUsers().size());
    }

    @Test
    @DisplayName("Cannot register multiple users with the same username")
    public void alreadyTaken(){
        assertThrows(AlreadyTakenException.class,() -> {
            var data1 = new UserData("mike", "meow", "pluey@delta.rune");
            handler.register(serializer.toJson(data1));
            var data2 = new UserData("mike", "yeehaw", "woody@delta.rune");
            handler.register(serializer.toJson(data2));
        });
    }

    @Test
    @DisplayName("Cannot register with incorrectly formatted input")
    public void badInput(){
        assertThrows(BadInputException.class,() -> {
            var data1 = Map.of("usernam", "burghley", "password", "gamer9999", "email", "bluebirdgamez@delta.rune");
            handler.register(serializer.toJson(data1));
        });
    }
}
