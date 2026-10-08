package dataaccess;

import data.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData data);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
