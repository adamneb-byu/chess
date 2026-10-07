package dataaccess;

import data.UserData;

public interface UserDAO {
    void clear();
    void createUser(String username);
    UserData getUser(String username);
}
