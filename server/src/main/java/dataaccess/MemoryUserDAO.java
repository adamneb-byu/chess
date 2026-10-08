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
    public void createUser(UserData data) {
        this.data.add(data);
    }

    @Override
    public UserData getUser(String username) {
        for(UserData user : data){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }
}
