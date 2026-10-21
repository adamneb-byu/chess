package dataaccess;

import data.GameData;
import data.GameDataPublic;
import handler.JoinRequest;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID);
    ArrayList<GameData> listGames();
    void addUser(JoinRequest request, String username);
    boolean gameExists(String gameName);
    GameDataPublic[] getGameList();
}
