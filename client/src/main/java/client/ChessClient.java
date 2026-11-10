package client;

import chess.ChessGame;
import chess.ChessPiece;

public class ChessClient {
    private boolean gameState;
    public REPL repl;
    public ServerFacade facade;

    private enum GameStates{
        LOGGED_IN,
        LOGGED_OUT,
        IN_GAME
    }

    public ChessClient() {
        System.out.println("♕ Welcome to the 240 Chess Client ♕");
        facade = new ServerFacade("http://localhost:8080", this);
        initializeREPL();
    }

    private void initializeREPL(){
        repl = new PrelogREPL(this);
    }

    public void login(ServerFacade facade){
        repl = new PostlogREPL(this);
    }

    public void logout(ServerFacade facade){
        repl = new PrelogREPL(this);
    }

    public void joinGame(int gameID, String color){
        repl = new GameREPL(this, gameID, color);
    }
}
