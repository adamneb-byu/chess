package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import data.GameData;
import data.GameDataPublic;
import handler.JoinRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO{
    private Gson serializer = new Gson();
    public DatabaseGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createGameTable);
    }

    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.executeStatement("TRUNCATE game");
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var game = new ChessGame();
        var gameJson = serializer.toJson(game);
        String statement = "INSERT INTO game (gameName, chessGame) VALUES ('" +
                gameName + "', '" + gameJson + "')";
        DatabaseManager.executeStatement(statement);
        try(var conn = DatabaseManager.getConnection()){
            statement = "SELECT gameID FROM game WHERE gameName='" +
                    gameName + "'";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Data access error: " + e);
        }
        return -1;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void addUser(JoinRequest request, String username) {

    }

    @Override
    public boolean gameExists(String gameName) {
        return false;
    }

    @Override
    public GameDataPublic[] getGameList() {
        return new GameDataPublic[0];
    }

    private final String[] createGameTable = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256) NOT NULL,
            `blackUsername` varchar(256) NOT NULL,
            `gameName` varchar(256) NOT NULL,
            `chessGame` TEXT DEFAULT NULL,
            PRIMARY KEY (`userID`)
            )
            """
    };
}
