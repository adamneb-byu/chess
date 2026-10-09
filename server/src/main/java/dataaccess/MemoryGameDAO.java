package dataaccess;

import chess.ChessGame;
import data.GameData;
import data.GameDataPublic;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    private ArrayList<GameData> data;
    private int currentID;

    public MemoryGameDAO(){
        data = new ArrayList<>();
        currentID = 1300;
    }

    @Override
    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public int createGame(String gameName) {
        int gameID = newGameID();
        var newGame = new GameData(gameID,"","",gameName,new ChessGame());
        data.add(newGame);
        return gameID;
    }

    @Override
    public boolean gameExists(String gameName){
        for(GameData game : data){
            if(game.gameName().equals(gameName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public GameDataPublic[] getGameList(){
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

    @Override
    public int newGameID(){
        currentID++;
        return currentID;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return data;
    }

    @Override
    public void updateGame(int gameID, String update) {

    }
}
