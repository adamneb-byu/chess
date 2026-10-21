package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import data.GameData;
import data.GameDataPublic;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import handler.AlreadyTakenException;
import handler.BadInputException;
import handler.JoinRequest;
import service.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO {
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
        String statement = "INSERT INTO game (gameName, chessGame) VALUES (\"" +
                gameName + "\", '" + gameJson + "')";
        DatabaseManager.executeStatement(statement);
        try(var conn = DatabaseManager.getConnection()){
            statement = "SELECT gameID FROM game WHERE gameName=\"" +
                    gameName + "\"";
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
    public GameData getGame(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM game WHERE gameID=" + gameID;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var list = new ArrayList<GameData>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(readGame(rs));
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return list;
    }

    @Override
    public void addUser(JoinRequest request, String username) throws DataAccessException {
        GameData game = getGame(request.gameID());
        if(game == null){
            throw new NotFoundException("Error: game not found");
        }else if((request.playerColor().equalsIgnoreCase("white") && game.whiteUsername() != null)
                || (request.playerColor().equalsIgnoreCase("black") && game.blackUsername() != null)){
            throw new AlreadyTakenException("Error: color is already taken");
        }

        if(request.playerColor().equalsIgnoreCase("white")){
           DatabaseManager.executeStatement("UPDATE game SET whiteUsername = \"" + username + "\" WHERE gameID = " + request.gameID());
        }else if(request.playerColor().equalsIgnoreCase("black")){
            DatabaseManager.executeStatement("UPDATE game SET blackUsername = \"" + username + "\" WHERE gameID = " + request.gameID());
        }else{
            throw new BadInputException("Error: invalid color");
        }
    }

    @Override
    public boolean gameExists(String gameName) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID FROM game WHERE gameName=\"" +
                    gameName + "\"";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) != -1;
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return false;
    }

    @Override
    public GameDataPublic[] getGameList() throws DataAccessException {
        var data = listGames();
        GameDataPublic[] list = new GameDataPublic[data.size()];
        int counter = 0;
        for(GameData game : data){
            list[counter] = new GameDataPublic(
                    game.gameID(), game.whiteUsername(),
                    game.blackUsername(),game.gameName()
            );
            counter++;
        }
        return list;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var gameJson = rs.getString("chessGame");
        var game = serializer.fromJson(gameJson, ChessGame.class);
        return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
    }

    private final String[] createGameTable = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `chessGame` TEXT DEFAULT NULL,
            PRIMARY KEY (`gameID`)
            )
            """
    };
}
