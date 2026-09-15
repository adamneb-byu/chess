package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return PieceMovesCalculator.pieceMoves(board,startPosition);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(move.isValid() && board.getPiece(move.getStartPosition()) != null &&
                board.getPiece(move.getStartPosition()).getTeamColor() == teamTurn &&
                isPossibleMove(move, PieceMovesCalculator.pieceMoves(board,move.getStartPosition()))){
            ChessPiece originalPiece = board.getPiece(move.getStartPosition());
            ChessPiece piece = new ChessPiece(originalPiece.getTeamColor(),originalPiece.getPieceType());
            if(move.getPromotionPiece() != null){
                piece = new ChessPiece(originalPiece.getTeamColor(),move.getPromotionPiece());
            }
            board.addPiece(move.getStartPosition(),null);
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(),piece);

            if(isInCheck(teamTurn)){
                board.addPiece(move.getStartPosition(),originalPiece);
                board.addPiece(move.getEndPosition(),capturedPiece);
                throw new InvalidMoveException();
            }
            if(teamTurn == TeamColor.BLACK){
                teamTurn = TeamColor.WHITE;
            }else{
                teamTurn = TeamColor.BLACK;
            }
        }else{
            throw new InvalidMoveException();
        }
    }

    public boolean isPossibleMove(ChessMove move, Collection<ChessMove> validMoves){
        for(ChessMove validMove : validMoves){
            if(move.equals(validMove)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessMove> attacks = (ArrayList<ChessMove>) getTeamMoves(getOtherTeam(teamColor), true);
        ChessPosition kingPos = findKing(teamColor);
        return isUnderAttack(kingPos, attacks);
    }

    public ChessPosition findKing(TeamColor teamColor){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                ChessPosition myPosition = new ChessPosition(i,j);
                if(board.getPiece(myPosition)!= null &&
                board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING &&
                board.getPiece(myPosition).getTeamColor() == teamColor){
                    return myPosition;
                }
            }
        }
        return null;
    }

    public boolean isUnderAttack(ChessPosition victimPosition, Collection<ChessMove> attacks){
        for(ChessMove attack : attacks){
            if(attack.getEndPosition().equals(victimPosition)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public TeamColor getOtherTeam(TeamColor color){
        if(color == TeamColor.WHITE){
            return TeamColor.BLACK;
        }else{
            return TeamColor.WHITE;
        }
    }

    public Collection<ChessMove> getTeamMoves(TeamColor color, boolean attacksOnly){
        ArrayList<ChessMove> moves = new ArrayList<>();
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null &&
                board.getPiece(pos).getTeamColor() == color){
                    if (attacksOnly && board.getPiece(pos).getPieceType() == ChessPiece.PieceType.PAWN){
                        moves.addAll(PawnMovesCalculator.pieceMoves(board,pos,true));
                    }else {
                        moves.addAll(validMoves(pos));
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
