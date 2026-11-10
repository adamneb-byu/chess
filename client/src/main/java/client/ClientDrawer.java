package client;

import chess.*;
import ui.EscapeSequences;

import java.util.ArrayList;

public class ClientDrawer {
    public static void drawBoard(ChessBoard board, String color){
        boolean whiteSquare = true;
        char[] letters = {'a','b','c','d','e','f','g','h'};
        char[] numbers = {'8','7','6','5','4','3','2','1'};
        if(color.equalsIgnoreCase("black")){
            //whiteSquare = false;
            letters = new char[]{'h','g','f','e','d','c','b','a'};
            numbers = new char[]{'1','2','3','4','5','6','7','8'};
        }
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(j == 0){
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                    System.out.print("\n");
                    whiteSquare = !whiteSquare;
                }
                if(i == 0 || i == 9){
                    drawBorderSquare(j, letters);
                }else if(j == 0 || j == 9){
                    drawBorderSquare(i,numbers);
                }else{
                    ChessPiece piece;
                    if(color.equalsIgnoreCase("white")){
                        piece = board.getPiece(new ChessPosition(9-i,j));
                    }else{
                        piece = board.getPiece(new ChessPosition(i,9-j));
                    }
                    if(whiteSquare){
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }else{
                        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
                    }
                    if(piece != null){
                        drawPiece(piece);
                    }else {
                        System.out.print("   ");
                    }
                    whiteSquare = !whiteSquare;
                }
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    public static void drawHighlightedBoard(ChessBoard board, String color, ArrayList<ChessMove> validMoves){
        //ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) PieceMovesCalculator.pieceMoves(board,pos);
        boolean whiteSquare = true;
        char[] letters = {'a','b','c','d','e','f','g','h'};
        char[] numbers = {'8','7','6','5','4','3','2','1'};
        if(color.equalsIgnoreCase("black")){
            letters = new char[]{'h','g','f','e','d','c','b','a'};
            numbers = new char[]{'1','2','3','4','5','6','7','8'};
        }
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(j == 0){
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                    System.out.print("\n");
                    whiteSquare = !whiteSquare;
                }
                if(i == 0 || i == 9){
                    drawBorderSquare(j, letters);
                }else if(j == 0 || j == 9){
                    drawBorderSquare(i,numbers);
                }else{
                    ChessPiece piece;
                    ChessPosition position;
                    if(color.equalsIgnoreCase("white")){
                        position = new ChessPosition(9-i,j);
                        piece = board.getPiece(position);
                    }else{
                        position = new ChessPosition(i,9-j);
                        piece = board.getPiece(position);
                    }
                    if(whiteSquare){
                        System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    }else{
                        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
                    }
                    if(positionHighlighted(position,validMoves)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    }
                    if(piece != null){
                        drawPiece(piece);
                    }else {
                        System.out.print("   ");
                    }
                    whiteSquare = !whiteSquare;
                }
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    private static boolean positionHighlighted(ChessPosition pos, ArrayList<ChessMove> moves){
        for(ChessMove move : moves){
            if(move.getEndPosition().equals(pos)){
                return true;
            }
        }
        return false;
    }

    private static void drawBorderSquare(int pos, char[] letters){
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
        if(pos > 0 && pos < 9){
            System.out.print(" " + letters[pos - 1] + " ");
        }else {
            System.out.print("   ");
        }
    }

    private static void drawPiece(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        }else{
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        }
        System.out.print(" " + piece.getCharRepresentation() + " ");
    }
}
