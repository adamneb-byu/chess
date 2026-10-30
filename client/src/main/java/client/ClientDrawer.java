package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

public class ClientDrawer {
    public static void drawBoard(ChessBoard board, String color){
        boolean whiteSquare = true;
        char[] letters = {'a','b','c','d','e','f','g','h'};
        char[] numbers = {'8','7','6','5','4','3','2','1'};
        if(color.equalsIgnoreCase("black")){
            whiteSquare = false;
            letters = new char[]{'h','g','f','e','d','c','b','a'};
            numbers = new char[]{1,2,3,4,5,6,7,8};
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
                        piece = board.getPiece(new ChessPosition(9-i,9-j));
                    }else{
                        piece = board.getPiece(new ChessPosition(i,j));
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
