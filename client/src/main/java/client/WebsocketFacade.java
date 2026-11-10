package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class WebsocketFacade extends Endpoint {
    String serverUrl;
    private Session session;
    private GameREPL gameREPL;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // THIS PAGE INTENTIONALLY LEFT BLANK
    }

    public WebsocketFacade() {
        try{
            serverUrl = "ws://localhost:8080/ws";
            URI uri = new URI(serverUrl);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message){
                    HashMap<String, String> msg = new Gson().fromJson(message, HashMap.class);
                    switch(msg.get("serverMessageType")){
                        case "ERROR" -> serverError(new Gson().fromJson(message, ServerErrorMessage.class));
                        case "LOAD_GAME" -> loadGame(new Gson().fromJson(message, ServerLoadMessage.class));
                        case "NOTIFICATION" -> notification(new Gson().fromJson(message, ServerNotificationMessage.class));
                        case null, default -> throw new RuntimeException("Unknown message type!");
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void serverError(ServerErrorMessage msg){
        System.out.println("Received an error");
    }

    private void loadGame(ServerLoadMessage msg){
        if(gameREPL != null) {
            gameREPL.loadGame(msg.getDeserializedGame());
        }
    }

    private void notification(ServerNotificationMessage msg){
        System.out.println("Received a notification");
    }

    public void send(String msg) throws Exception{
        this.session.getBasicRemote().sendText(msg);
    }

    public void updateREPL(GameREPL repl){
        gameREPL = repl;
    }
}
