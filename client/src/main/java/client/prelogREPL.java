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
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case "login" -> {
                    processLogin(processInput(input));
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

    }

    private void processLogin(String[] input){
        if(input.length != 3){
            throw new RuntimeException("Invalid input. Please try again.");
        }

    }

    private void showHelp(){
        System.out.println("Hmmm.... I think you should " +
                EscapeSequences.SET_TEXT_COLOR_RED + " KILL YOUR SELF " +
                EscapeSequences.RESET_TEXT_COLOR + " if you can't remember the inputs");
    }
}
