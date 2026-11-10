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
            var command = processCommand(ctx.message());
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
        //session.getRemote().sendString(new Gson().toJson(message));
        sessionManager.broadcast(gameID, message);
    }

    private void connect(Session session, String username, UserConnectCommand command) throws IOException {
        System.out.println("Received message! " + username + " has joined the fray!");
        sessionManager.addSession(command.getGameID(), session);
        sendMessage(session, command.getGameID(), new ServerNotificationMessage(username + " has joined the game"));
    }

    private void makeMove(Session session, String username, UserMakeMoveCommand command){
        System.out.println("Received message! " + command.getMove());
        ServerMessage msg;
        try{
            generalHandler.updateGame(command.getGameID(), command.getMove());
            msg = new ServerNotificationMessage(username + " made a move.");
            String updatedGame = new Gson().toJson(generalHandler.getGame(command.getGameID()).game());
            sendMessage(session, command.getGameID(), new ServerLoadMessage(updatedGame));
        }catch (Exception e){
            msg = new ServerErrorMessage("Server error!");
        }
        try {
            sendMessage(session, command.getGameID(), msg);
        } catch (IOException e) {
            System.out.println("Error sending move message");
        }
    }

    private void leave(Session session, String username, UserLeaveCommand command){
        System.out.println("Received message! " + username + " is out of here");
        ServerMessage msg;
        try {
            generalHandler.userLeave(username, command.getGameID());
            msg = new ServerNotificationMessage(username + " left the game");
            sessionManager.removeSession(command.getGameID(),session);
        }catch (NotFoundException e){
            System.out.println(e.getMessage());
            msg = new ServerErrorMessage("Error! Given user not found.");
        }catch (Exception e){
            System.out.println("Server error leaving game");
            msg = new ServerErrorMessage("Error! Something went wrong.");
        }
        try {
            sendMessage(session, command.getGameID(), msg);
        } catch (IOException e) {
            System.out.println("Error sending leave message");
        }
    }

    private void resign(Session session, String username, UserResignCommand command){
        System.out.println("Received message! Yeah that's right raise the white flag " + username + " you coward");
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
