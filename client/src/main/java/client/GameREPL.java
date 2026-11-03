package client;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class GameREPL extends REPL {
    public boolean running;
    private final String color;
    private final String displayColor;
    private ChessBoard board;
    private int gameID;

    public GameREPL(ChessClient parent, ServerFacade facade, int gameID, String color){
        this.color = color.toUpperCase();
        this.gameID = gameID;
        if(color.equalsIgnoreCase("observer")){
            displayColor = "WHITE";
        }else{
            displayColor = color.toUpperCase();
        }
        parentClient = parent;
        this.facade = facade;
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
                    parentClient.login(facade);
                    running = false;
                }
                case null, default -> {
                    System.out.println("Command not recognized: '" + processInput(input)[0] + "'. Please try again, or type 'help' for help.");
                }
            }
        }
    }

    private void updateBoard(){
        ArrayList games = (ArrayList) facade.listGames().get("games");
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
