package service;

import data.UserData;
import dataaccess.*;
import handler.AlreadyTakenException;
import handler.LoginRequest;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        try {
            userDAO = new DatabaseUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
        //}else if(!data.password().equals(request.password())){
        }else if(!BCrypt.checkpw(request.password(), data.password())){
            throw new IncorrectPasswordException("Error: incorrect password");
        }
    }

    public ArrayList<UserData> listUsers() throws DataAccessException {
        return userDAO.listUsers();
    }
}

