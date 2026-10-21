package service;

import data.GameData;
import data.GameDataPublic;
import dataaccess.*;
import dataaccess.sql.DatabaseGameDAO;
import handler.AlreadyTakenException;
import handler.JoinRequest;

public class GameService {
    private final DatabaseGameDAO gameDAO;

    public GameService(){
        try {
            gameDAO = new DatabaseGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    public int createGame(String gameName) throws DataAccessException {
        if(gameDAO.gameExists(gameName)){
            throw new AlreadyTakenException("Error: game name already taken");
        }
        return gameDAO.createGame(gameName);
    }

    public void joinGame(JoinRequest request, String username) throws DataAccessException {
        gameDAO.addUser(request, username);
    }

    public GameDataPublic[] listGames() throws DataAccessException {
        return gameDAO.getGameList();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDAO.getGame(gameID);
    }
}
