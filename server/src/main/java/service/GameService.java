package service;

import data.GameData;
import data.GameDataPublic;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import handler.AlreadyTakenException;
import handler.GameCreateResult;
import handler.GeneralHandler;
import handler.JoinRequest;

public class GameService {
    private final MemoryGameDAO gameDAO;

    public GameService(){
        gameDAO = new MemoryGameDAO();
    }

    public void clear(){
        gameDAO.clear();
    }

    public int createGame(String gameName){
        if(gameDAO.gameExists(gameName)){
            throw new AlreadyTakenException("Error: game name already taken");
        }
        return gameDAO.createGame(gameName);
    }

    public void joinGame(JoinRequest request, String username){
        gameDAO.addUser(request, username);
    }

    public GameDataPublic[] listGames(){
        return gameDAO.getGameList();
    }

    public GameData getGame(int gameID){
        return gameDAO.getGame(gameID);
    }
}
