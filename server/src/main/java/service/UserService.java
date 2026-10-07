package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

import java.util.Map;

public class UserService {
    private final UserDAO userDAO;

    public UserService(){
        userDAO = new MemoryUserDAO();
    }

    public void clear(){
        userDAO.clear();
    }

    public void addUser(RegisterRequest request){

    }
}

