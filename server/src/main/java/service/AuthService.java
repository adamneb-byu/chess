package service;

import data.AuthData;
import data.UserData;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.LoginRequest;

import java.util.ArrayList;
import java.util.UUID;

public class AuthService {
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public void clear(){
        authDAO.clear();
    }

    public AuthData registerAuth(UserData request){
        String token = generateToken();
        AuthData data = new AuthData(token, request.username());
        authDAO.createAuth(data);
        return data;
    }

    public AuthData loginAuth(LoginRequest request){
        String token = generateToken();
        AuthData data = new AuthData(token, request.username());
        authDAO.createAuth(data);
        return data;
    }

    public void logout(String authToken){
        authDAO.deleteAuth(authToken);
    }

    public boolean verifyAuth(String authToken){
        return authDAO.verifyAuth(authToken);
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }

    public ArrayList<AuthData> listAuth(){
        return authDAO.listAuth();
    }
}
