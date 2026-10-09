package dataaccess;

import data.GameData;
import data.GameDataPublic;
import handler.JoinRequest;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    GameData getGame(int gameID);
    ArrayList<GameData> listGames();
    void addUser(JoinRequest request, String username);
    boolean gameExists(String gameName);
    int newGameID();
    GameDataPublic[] getGameList();
}
