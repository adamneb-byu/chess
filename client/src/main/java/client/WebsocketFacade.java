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

public class WebsocketFacade extends Endpoint {
    String serverUrl;
    private Session session;

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
                    var msg = new Gson().fromJson(message, ServerMessage.class);
                    switch(msg.getServerMessageType()){
                        case ERROR -> serverError((ServerErrorMessage) msg);
                        case LOAD_GAME -> loadGame((ServerLoadMessage) msg);
                        case NOTIFICATION -> notification((ServerNotificationMessage) msg);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void serverError(ServerErrorMessage msg){

    }

    private void loadGame(ServerLoadMessage msg){

    }

    private void notification(ServerNotificationMessage msg){

    }

    public void send(String msg) throws Exception{
        this.session.getBasicRemote().sendText(msg);
    }
}
