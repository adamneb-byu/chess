package dataaccess;

import data.GameData;
import data.GameDataPublic;
import handler.JoinRequest;

import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO{
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
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
    public int newGameID() {
        return 0;
    }

    @Override
    public GameDataPublic[] getGameList() {
        return new GameDataPublic[0];
    }
}
