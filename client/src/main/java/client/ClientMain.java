package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    private boolean gameState;
    private static REPL repl;

    private enum GameStates{
        LOGGED_IN,
        LOGGED_OUT,
        IN_GAME
    }

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var board = new ChessBoard();
        board.resetBoard();
        ClientDrawer.drawBoard(board,"white");
        repl = new prelogREPL();
    }




}
