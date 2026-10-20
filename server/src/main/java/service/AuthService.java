package service;

import data.AuthData;
import data.UserData;
import dataaccess.*;
import handler.LoginRequest;

import java.util.ArrayList;
import java.util.UUID;

public class AuthService {
    private final DatabaseAuthDAO authDAO;

    public AuthService(){
        try {
            authDAO = new DatabaseAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
    }

    public AuthData registerAuth(UserData request) throws DataAccessException {
        String token = generateToken();
        AuthData data = new AuthData(token, request.username());
        authDAO.createAuth(data);
        return data;
    }

    public AuthData loginAuth(LoginRequest request) throws DataAccessException {
        String token = generateToken();
        AuthData data = new AuthData(token, request.username());
        authDAO.createAuth(data);
        return data;
    }

    public void logout(String authToken){
        authDAO.deleteAuth(authToken);
    }

    public boolean verifyAuth(String authToken) throws DataAccessException {
        return authDAO.verifyAuth(authToken);
    }

    public String getUsername(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).username();
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }

    public ArrayList<AuthData> listAuth(){
        return authDAO.listAuth();
    }
}
