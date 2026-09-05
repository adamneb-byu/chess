package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Arrays.fill(board, null);
        addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        String visualBoard = "";

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                if (getPiece(new ChessPosition(i, j)) != null) {
                    visualBoard += String.format("|%s", getPiece(new ChessPosition(i, j)).getCharRepresentation());
                }else {
                    visualBoard += "| ";
                }
            }
            visualBoard += "|\n";
        }

        return visualBoard;
    }
}

class Main{
    public static void main(String[] args){
        ChessBoard testBoard = new ChessBoard();
        System.out.println(testBoard);
    }
}
