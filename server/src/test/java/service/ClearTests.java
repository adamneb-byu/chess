package service;

import com.google.gson.Gson;
import data.UserData;
import handler.GeneralHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearTests {
    private final GeneralHandler handler = new GeneralHandler();
    private final Gson serializer = new Gson();

    @Test
    @DisplayName("Clearing an empty database does nothing")
    public void clearEmpty(){
        assertEquals(0, handler.listUsers().size());
        handler.clear();
        assertEquals(0, handler.listUsers().size());
    }

    @Test
    @DisplayName("Clearing data from user database")
    public void clearData(){
        var data1 = new UserData("kris","mosseater","human123@delta.rune");
        handler.register(serializer.toJson(data1));
        var data2 = new UserData("susie","sk8board","susiezilla@delta.rune");
        handler.register(serializer.toJson(data2));
        var data3 = new UserData("ralsei","0ih2d8y2mpp","prince.ralsei@delta.rune");
        handler.register(serializer.toJson(data3));
        assertEquals(3, handler.listUsers().size());
        handler.clear();
        assertEquals(0, handler.listUsers().size());
    }
}
