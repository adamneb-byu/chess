package websocket;

import chess.ChessGame;
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
//import jakarta.websocket.*;

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
            try {
                String username = getUsername(command.getAuthToken());

                switch (command.getCommandType()) {
                    case CONNECT -> connect(session, username, (UserConnectCommand) command);
                    case MAKE_MOVE -> makeMove(session, username, (UserMakeMoveCommand) command);
                    case LEAVE -> leave(session, username, (UserLeaveCommand) command);
                    case RESIGN -> resign(session, username, (UserResignCommand) command);
                }
            }catch(NotFoundException e){
                var msg = new ServerErrorMessage("Error: Invalid authToken");
                session.getRemote().sendString(new Gson().toJson(msg));
            }
        }catch (Exception e){
            sendMessage(null, gameID, new ServerErrorMessage("Error: " + e.getMessage()));
        }
    }

    public void sendMessage(Session session, int gameID, ServerMessage message) throws IOException {
        sessionManager.broadcast(gameID, message, session);
    }

    private void connect(Session session, String username, UserConnectCommand command) throws IOException, DataAccessException {
        System.out.println("Received message! " + username + " has joined the fray!");
        sessionManager.addSession(command.getGameID(), session);
        try{
            if(generalHandler.getGame(command.getGameID()) == null){
                throw new NotFoundException("Game ID not found");
            }
            var msg = new ServerLoadMessage(new Gson().toJson(generalHandler.getGame(command.getGameID())));
            session.getRemote().sendString(new Gson().toJson(msg));
            sendMessage(session, command.getGameID(), new ServerNotificationMessage(username + " has joined the game"));
        } catch (Exception e) {
            var msg = new ServerErrorMessage("Error: Invalid game ID");
            session.getRemote().sendString(new Gson().toJson(msg));
        }

    }

    private void makeMove(Session session, String username, UserMakeMoveCommand command){
        System.out.println("Received message! " + command.getMove());
        ServerMessage msg;
        try{
            if(generalHandler.getGame(command.getGameID()).game().gameOver){
                throw new RuntimeException("The game hath been overed!");
            }
            var teamTurn = generalHandler.getGame(command.getGameID()).game().getTeamTurn();
            var gameData = generalHandler.getGame(command.getGameID());
            if(teamTurn.equals(ChessGame.TeamColor.WHITE) && !username.equals(gameData.whiteUsername())){
                throw new RuntimeException("It's not your turn!");
            }
            if(teamTurn.equals(ChessGame.TeamColor.BLACK) && !username.equals(gameData.blackUsername())){
                throw new RuntimeException("It's not your turn!");
            }
            generalHandler.updateGame(command.getGameID(), command.getMove());
            msg = new ServerNotificationMessage(username + " made a move.");
            var game = generalHandler.getGame(command.getGameID()).game();
            String updatedGame = new Gson().toJson(game);
            sendMessage(null, command.getGameID(), new ServerLoadMessage(updatedGame));
            ServerMessage endgameMsg;
            if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
                endgameMsg = new ServerNotificationMessage("White is in checkmate! Black wins!");
            }else if(game.isInCheckmate(ChessGame.TeamColor.BLACK)){
                endgameMsg = new ServerNotificationMessage("Black is in checkmate! White wins!");
            }else if(game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)){
                endgameMsg = new ServerNotificationMessage("Stalemate! Nobody wins!");
            }else{
                endgameMsg = null;
            }
            if(endgameMsg != null){
                sendMessage(null, command.getGameID(), endgameMsg);
            }
        }catch (Exception e){
            msg = new ServerErrorMessage("Server error!");
        }
        try {
            if(msg.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
                session.getRemote().sendString(new Gson().toJson(msg));
            }else{
                sendMessage(session, command.getGameID(), msg);
            }
        } catch (IOException e) {
            System.out.println("Error sending move message");
        }
    }

    private void leave(Session session, String username, UserLeaveCommand command) throws IOException, DataAccessException {
        System.out.println("Received message! " + username + " is out of here");
        ServerMessage msg;
        var gameData = generalHandler.getGame(command.getGameID());
        if(!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())){
            msg = new ServerNotificationMessage("Observer " + username + " left the game");
            sendMessage(session, command.getGameID(), msg);
        }else {
            try {
                generalHandler.userLeave(username, command.getGameID());
                msg = new ServerNotificationMessage(username + " left the game");
                sessionManager.removeSession(command.getGameID(), session);
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
                msg = new ServerErrorMessage("Error! Given user not found.");
            } catch (Exception e) {
                System.out.println("Server error leaving game");
                msg = new ServerErrorMessage("Error! Something went wrong.");
            }
            try {
                sendMessage(session, command.getGameID(), msg);
            } catch (IOException e) {
                System.out.println("Error sending leave message");
            }
        }
    }

    private void resign(Session session, String username, UserResignCommand command) throws DataAccessException, IOException {
        System.out.println("Received message! Yeah that's right raise the white flag " + username + " you coward");
        var gameData = generalHandler.getGame(command.getGameID());
        var game = gameData.game();
        var alreadyOver = game.gameOver;
        game.gameOver = true;
        generalHandler.endGame(command.getGameID());
        ServerMessage message;
        if(alreadyOver){
            message = new ServerErrorMessage("The game is already over!");
            session.getRemote().sendString(new Gson().toJson(message));
        }else if(!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())){
            message = new ServerErrorMessage("Observers can't resign!");
            session.getRemote().sendString(new Gson().toJson(message));
        }else {
            message = new ServerNotificationMessage(username + " has resigned. Game over!");
            sendMessage(null, command.getGameID(), message);
        }
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
