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
            if(!returnList.getFirst().isValid() || northwestBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                northwestBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }

            // Check northeast diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i),
                    null
            ));
            if(!returnList.getFirst().isValid() || northeastBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                northeastBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }

            // Check southwest diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i),
                    null
            ));
            if(!returnList.getFirst().isValid() || southwestBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                southwestBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }

            // Check southeast diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i),
                    null
            ));
            if(!returnList.getFirst().isValid() || southeastBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                southeastBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }
        }

        return returnList;
    }

    public static Collection<ChessMove> findStraights(ChessBoard board, ChessPosition myPosition, int range){
        List<ChessMove> returnList = new ArrayList<>();

        boolean northBlocked = false;
        boolean southBlocked = false;
        boolean eastBlocked = false;
        boolean westBlocked = false;

        for(int i = 1; i <= range; i++){
            // Check north line
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()),
                    null
            ));
            if(!returnList.getFirst().isValid() || northBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                northBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }

            // Check south diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()),
                    null
            ));
            if(!returnList.getFirst().isValid() || southBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                southBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }

            // Check east diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i),
                    null
            ));
            if(!returnList.getFirst().isValid() || eastBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                eastBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }

            // Check west diagonal
            returnList.addFirst(new ChessMove(
                    myPosition,
                    new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i),
                    null
            ));
            if(!returnList.getFirst().isValid() || westBlocked){
                returnList.removeFirst();
            }else if(board.getPiece(returnList.getFirst().getEndPosition()) != null){
                westBlocked = true;
                if (board.getPiece(returnList.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(returnList.getFirst().getStartPosition()).getTeamColor()){
                    returnList.removeFirst();
                }
            }
        }

        return returnList;
    }
}

class KingMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> diagonalMoves = (ArrayList<ChessMove>) findDiagonals(board, myPosition, 1);
        ArrayList<ChessMove> straightMoves = (ArrayList<ChessMove>) findStraights(board, myPosition, 1);
        diagonalMoves.removeAll(straightMoves);
        diagonalMoves.addAll(straightMoves);
        return diagonalMoves;
    }
}

class QueenMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> diagonalMoves = (ArrayList<ChessMove>) findDiagonals(board, myPosition, 8);
        ArrayList<ChessMove> straightMoves = (ArrayList<ChessMove>) findStraights(board, myPosition, 8);
        diagonalMoves.removeAll(straightMoves);
        diagonalMoves.addAll(straightMoves);
        return diagonalMoves;
    }
}

class RookMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return findStraights(board, myPosition, 8);
    }
}

class BishopMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return findDiagonals(board, myPosition, 8);
    }
}

class KnightMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] possibilities = {
                {2,1}, {1,2},
                {-2, 1}, {1, -2},
                {2, -1}, {-1, 2},
                {-2, -1}, {-1, -2}
        };
        for(int[] set : possibilities){
            moves.addFirst(new ChessMove(myPosition,
                    new ChessPosition(myPosition.getRow() + set[0], myPosition.getColumn() + set[1]),
                    null));
            if(!moves.getFirst().isValid() || (board.getPiece(moves.getFirst().getEndPosition()) != null &&
                    board.getPiece(moves.getFirst().getEndPosition()).getTeamColor() == board.getPiece(myPosition).getTeamColor())){
                moves.removeFirst();
            }
        }

        return moves;
    }
}

class PawnMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int colorMod = 1;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            colorMod = -1;
        }
        int[][] options = {
                {colorMod,0,0},{colorMod,-1,1},{colorMod,1,1},{colorMod*2,0,0}
        };
        for(int[] option : options){
            moves.addFirst(new ChessMove(myPosition,
                    new ChessPosition(myPosition.getRow() + option[0],
                            myPosition.getColumn() + option[1]), null));
            if (!moves.getFirst().getEndPosition().isValid()) {
                moves.removeFirst();
            } else if (board.getPiece(moves.getFirst().getEndPosition()) != null){
                if(board.getPiece(moves.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(myPosition).getTeamColor()) {
                    moves.removeFirst();
                }else if(option[2] == 0){
                    moves.removeFirst();
                }
            }else if(option[2] == 1){
                moves.removeFirst();
            }else if(option[0] == colorMod*2){
                if(board.getPiece(new ChessPosition(
                        myPosition.getRow() + colorMod, myPosition.getColumn()
                )) != null || (myPosition.getRow() != 2 && myPosition.getRow() != 7)){
                    moves.removeFirst();
                }
            }
            if(!moves.isEmpty() && moves.getFirst().getPromotionPiece() == null &&
                    (moves.getFirst().getEndPosition().getRow() == 1 ||
                            moves.getFirst().getEndPosition().getRow() == 8)){
                ChessPiece.PieceType[] promotes = {
                        ChessPiece.PieceType.ROOK,
                        ChessPiece.PieceType.KNIGHT,
                        ChessPiece.PieceType.BISHOP
                };
                moves.getFirst().setPromotionPiece(ChessPiece.PieceType.QUEEN);
                for(ChessPiece.PieceType type : promotes){
                    moves.addFirst(new ChessMove(myPosition,
                            moves.getFirst().getEndPosition(),type));
                }
            }
        }
        return moves;
    }

    private static ArrayList<ChessMove> computePromotion(ArrayList<ChessMove> moves, ChessPosition myPosition){
        ChessPiece.PieceType[] promoteTypes = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        moves.getFirst().setPromotionPiece(ChessPiece.PieceType.QUEEN);
        for(ChessPiece.PieceType type : promoteTypes){
            moves.addFirst(new ChessMove(myPosition,
                    moves.getFirst().getEndPosition(),type));
        }
        return moves;
    }
}