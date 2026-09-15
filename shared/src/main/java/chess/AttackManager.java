package chess;

import java.util.ArrayList;
import java.util.Collection;

public class AttackManager {
    public static boolean isUnderAttack(ChessPosition victimPosition, Collection<ChessMove> attacks){
        for(ChessMove attack : attacks){
            if(attack.getEndPosition().equals(victimPosition)){
                return true;
            }
        }
        return false;
    }

    public static Collection<ChessMove> getTeamMoves(ChessGame.TeamColor color, boolean attacksOnly, ChessBoard board){
        ArrayList<ChessMove> moves = new ArrayList<>();
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null &&
                        board.getPiece(pos).getTeamColor() == color){
                    if (attacksOnly && board.getPiece(pos).getPieceType() == ChessPiece.PieceType.PAWN){
                        moves.addAll(PawnMovesCalculator.pieceMoves(board,pos,true));
                    }else {
                        moves.addAll(PieceMovesCalculator.pieceMoves(board,pos));
                    }
                }
            }
        }
        return moves;
    }
}
