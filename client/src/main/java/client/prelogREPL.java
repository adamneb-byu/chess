package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class prelogREPL extends REPL {
    public boolean running;
    public prelogREPL(){
        facade = new ServerFacade("http://localhost:8080");
        running = true;
        run();
    }

    private void run(){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(running){
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
