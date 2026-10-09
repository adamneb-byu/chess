package dataaccess;

import data.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData data);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    ArrayList<AuthData> listAuth();
}
