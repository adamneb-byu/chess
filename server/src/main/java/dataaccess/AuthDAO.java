package dataaccess;

import data.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(AuthData data);
}
