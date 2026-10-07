package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class AuthService {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public void clear(){
        authDAO.clear();
    }
}
