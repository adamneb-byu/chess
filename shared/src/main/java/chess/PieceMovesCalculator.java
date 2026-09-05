package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece piece = board.getPiece(myPosition);
        switch(piece.getPieceType()){
            case KING -> {
                return KingMovesCalculator.pieceMoves(board, myPosition);
            }
            case QUEEN -> {
                return QueenMovesCalculator.pieceMoves(board, myPosition);
            }
            case ROOK -> {
                return RookMovesCalculator.pieceMoves(board, myPosition);
            }
            case BISHOP -> {
                return BishopMovesCalculator.pieceMoves(board, myPosition);
            }
            case KNIGHT -> {
                return KnightMovesCalculator.pieceMoves(board, myPosition);
            }
            case PAWN -> {
                return PawnMovesCalculator.pieceMoves(board, myPosition);
            }
            case null, default -> {
                return List.of();
            }
        }
    }

    public static Collection<ChessMove> findDiagonals(ChessBoard board, ChessPosition myPosition, int range){
        List<ChessMove> returnList = new ArrayList<>();

        for(int i = 1; i <= range; i++){
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i),
                    null
            ));
            if(!returnList.getFirst().isValid()){
                returnList.removeFirst();
            }
        }

        return List.of();
    }

    public static Collection<ChessMove> findStraights(ChessBoard board, ChessPosition myPosition, int range){
        return List.of();
    }
}

class KingMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return List.of();
    }
}

class QueenMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return List.of();
    }
}

class RookMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return List.of();
    }
}

class BishopMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return findDiagonals(board, myPosition, 8);
    }
}

class KnightMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return List.of();
    }
}

class PawnMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return List.of();
    }
}