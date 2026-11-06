package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class ServerLoadMessage extends ServerMessage{
    private String game;
    public ServerLoadMessage(String game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String getGame() {
        return game;
    }

    public ChessGame getDeserializedGame(){
        return new Gson().fromJson(game, ChessGame.class);
    }
}
