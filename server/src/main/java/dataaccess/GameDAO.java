package dataaccess;

import data.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    GameData getGame(int gameID);
    ArrayList<GameData> listGames();
    void updateGame(int gameID, String update);
    boolean gameExists(String gameName);
    int newGameID();
}
