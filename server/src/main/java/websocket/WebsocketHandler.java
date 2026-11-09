package websocket;

import com.google.gson.Gson;
import data.AuthData;
import data.UserData;
import dataaccess.DataAccessException;
import handler.GeneralHandler;
import io.javalin.websocket.WsMessageContext;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.NotFoundException;
import websocket.commands.*;
import websocket.messages.*;
import jakarta.websocket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebsocketHandler {
    private final GeneralHandler generalHandler;
    private final SessionManager sessionManager;
    public WebsocketHandler(GeneralHandler handler){
        generalHandler = handler;
        sessionManager = new SessionManager();
    }
    public void handleMessage(@NotNull WsMessageContext ctx) throws IOException {
        int gameID = -1;
        var session = ctx.session;

        try{
            var command = processCommand(ctx.message());//new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthToken());

            switch(command.getCommandType()){
                case CONNECT -> connect(session, username, (UserConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (UserMakeMoveCommand) command);
                case LEAVE -> leave(session, username, (UserLeaveCommand) command);
                case RESIGN -> resign(session, username, (UserResignCommand) command);
            }
        }catch (Exception e){
            e.printStackTrace();
            sendMessage(session, gameID, new ServerErrorMessage("Error: " + e.getMessage()));
        }
    }

    public void sendMessage(Session session, int gameID, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void connect(Session session, String username, UserConnectCommand command){

    }

    private void makeMove(Session session, String username, UserMakeMoveCommand command){
        System.out.println("Received message! " + command.getMove());
    }

    private void leave(Session session, String username, UserLeaveCommand command){

    }

    private void resign(Session session, String username, UserResignCommand command){

    }

    private String getUsername(String authToken) throws DataAccessException {
        ArrayList<AuthData> authData = generalHandler.listAuth();
        for(AuthData auth : authData){
            if(auth.authToken().equals(authToken)){
                return auth.username();
            }
        }
        throw new NotFoundException("No user found with given authToken.");
    }

    private UserGameCommand processCommand(String msg){
        HashMap<String, String> cmdMap = new Gson().fromJson(msg, HashMap.class);
        switch(cmdMap.get("commandType")){
            case "CONNECT" -> {
                return new Gson().fromJson(msg, UserConnectCommand.class);
            }
            case "MAKE_MOVE" -> {
                return new Gson().fromJson(msg, UserMakeMoveCommand.class);
            }
            case "LEAVE" -> {
                return new Gson().fromJson(msg, UserLeaveCommand.class);
            }
            case "RESIGN" -> {
                return new Gson().fromJson(msg, UserResignCommand.class);
            }
            case null, default -> throw new RuntimeException("Unrecognized command type");

        }
    }
}
