package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class GameREPL extends REPL {
    public boolean running;
    private final String color;
    private final String displayColor;
    private ChessBoard board;
    private int gameID;

    public GameREPL(ChessClient parent, int gameID, String color){
        this.color = color.toUpperCase();
        this.gameID = gameID;
        if(color.equalsIgnoreCase("observer")){
            displayColor = "WHITE";
        }else{
            displayColor = color.toUpperCase();
        }
        parentClient = parent;
        updateBoard();
        running = true;
        run();
    }

    private void run(){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(running){
            ClientDrawer.drawBoard(board,displayColor);
            System.out.print("[" + color + "] >>> ");
            input = scanner.nextLine();
            switch(processInput(input)[0]){
                case "leave" -> {
                    parentClient.login(parentClient.facade);
                    running = false;
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
                case "redraw" -> ClientDrawer.drawBoard(board, displayColor);
                case "resign" -> {
                    processResign();
                }
                case null, default -> {
                    System.out.println("Command not recognized: '" + processInput(input)[0] + "'. Please try again, or type 'help' for help.");
                }
            }
        }
    }

    private void processMove(String[] input){

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
        ClientDrawer.drawHighlightedBoard(board,displayColor,chessPos);
    }

    private void processResign(){

    }

    private ChessPosition processPosition(String position){
        char letter = position.toLowerCase().charAt(0);
        char number = position.charAt(1);

        // Subtract 60 since ASCII for 'a' is 61
        int col = letter - 60;
        int row = Integer.parseInt(""+number);

        return new ChessPosition(row, col);
    }

    private void updateBoard(){
        ArrayList games = (ArrayList) parentClient.facade.listGames().get("games");
        for(Object gameObject : games){
            Map game = (Map) gameObject;
            Integer id = ((Double) game.get("gameID")).intValue();
            if(id == gameID){
                String gameJson = new Gson().toJson(game.get("game"));
                ChessGame chessGame = new Gson().fromJson(gameJson, ChessGame.class);
                board = chessGame.getBoard();
            }
        }
    }
}
