package client;

import chess.*;
import com.google.gson.Gson;
import ui.EscapeSequences;
import websocket.commands.UserConnectCommand;
import websocket.commands.UserLeaveCommand;
import websocket.commands.UserMakeMoveCommand;
import websocket.commands.UserResignCommand;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import static chess.ChessPiece.PieceType.*;

public class GameREPL extends REPL {
    public boolean running;
    private final String color;
    private final String displayColor;
    private int gameID;
    private ChessGame gameState;

    public GameREPL(ChessClient parent, int gameID, String color) {
        this.color = color.toUpperCase();
        this.gameID = gameID;
        if(color.equalsIgnoreCase("observer")){
            displayColor = "WHITE";
        }else{
            displayColor = color.toUpperCase();
        }
        parentClient = parent;
        parentClient.facade.wsFacade.updateREPL(this);
        updateBoard();
        running = true;
        connect();
        run();
    }

    private void connect(){
        var command = new UserConnectCommand(parentClient.facade.authToken, gameID);
        try {
            parentClient.facade.wsFacade.send(new Gson().toJson(command));
        } catch (Exception e) {
            System.out.println("Unable to connect. Sorry :/");
        }
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        ClientDrawer.drawBoard(gameState.getBoard(),displayColor);
        while(running){
            printNewName();
            input = scanner.nextLine();
            switch(processInput(input)[0]){
                case "leave" -> {
                    try{
                        processLeave();
                    }catch(Exception e){
                        System.out.println("Error leaving. Try again.");
                    }
                }
                case "help" -> {
                    System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN +
                            "move <start position> <end position> " +
                            EscapeSequences.RESET_TEXT_COLOR +
                            "- make a move on your turn\n" +
                            EscapeSequences.SET_TEXT_COLOR_GREEN +
                            "highlight <position> " +
                            EscapeSequences.RESET_TEXT_COLOR +
                            "- highlight all legal moves for piece at given position\n" +
                            EscapeSequences.SET_TEXT_COLOR_GREEN +
                            "redraw " +
                            EscapeSequences.RESET_TEXT_COLOR +
                            "- redraws the chess board\n" +
                            EscapeSequences.SET_TEXT_COLOR_GREEN +
                            "resign " +
                            EscapeSequences.RESET_TEXT_COLOR +
                            "- forfeit the game\n" +
                            EscapeSequences.SET_TEXT_COLOR_GREEN +
                            "leave " +
                            EscapeSequences.RESET_TEXT_COLOR +
                            "- exit the game without forfeiting\n" +
                            EscapeSequences.SET_TEXT_COLOR_GREEN +
                            "help " +
                            EscapeSequences.RESET_TEXT_COLOR +
                            "- see possible commands. You are here!");
                }
                case "move" -> {
                    processMove(processInput(input));
                }
                case "highlight" -> {
                    processHighlight(processInput(input));
                }
                case "redraw" -> ClientDrawer.drawBoard(gameState.getBoard(), displayColor);
                case "resign" -> {
                    try {
                        processResign();
                    }catch(Exception e){
                        System.out.println("Error resigning. Try again.");
                    }
                }
                case null, default -> {
                    System.out.println("Command not recognized: '" + processInput(input)[0] + "'. Please try again, or type 'help' for help.");
                }
            }
        }
    }

    public void printNewName(){
        System.out.print("[" + color + "] >>> ");
    }

    private void processMove(String[] input) {
        if(input.length != 3){
            throw new RuntimeException("Incorrect number of arguments. Try again.");
        }
        ChessPosition chessPosFrom;
        ChessPosition chessPosTo;
        try{
            chessPosFrom = processPosition(input[1]);
            if(chessPosFrom.getRow() < 1 || chessPosFrom.getRow() > 8 || chessPosFrom.getColumn() < 1 || chessPosFrom.getColumn() > 8){
                throw new RuntimeException();
            }
            chessPosTo = processPosition(input[2]);
            if(chessPosTo.getRow() < 1 || chessPosTo.getRow() > 8 || chessPosTo.getColumn() < 1 || chessPosTo.getColumn() > 8){
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid position. Try again.");
        }
        ChessPiece.PieceType promo = checkPromotion(chessPosFrom, chessPosTo);
        System.out.println("Move from " + chessPosFrom + " to " + chessPosTo);
        String authToken = parentClient.facade.authToken;
        ChessMove myMove = new ChessMove(chessPosFrom, chessPosTo, promo);
        var command = new UserMakeMoveCommand(authToken,gameID,myMove);
        try {
            if(!gameState.isPossibleMove(myMove,gameState.validMoves(chessPosFrom))){
                System.out.println("Move is invalid!");
                throw new RuntimeException("Invalid move");
            }
            parentClient.facade.wsFacade.send(new Gson().toJson(command));
        }catch(Exception e){
            System.out.println("Failed to make move. Please try again.");
        }
    }

    private ChessPiece.PieceType checkPromotion(ChessPosition start, ChessPosition end){
        if(gameState.getBoard().getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN &&
                (end.getRow() == 1 || end.getRow() == 8)){
            Scanner getPromo = new Scanner(System.in);
            System.out.println("Your pawn is about to promote! Enter N, R, B, or Q to choose a promotion piece: ");
            String promo = getPromo.nextLine();
            switch(promo){
                case "N" -> {
                    return KNIGHT;
                }
                case "B" -> {
                    return BISHOP;
                }
                case "R" -> {
                    return ROOK;
                }
                case "Q" -> {
                    return QUEEN;
                }
            }
        }
        return null;
    }

    private void processHighlight(String[] input){
        if(input.length != 2){
            throw new RuntimeException("Incorrect number of arguments. Try again.");
        }
        ChessPosition chessPos;
        try{
            chessPos = processPosition(input[1]);
            if(chessPos.getRow() < 1 || chessPos.getRow() > 8 || chessPos.getColumn() < 1 || chessPos.getColumn() > 8){
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid position. Try again.");
        }
        ClientDrawer.drawHighlightedBoard(gameState.getBoard(),displayColor,chessPos);
    }

    private void processResign() throws Exception {
        UserResignCommand command = new UserResignCommand(parentClient.facade.authToken,gameID);
        parentClient.facade.wsFacade.send(new Gson().toJson(command));
    }

    private void processLeave() throws Exception {
        UserLeaveCommand command = new UserLeaveCommand(parentClient.facade.authToken,gameID);
        parentClient.facade.wsFacade.send(new Gson().toJson(command));
        parentClient.login(parentClient.facade);
        running = false;
    }

    private ChessPosition processPosition(String position){
        char letter = position.toLowerCase().charAt(0);
        char number = position.charAt(1);

        // Subtract 60 since ASCII for 'a' is 97
        int col = letter - 96;
        int row = Integer.parseInt(""+number);

        return new ChessPosition(row, col);
    }

    public void loadGame(ChessGame newGame){
        gameState = newGame;
        ClientDrawer.drawBoard(gameState.getBoard(), displayColor);
    }

    private void updateBoard(){
        ArrayList games = (ArrayList) parentClient.facade.listGames().get("games");
        for(Object gameObject : games){
            Map game = (Map) gameObject;
            Integer id = ((Double) game.get("gameID")).intValue();
            if(id == gameID){
                String gameJson = new Gson().toJson(game.get("game"));
                gameState = new Gson().fromJson(gameJson, ChessGame.class);
            }
        }
    }
}
