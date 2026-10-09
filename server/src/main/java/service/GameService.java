package service;

import data.GameDataPublic;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import handler.AlreadyTakenException;
import handler.GameCreateResult;
import handler.GeneralHandler;

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

    public GameDataPublic[] listGames(){
        return gameDAO.getGameList();
    }
}
