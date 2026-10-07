package dataaccess;

import data.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO{
    private ArrayList<AuthData> data;

    public MemoryAuthDAO(){
        data = new ArrayList<>();
    }

    @Override
    public void clear() {

    }

    @Override
    public void createAuth(String username) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) {

    }
}
