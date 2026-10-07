package dataaccess;

import data.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO{
    private ArrayList<UserData> data;

    public MemoryUserDAO(){
        data = new ArrayList<>();
    }

    @Override
    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public void createUser(String username) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }
}
