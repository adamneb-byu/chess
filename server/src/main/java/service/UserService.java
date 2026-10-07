package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(){
        userDAO = new MemoryUserDAO();
    }

    public void clear(){
        userDAO.clear();
    }
}
