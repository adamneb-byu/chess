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

        boolean northwestBlocked = false;
        boolean southwestBlocked = false;
        boolean northeastBlocked = false;
        boolean southeastBlocked = false;

        for(int i = 1; i <= range; i++){
            // Check northwest diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i),
                    null
            ));
            if(returnList.getFirst().isValid() &&
                    board.getPiece(returnList.getFirst().getEndPosition()) != null &&
                    (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
            board.getPiece(myPosition).getTeamColor())) {
                northwestBlocked = true;
            }
            if(!returnList.getFirst().isValid() || northwestBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                northwestBlocked = true;
            }

            // Check northeast diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i),
                    null
            ));
            if(returnList.getFirst().isValid() &&
                    board.getPiece(returnList.getFirst().getEndPosition()) != null &&
                    (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                    board.getPiece(myPosition).getTeamColor())) {
                northeastBlocked = true;
            }
            if(!returnList.getFirst().isValid() || northeastBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                northeastBlocked = true;
            }

            // Check southwest diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i),
                    null
            ));
            if(returnList.getFirst().isValid() &&
                    board.getPiece(returnList.getFirst().getEndPosition()) != null &&
                    (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                    board.getPiece(myPosition).getTeamColor())) {
                southwestBlocked = true;
            }
            if(!returnList.getFirst().isValid() || southwestBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                southwestBlocked = true;
            }

            // Check southeast diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i),
                    null
            ));
            if(returnList.getFirst().isValid() &&
                    board.getPiece(returnList.getFirst().getEndPosition()) != null &&
                    (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                    board.getPiece(myPosition).getTeamColor())) {
                southeastBlocked = true;
            }
            if(!returnList.getFirst().isValid() || southeastBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                southeastBlocked = true;
            }
        }

        return returnList;
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