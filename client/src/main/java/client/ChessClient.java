package client;

import chess.ChessGame;
import chess.ChessPiece;

public class ChessClient {
    private boolean gameState;
    private static REPL repl;

    private enum GameStates{
        LOGGED_IN,
        LOGGED_OUT,
        IN_GAME
    }

    public ChessClient() {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        initializeREPL();
    }

    private void initializeREPL(){
        repl = new prelogREPL(this);
    }

    public void login(ServerFacade facade){
        repl = new postlogREPL(this, facade);
    }

    public void logout(ServerFacade facade){
        repl = new prelogREPL(this, facade);
    }

    public void joinGame(ServerFacade facade, int gameID, String color){
        repl = new gameREPL(this, facade, gameID, color);
    }
}
