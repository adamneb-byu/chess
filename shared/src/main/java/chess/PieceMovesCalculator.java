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
                return PawnMovesCalculator.pieceMoves(board, myPosition, false);
            }
            case null, default -> {
                return List.of();
            }
        }
    }

    public static Collection<ChessMove> findDiagonals(ChessBoard board, ChessPosition myPosition, int range){
        int[][] options = {
                {1,1},{-1,1},{1,-1},{-1,-1}
        };
        return processOptions(board,myPosition,range,options);
    }

    public static Collection<ChessMove> findStraights(ChessBoard board, ChessPosition myPosition, int range){
        int[][] options = {
                {1,0},{-1,0},{0,1},{0,-1}
        };
        return processOptions(board,myPosition,range,options);
    }

    private static Collection<ChessMove> processOptions(ChessBoard board, ChessPosition myPosition, int range, int[][] options){
        boolean[] blocked = {false,false,false,false};
        ArrayList<ChessMove> moves = new ArrayList<>();
        for(int i = 1; i <= range; i++){
            for(int j = 0; j < 4; j++){
                moves.addFirst(new ChessMove(myPosition,
                        new ChessPosition(myPosition.getRow()+(i*options[j][0]),
                                myPosition.getColumn()+(i*options[j][1])),
                        null
                ));
                if(blocked[j] || !moves.getFirst().getEndPosition().isValid()){
                    moves.removeFirst();
                }else if(board.getPiece(moves.getFirst().getEndPosition()) != null){
                    blocked[j] = true;
                    if(board.getPiece(moves.getFirst().getEndPosition()).getTeamColor() ==
                            board.getPiece(myPosition).getTeamColor()){
                        moves.removeFirst();
                    }
                }
            }
        }
        return moves;
    }
}

class KingMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> diagonalMoves = (ArrayList<ChessMove>) findDiagonals(board, myPosition, 1);
        ArrayList<ChessMove> straightMoves = (ArrayList<ChessMove>) findStraights(board, myPosition, 1);
        diagonalMoves.removeAll(straightMoves);
        diagonalMoves.addAll(straightMoves);

        if(!board.getPiece(myPosition).getHasMoved() &&
        !AttackManager.isUnderAttack(myPosition,AttackManager.getTeamMoves(
                ChessGame.getOtherTeam(board.getPiece(myPosition).getTeamColor()),
                true, board
        ))) {
            boolean[] canCastle = castle(board, myPosition);
            if (canCastle[0]) {
                diagonalMoves.addFirst(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), 3), null));
            }
            if (canCastle[1]) {
                diagonalMoves.addFirst(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), 7), null));
            }
        }
        return diagonalMoves;
    }

    public static Collection<ChessMove> pieceAttacks(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> diagonalMoves = (ArrayList<ChessMove>) findDiagonals(board, myPosition, 1);
        ArrayList<ChessMove> straightMoves = (ArrayList<ChessMove>) findStraights(board, myPosition, 1);
        diagonalMoves.removeAll(straightMoves);
        diagonalMoves.addAll(straightMoves);

        return diagonalMoves;
    }

    public static boolean[] castle(ChessBoard board, ChessPosition myPosition){
        boolean[] result = {true,true};
        if(board.getPiece(myPosition).getHasMoved()){
            return new boolean[] {false, false};
        }
        ChessPiece leftRook = board.getPiece(new ChessPosition(myPosition.getRow(), 1));
        if(leftRook != null) {
            if (leftRook.getHasMoved() || leftRook.getPieceType() != ChessPiece.PieceType.ROOK) {
                // If left rook has moved, left castling is impossible
                result[0] = false;
            } else {
                // All spaces between rook and king must be clear
                int[] checkColumns = {2, 3, 4};
                for (int col : checkColumns) {
                    ChessPosition checkPos = new ChessPosition(myPosition.getRow(), col);
                    if (board.getPiece(checkPos) != null) {
                        result[0] = false;
                    }
                    // Castling cannot take king through or to check
                    ChessGame.TeamColor color = ChessGame.getOtherTeam(leftRook.getTeamColor());
                    ArrayList<ChessMove> attacks = (ArrayList<ChessMove>) AttackManager.getTeamMoves(color, true, board);
                    if (col != 2 && AttackManager.isUnderAttack(checkPos, attacks)) {
                        result[0] = false;
                    }
                }
            }
        }else{
            result[0] = false;
        }
        ChessPiece rightRook = board.getPiece(new ChessPosition(myPosition.getRow(), 8));
        if(rightRook != null) {
            if (rightRook.getHasMoved() || rightRook.getPieceType() != ChessPiece.PieceType.ROOK) {
                // If right rook has moved, right castling is impossible
                result[1] = false;
            } else {
                // All spaces between rook and king must be clear
                int[] checkColumns = {6, 7};
                for (int col : checkColumns) {
                    ChessPosition checkPos = new ChessPosition(myPosition.getRow(), col);
                    if (board.getPiece(checkPos) != null) {
                        result[1] = false;
                    }
                    // Castling cannot take king through or to check
                    ChessGame.TeamColor color = ChessGame.getOtherTeam(rightRook.getTeamColor());
                    ArrayList<ChessMove> attacks = (ArrayList<ChessMove>) AttackManager.getTeamMoves(color, true, board);
                    if (AttackManager.isUnderAttack(checkPos, attacks)) {
                        result[1] = false;
                    }
                }
            }
        }else{
            result[1] = false;
        }
        return result;
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
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, boolean attacksOnly){
        ArrayList<ChessMove> moves = new ArrayList<>();
        // Set possible movement patterns based on team color
        int colorMod = 1;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            colorMod = -1;
        }
        // {row, col, isAttack}
        int[][] options = {
                {colorMod,0,0},{colorMod,-1,1},{colorMod,1,1},{colorMod*2,0,0}
        };
        for(int[] option : options){
            // Add proposed move to list
            moves.addFirst(new ChessMove(myPosition,
                    new ChessPosition(myPosition.getRow() + option[0],
                            myPosition.getColumn() + option[1]), null));
            if (attacksOnly && option[2] == 0){
                // Remove non-attack moves if only counting attacks
                moves.removeFirst();
            }else if (!moves.getFirst().getEndPosition().isValid()) {
                // Remove if move is invalid
                moves.removeFirst();
            } else if (board.getPiece(moves.getFirst().getEndPosition()) != null){
                // Logic for if trying to move onto a piece
                if(board.getPiece(moves.getFirst().getEndPosition()).getTeamColor() ==
                        board.getPiece(myPosition).getTeamColor()) {
                    // Remove if trying to capture a friendly piece
                    moves.removeFirst();
                }else if(option[2] == 0){
                    // Remove if trying to move straight forward onto an enemy piece
                    moves.removeFirst();
                }
            }else if(option[2] == 1 && !enPassant(board, myPosition, moves.getFirst())){
                // Remove if proposed move is an attack but there is no piece to attack
                moves.removeFirst();
            }else if(option[0] == colorMod*2){
                // Only allow moving forward two spaces if on starting row
                if(board.getPiece(new ChessPosition(
                        myPosition.getRow() + colorMod, myPosition.getColumn()
                )) != null || (myPosition.getRow() != 2 && myPosition.getRow() != 7)){
                    moves.removeFirst();
                }
            }
            // Promotion logic
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

    public static boolean enPassant(ChessBoard board, ChessPosition myPosition, ChessMove move){
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessPosition targetPos = new ChessPosition(move.getStartPosition().getRow(),move.getEndPosition().getColumn());
        ChessPiece targetPiece = board.getPiece(targetPos);
        if(targetPiece == null || targetPiece.getLastMove() == null){
            return false;
        }
        return targetPiece.getTeamColor() != myPiece.getTeamColor() &&
                targetPiece.getPieceType() == ChessPiece.PieceType.PAWN &&
                Math.abs(targetPiece.getLastMove().getEndPosition().getRow() - targetPiece.getLastMove().getStartPosition().getRow()) == 2;
    }
}