package service;

import com.google.gson.Gson;
import data.AuthData;
import data.GameData;
import data.UserData;
import handler.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

public class JoinTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("Join a game")
    public void joinTest(){
        var user1 = new UserData("ralsei","ihateflowery","prince.ralsei@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        var game = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Castle Town")), authUser1.authToken()), GameData.class);
        var joinReq = new JoinRequest("WHITE",game.gameID());
        handler.joinGame(serializer.toJson(joinReq), authUser1.authToken());
        assertEquals("ralsei", handler.getGame(game.gameID()).whiteUsername());
    }

    @Test
    @DisplayName("Cannot join a game without a valid authToken")
    public void invalidAuthToken(){
        var user1 = new UserData("flowery","jarona","flowersblooms@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        var game = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Garden of Hopes and Dreams")), authUser1.authToken()), GameData.class);
        var joinReq = new JoinRequest("BLACK",game.gameID());
        assertThrows(UnauthorizedException.class,() -> {
            handler.joinGame(serializer.toJson(joinReq), "sustingus!");
        });
    }

    @Test
    @DisplayName("Cannot join a game with a taken color")
    public void colorTaken(){
        var user1 = new UserData("flowery","jarona","flowersblooms@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        var user2 = new UserData("pink","nyanyagirl","mewmewmagic@delta.rune");
        String jsonUser2 = handler.register(serializer.toJson(user2));
        var authUser2 = serializer.fromJson(jsonUser2, AuthData.class);
        var game = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Garden of Hopes and Dreams")), authUser1.authToken()), GameData.class);
        var joinReq = new JoinRequest("BLACK",game.gameID());
        handler.joinGame(serializer.toJson(joinReq), authUser1.authToken());
        assertThrows(AlreadyTakenException.class,() -> {
            handler.joinGame(serializer.toJson(joinReq), authUser2.authToken());
        });
    }

    @Test
    @DisplayName("Cannot join a game with bad input")
    public void badInput(){
        var user1 = new UserData("burghley", "gamer9999", "bluebirdgamez@delta.rune");
        var jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1,AuthData.class);
        var game = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Garden of Hopes and Dreams")), authUser1.authToken()), GameData.class);
        var badRequest = new JoinRequest("BLUE",game.gameID());
        assertThrows(BadInputException.class,() -> {
            handler.createGame(serializer.toJson(badRequest), authUser1.authToken());
        });
    }
}
