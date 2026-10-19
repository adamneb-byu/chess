package dataaccess;

import data.AuthData;

import java.util.ArrayList;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public void clear() {

    }

    @Override
    public void createAuth(AuthData data) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public ArrayList<AuthData> listAuth() {
        return null;
    }

    @Override
    public boolean verifyAuth(String authToken) {
        return false;
    }
}
