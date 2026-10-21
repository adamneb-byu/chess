package dataaccess;

import data.UserData;

import java.util.ArrayList;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData data) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    ArrayList<UserData> listUsers() throws DataAccessException;
}
