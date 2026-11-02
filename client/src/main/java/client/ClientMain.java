package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    private static ChessClient client;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        client = new ChessClient();
    }
}
