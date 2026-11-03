package client;

import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class PostlogREPL extends REPL {
    public boolean running;
    public PostlogREPL(ChessClient parent, ServerFacade facade){
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
                case "quit" -> {
                    running = false;
                    System.out.println("Until next time!");
                }
                case "help" -> {
                    showHelp();
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
        if(!isNumeric(input[1]) || !gameExists(Integer.parseInt(input[1]))){
            throw new RuntimeException("Invalid game ID. Try again.");
        }
        try{
            int trueID = getTrueID(Integer.parseInt(input[1]));
            parentClient.joinGame(facade, trueID,"OBSERVER");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void processJoin(String[] input) {
        if(input.length != 3){
            throw new RuntimeException("Invalid input. Try again.");
        }
        if(!isNumeric(input[1]) || !gameExists(Integer.parseInt(input[1]))){
            throw new RuntimeException("Invalid game ID. Try again.");
        }
        if(!input[2].equalsIgnoreCase("white") && !input[2].equalsIgnoreCase("black")){
            throw new RuntimeException("Invalid color. Try again.");
        }
        try {
            int trueID = getTrueID(Integer.parseInt(input[1]));
            facade.join(input[2],trueID);
            parentClient.joinGame(facade, trueID, input[2]);
        }catch(Exception e){
            throw new RuntimeException("Invalid selection. Try again.");
        }
    }

    private void processList() {
        try{
            ArrayList games = (ArrayList) facade.listGames().get("games");
            int count = 1;
            for(Object gameObject : games){
                Map game = (Map) gameObject;
                Double id = (Double) game.get("gameID");
                Integer gameID = id.intValue();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                String white = (String) game.get("whiteUsername");
                if(white == null || white.equalsIgnoreCase("null")){
                    white = "<EMPTY>";
                }
                String black = (String) game.get("blackUsername");
                if(black == null || black.equalsIgnoreCase("null")){
                    black = "<EMPTY>";
                }
                System.out.println("ID: " + count +
                        "   Name: " + game.get("gameName") +
                        "   White: " + white +
                        "   Black: " + black);
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                count++;
            }
            if(count == 1){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                System.out.println("No active games");
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
        if(gameID < 1 || gameID > games.size()){
            return false;
        }
        return true;
    }

    private int getTrueID(int clientGameID){
        int count = 1;
        ArrayList games = (ArrayList) facade.listGames().get("games");
        for(Object gameObject : games){
            Map game = (Map) gameObject;
            Integer id = ((Double) game.get("gameID")).intValue();
            if(count == clientGameID){
                return id;
            }
            count++;
        }
        return -1;
    }

    private boolean isNumeric(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
