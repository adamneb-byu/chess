package dataaccess;

import data.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
    void clear() throws DataAccessException;
    void createAuth(AuthData data) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);
    ArrayList<AuthData> listAuth();
    boolean verifyAuth(String authToken) throws DataAccessException;
}
