package dataaccess;

import data.AuthData;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO{
    private ArrayList<AuthData> data;

    public MemoryAuthDAO(){
        data = new ArrayList<>();
    }

    @Override
    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public void createAuth(AuthData data) {
        this.data.add(data);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) {

    }
}
