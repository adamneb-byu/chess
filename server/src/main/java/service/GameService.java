package service;

import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import handler.GeneralHandler;

public class GameService {
    private final MemoryGameDAO gameDAO;

    public GameService(){
        gameDAO = new MemoryGameDAO();
    }

    public void clear(){
        gameDAO.clear();
    }
}
