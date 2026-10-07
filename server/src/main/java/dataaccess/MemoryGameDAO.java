package dataaccess;

import data.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    private ArrayList<GameData> data;

    public MemoryGameDAO(){
        data = new ArrayList<>();
    }

    @Override
    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public void createGame() {

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
    public void updateGame(int gameID, String update) {

    }
}
