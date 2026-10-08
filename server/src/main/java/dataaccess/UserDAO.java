package dataaccess;

import data.UserData;

import java.util.ArrayList;

public interface UserDAO {
    void clear();
    void createUser(UserData data);
    UserData getUser(String username);
    ArrayList<UserData> listUsers();
}
