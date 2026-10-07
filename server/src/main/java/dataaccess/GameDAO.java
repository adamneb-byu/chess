package dataaccess;

import data.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    void createGame();
    GameData getGame(int gameID);
    ArrayList<GameData> listGames();
    void updateGame(int gameID, String update);
}
