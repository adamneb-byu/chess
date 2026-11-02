package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class prelogRepl extends REPL {
    public boolean running;
    public prelogRepl(ChessClient parent){
        facade = new ServerFacade("http://localhost:8080");
        parentClient = parent;
        running = true;
        run();
    }

    public prelogRepl(ChessClient parent, ServerFacade facade){
        this.facade = facade;
        parentClient = parent;
        running = true;
        run();
    }

    private void run(){
        facade.clear();
        // DELETE THE ABOVE LINE BEFORE PASSOFF
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(running){
            System.out.print("[LOGGED OUT] >>> ");
            input = scanner.nextLine();
            switch(processInput(input)[0]){
                case "register" -> {
                    try {
                        processRegister(processInput(input));
                    }catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "login" -> {
                    try {
                        processLogin(processInput(input));
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

    private void processRegister(String[] input){
        if(input.length != 4){
            throw new RuntimeException("Invalid input. Please try again.");
        }
        try{
            facade.register(input[1],input[2],input[3]);
        }catch(Exception e){
            throw new RuntimeException("Registration error. Please try again.");
        }
        System.out.println("Registration successful! Welcome, " + input[1] + "!");
        switchLogin();
    }

    private void processLogin(String[] input){
        if(input.length != 3){
            throw new RuntimeException("Invalid input. Please try again.");
        }
        try{
            facade.login(input[1],input[2]);
        }catch(Exception e){
            throw new RuntimeException("Invalid credentials. Please try again.");
        }
        System.out.println("Login successful! Welcome, " + input[1] + "!");
        switchLogin();
    }

    private void switchLogin(){
        parentClient.login(facade);
        running = false;
    }

    private void showHelp(){
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN +
                "register <username> <password> <email> " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- register a new user\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "login <username> <password> " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- log in to existing account\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "quit " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- exit the chess client\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                "help " +
                EscapeSequences.RESET_TEXT_COLOR +
                "- see possible commands. You are here!");
    }
}
