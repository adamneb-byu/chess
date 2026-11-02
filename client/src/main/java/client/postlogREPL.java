package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class postlogREPL extends REPL {
    public boolean running;
    public postlogREPL(ChessClient parent, ServerFacade facade){
        parentClient = parent;
        this.facade = facade;
        running = true;
        run();
    }

    private void run(){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(running){
            System.out.print("[LOGGED IN] >>> ");
            input = scanner.nextLine();
            switch(processInput(input)[0]){
                case "create" -> {
                    try {
                        processCreate(processInput(input));
                    }catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "list" -> {
                    try {
                        processList();
                    }catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "join" -> {
                    try{
                        processJoin(processInput(input));
                    }catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "observe" -> {
                    try{
                        processObserve(processInput(input));
                    }catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "logout" -> {
                    try{
                        processLogout();
                    }catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "help" -> {
                    showHelp();
                }
                case "quit" -> {
                    running = false;
                    System.out.println("Until next time!");
                }
                case null, default -> {
                    System.out.println("Command not recognized: '" + processInput(input)[0] + "'. Please try again, or type 'help' for help.");
                }
            }
        }
    }

    private void processLogout() {
        try{
            facade.logout();
        }catch(Exception e){
            throw new RuntimeException("Logout error. Please try again.");
        }
        System.out.println("Logout successful!");
        switchLogout();
    }

    private void processObserve(String[] input) {
        if(input.length != 2){
            throw new RuntimeException("Invalid input. Try again.");
        }
        if(!gameExists(Integer.parseInt(input[1]))){
            throw new RuntimeException("Invalid game ID. Try again.");
        }
        try{
            parentClient.joinGame(facade, Integer.parseInt(input[1]),"OBSERVER");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void processJoin(String[] input) {
        if(input.length != 3){
            throw new RuntimeException("Invalid input. Try again.");
        }
        if(!gameExists(Integer.parseInt(input[1]))){
            throw new RuntimeException("Invalid game ID. Try again.");
        }
        if(!input[2].equalsIgnoreCase("white") && !input[2].equalsIgnoreCase("black")){
            throw new RuntimeException("Invalid color. Try again.");
        }
        try {
            parentClient.joinGame(facade, Integer.parseInt(input[1]), input[2]);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void processList() {
        try{
            ArrayList games = (ArrayList) facade.listGames().get("games");
            for(Object gameObject : games){
                Map game = (Map) gameObject;
                Double id = (Double) game.get("gameID");
                Integer gameID = id.intValue();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                System.out.println("ID: " + gameID + "   Name: " + game.get("gameName"));
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        }catch(Exception e){
            throw new RuntimeException("List error. Please try again.");
        }
    }

    private void processCreate(String[] input) {
        if(input.length != 2){
            throw new RuntimeException("Invalid input. Please try again.");
        }
        try{
            facade.createGame(input[1]);
        }catch(Exception e){
            throw new RuntimeException("Creation error. Please try again.");
        }
        System.out.println("Successfully created game with name " + input[1] + "!");
    }

    private void showHelp() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN +
                "create <name> " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- create a new chess game\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "list " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- show all existing games\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "join <ID> <white|black> " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- join a chess game\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "observe <ID> " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- observe a chess game\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "logout " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- don't feel like I need to explain this one\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "quit " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- see the above description\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "help " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- see possible commands. You are here!");
    }

    private void switchLogout(){
        parentClient.logout(facade);
        running = false;
    }

    private boolean gameExists(int gameID){
        ArrayList games = (ArrayList) facade.listGames().get("games");
        for(Object gameObject : games){
            Map game = (Map) gameObject;
            Integer id = ((Double) game.get("gameID")).intValue();
            if(id == gameID){
                return true;
            }
        }
        return false;
    }
}
