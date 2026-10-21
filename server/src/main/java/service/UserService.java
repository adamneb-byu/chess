package service;

import data.UserData;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handler.AlreadyTakenException;
import handler.LoginRequest;

import java.util.ArrayList;

public class UserService {
    private final UserDAO userDAO;

    public UserService(){
        userDAO = new MemoryUserDAO();
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
    }

    public void register(UserData request) throws AlreadyTakenException, DataAccessException {
        if(userDAO.getUser(request.username()) == null){
            userDAO.createUser(request);
        }else{
            throw new AlreadyTakenException("Erorr: Username is already taken");
        }
    }

    public void verifyPassword(LoginRequest request) throws DataAccessException {
        var data = userDAO.getUser(request.username());
        if(data == null){
            throw new NotFoundException("Error: user not found");
        }else if(!data.password().equals(request.password())){
            throw new IncorrectPasswordException("Error: incorrect password");
        }
    }

    public ArrayList<UserData> listUsers() throws DataAccessException {
        return userDAO.listUsers();
    }
}

