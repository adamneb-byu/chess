package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private PieceType type;
    private ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING('k'),
        QUEEN('q'),
        BISHOP('b'),
        KNIGHT('n'),
        ROOK('r'),
        PAWN('p');

        public final char charRepresentation;

        PieceType(char charRepresentation) {
            this.charRepresentation = charRepresentation;
        }
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Gives a small way to represent pieces for the board toString
     *
     * @return a single-character representation of the piece type
     */
    public String getCharRepresentation() {
        String returnMe = "" + type.charRepresentation;
        /*switch(type){
            case PAWN -> {
                returnMe = "p";
            }
            case KNIGHT -> {
                returnMe = "h";
            }
            case ROOK -> {
                returnMe = "r";
            }
            case BISHOP -> {
                returnMe = "b";
            }
            case QUEEN -> {
                returnMe = "q";
            }
            case KING -> {
                returnMe = "k";
            }
        }*/
        if(color == ChessGame.TeamColor.WHITE) {
            returnMe = returnMe.toUpperCase();
        }
        return returnMe;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return PieceMovesCalculator.pieceMoves(board, myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return hashCode() == obj.hashCode();
    }
}
