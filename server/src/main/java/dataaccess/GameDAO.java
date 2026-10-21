package dataaccess;

import data.GameData;
import data.GameDataPublic;
import handler.JoinRequest;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void addUser(JoinRequest request, String username) throws DataAccessException;
    boolean gameExists(String gameName) throws DataAccessException;
    GameDataPublic[] getGameList();
}
