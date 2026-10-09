package dataaccess;

import chess.ChessGame;
import data.GameData;
import data.GameDataPublic;
import handler.AlreadyTakenException;
import handler.BadInputException;
import handler.JoinRequest;
import service.NotFoundException;

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
        var newGame = new GameData(gameID,null,null,gameName,new ChessGame());
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
    public void addUser(JoinRequest request, String username){
        GameData game = getGame(request.gameID());
        if(game == null){
            throw new NotFoundException("Error: game not found");
        }else if((request.playerColor().equalsIgnoreCase("white") && game.whiteUsername() != null)
        || (request.playerColor().equalsIgnoreCase("black") && game.blackUsername() != null)){
            throw new AlreadyTakenException("Error: color is already taken");
        }

        data.remove(game);
        GameData newGame;
        if(request.playerColor().equalsIgnoreCase("white")){
            newGame = new GameData(
                    game.gameID(), username, game.blackUsername(),
                    game.gameName(), game.game()
            );
        }else if(request.playerColor().equalsIgnoreCase("black")){
            newGame = new GameData(
                    game.gameID(), game.whiteUsername(), username,
                    game.gameName(), game.game()
            );
        }else{
            throw new BadInputException("Error: invalid color");
        }
        data.add(newGame);
    }

    @Override
    public int newGameID(){
        currentID++;
        return currentID;
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData game : data){
            if(game.gameID() == gameID){
                return game;
            }
        }
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
