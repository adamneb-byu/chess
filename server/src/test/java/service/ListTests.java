package service;

import com.google.gson.Gson;
import data.AuthData;
import data.GameData;
import data.GameDataPublic;
import data.UserData;
import handler.AlreadyTakenException;
import handler.BadInputException;
import handler.GameName;
import handler.GeneralHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ListTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("List when no games exist")
    public void listNone(){
        var user1 = new UserData("ralsei","ihateflowery","prince.ralsei@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        String result = handler.listGames(authUser1.authToken());
        String expected = serializer.toJson(Map.of("games", new int[0]));
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("List when one game exists")
    public void listOneGame(){
        var user1 = new UserData("ralsei","ihateflowery","prince.ralsei@delta.rune");
        String jsonUser1 = handler.register(serializer.toJson(user1));
        var authUser1 = serializer.fromJson(jsonUser1, AuthData.class);
        var game1 = serializer.fromJson(handler.createGame(serializer.toJson(new GameName("Castle Town")), authUser1.authToken()), GameData.class);
        String result = handler.listGames(authUser1.authToken());
        Map[] mapArr = {Map.of("gameID",game1.gameID(),"whiteUsername","","blackUsername","","gameName","Castle Town")};
        String expected = serializer.toJson(Map.of("games", mapArr));
        assertEquals(serializer.fromJson(expected, GameDataPublic.class), serializer.fromJson(result, GameDataPublic.class));
    }

    @Test
    @DisplayName("Cannot list games without a valid authToken")
    public void invalidAuthToken(){
        assertThrows(NotFoundException.class,() -> {
            handler.listGames("invalid authtoken be like");
        });
    }
}
