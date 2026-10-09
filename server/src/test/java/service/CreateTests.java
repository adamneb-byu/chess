package service;

import com.google.gson.Gson;
import data.AuthData;
import data.GameData;
import data.UserData;
import handler.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CreateTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("Create a game with a new account")
    public void createTest(){
        var user1 = new UserData("ralsei","ihateflowery","prince.ralsei@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        var game = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Castle Town")), authUser1.authToken()), GameData.class);
        assertNotEquals("","" + game.gameID());
    }

    @Test
    @DisplayName("Cannot create a game without a valid authToken")
    public void invalidAuthToken(){
        assertThrows(NotFoundException.class,() -> {
            var gamename = serializer.toJson(new GameName("Scarlet Forest"));
            var game = handler.createGame(gamename, "invalid authtoken be like");
        });
    }

    @Test
    @DisplayName("Cannot create two games with the same name")
    public void nameTaken(){
        var user1 = new UserData("queen","ooooohohohoho","cyberqueen76@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        var game1 = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Castle Town")), authUser1.authToken()), GameData.class);
        assertThrows(AlreadyTakenException.class,() -> {
            var game2 = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Castle Town")), authUser1.authToken()), GameData.class);
        });
    }

    @Test
    @DisplayName("Cannot create a game with bad input")
    public void badInput(){
        var user1 = new UserData("burghley", "gamer9999", "bluebirdgamez@delta.rune");
        var jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1,AuthData.class);
        assertThrows(BadInputException.class,() -> {
            var badRequest = Map.of("gamNam", "Smartopia");
            handler.createGame(serializer.toJson(badRequest), authUser1.authToken());
        });
    }
}
