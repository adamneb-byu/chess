package dataaccess;

import data.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        var hashData = new UserData(data.username(), BCrypt.hashpw(data.password(),BCrypt.gensalt()), data.email());
        this.data.add(hashData);
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

    public ArrayList<UserData> listUsers(){
        return data;
    }
}
