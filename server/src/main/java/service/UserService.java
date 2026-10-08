package service;

import data.UserData;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handler.AlreadyTakenException;

import java.util.ArrayList;

public class UserService {
    private final UserDAO userDAO;

    public UserService(){
        userDAO = new MemoryUserDAO();
    }

    public void clear(){
        userDAO.clear();
    }

    public void register(UserData request) throws AlreadyTakenException{
        if(userDAO.getUser(request.username()) == null){
            userDAO.createUser(request);
        }else{
            throw new AlreadyTakenException("Erorr: Username is already taken");
        }
    }

    public ArrayList<UserData> listUsers(){
        return userDAO.listUsers();
    }
}

