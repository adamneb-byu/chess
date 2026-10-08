package service;

import data.AuthData;
import data.UserData;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import java.util.UUID;

public class AuthService {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public void clear(){
        authDAO.clear();
    }

    public AuthData registerAuth(UserData request){
        String token = generateToken();
        AuthData data = new AuthData(token, request.username());
        authDAO.createAuth(data);
        return data;
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }
}
