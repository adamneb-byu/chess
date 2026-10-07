package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class DataService {
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();


    public void clear(){
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }
}
