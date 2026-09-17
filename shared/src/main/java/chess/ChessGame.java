package chess;

import java.lang.reflect.Array;
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
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) PieceMovesCalculator.pieceMoves(board,startPosition);
        TeamColor myColor = board.getPiece(startPosition).getTeamColor();
        ArrayList<ChessMove> badMoves = new ArrayList<>();

        // If any of the moves puts the king in check, delete it from moves
        for(ChessMove move : moves){
            ChessBoard simBoard = simulateMove(move);
            ArrayList<ChessMove> attacks = (ArrayList<ChessMove>) AttackManager.getTeamMoves(getOtherTeam(myColor),true,simBoard);
            if(AttackManager.isUnderAttack(findKing(myColor, simBoard),attacks)){
                badMoves.add(move);
            }
        }
        moves.removeAll(badMoves);
        return moves;
    }

    public ChessBoard simulateMove(ChessMove move){
        ChessBoard simBoard = board.makeCopy();

        simBoard.addPiece(move.getEndPosition(),new ChessPiece(simBoard.getPiece(move.getStartPosition()).getTeamColor(),
                simBoard.getPiece(move.getStartPosition()).getPieceType()));
        simBoard.addPiece(move.getStartPosition(),null);

        return simBoard;
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
            ChessPiece piece = new ChessPiece(originalPiece.getTeamColor(),originalPiece.getPieceType(),move);
            if(move.getPromotionPiece() != null){
                piece = new ChessPiece(originalPiece.getTeamColor(),move.getPromotionPiece(),move);
            }
            boolean enPassant = PawnMovesCalculator.enPassant(board,move.getStartPosition(),move);

            board.addPiece(move.getStartPosition(),null);
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(),piece);

            //System.out.println(move.getSpecialMove());

            //if(move.getSpecialMove() == ChessMove.SpecialMove.EN_PASSANT){


            if(isInCheck(teamTurn)){
                board.addPiece(move.getStartPosition(),originalPiece);
                board.addPiece(move.getEndPosition(),capturedPiece);
                throw new InvalidMoveException();
            }
            if(enPassant){
                ChessPosition target = new ChessPosition(move.getStartPosition().getRow(),move.getEndPosition().getColumn());
                ChessPiece newPiece = new ChessPiece(getOtherTeam(piece.getTeamColor()), ChessPiece.PieceType.QUEEN);
                board.addPiece(target, null);
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
        ArrayList<ChessMove> attacks = (ArrayList<ChessMove>) AttackManager.getTeamMoves(getOtherTeam(teamColor), true, board);
        ChessPosition kingPos = findKing(teamColor, board);
        return AttackManager.isUnderAttack(kingPos, attacks);
    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard board){
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

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = true;
        // Simulate every single possible move, set to false if king is safe
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition curPos = new ChessPosition(i,j);
                if(board.getPiece(curPos) != null &&
                board.getPiece(curPos).getTeamColor() == teamColor &&
                !simulateAttacks(teamColor,curPos)){
                    checkmate = false;
                }
            }
        }
        return checkmate;
    }

    public boolean simulateAttacks(TeamColor teamColor, ChessPosition curPos){
        boolean returnMe = true;
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) PieceMovesCalculator.pieceMoves(board,curPos);
        for(ChessMove move : moves){
            ChessBoard simBoard = simulateMove(move);
            ArrayList<ChessMove> attacks = (ArrayList<ChessMove>) AttackManager.getTeamMoves(getOtherTeam(teamColor),true,simBoard);
            if(!AttackManager.isUnderAttack(findKing(teamColor,simBoard),attacks)){
                returnMe = false;
            }
        }
        return returnMe;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = true;
        // Simulate every single possible move, set to false if a piece can move
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition curPos = new ChessPosition(i,j);
                if(board.getPiece(curPos) != null &&
                        board.getPiece(curPos).getTeamColor() == teamColor){
                    ArrayList<ChessMove> moves = (ArrayList<ChessMove>) validMoves(curPos);
                    // Stalemate is impossible if king is in check
                    if (!moves.isEmpty() || isInCheck(teamColor)){
                        stalemate = false;
                    }
                }
            }
        }
        return stalemate;
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
