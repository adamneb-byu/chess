package dataaccess;

import com.google.gson.Gson;
import data.AuthData;
import io.javalin.http.NotAcceptableResponse;
import service.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseAuthDAO implements AuthDAO{
    public DatabaseAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createAuthTable);
    }

    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.executeStatement("TRUNCATE auth");
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        String statement = "INSERT INTO auth (username, authToken) VALUES ('" +
                data.username() + "', '" + data.authToken() + "')";
        DatabaseManager.executeStatement(statement);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM auth WHERE authToken='" +
                    authToken + "'";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        try {
            var auth = getAuth(authToken);
            if(auth == null){
                throw new NotFoundException("Not found");
            }
            String statement = "DELETE FROM auth WHERE authToken='" + authToken + "'";
            DatabaseManager.executeStatement(statement);
        }catch(DataAccessException e){
            throw new RuntimeException("Error: authToken not found");
        }
    }

    @Override
    public ArrayList<AuthData> listAuth() throws DataAccessException {
        var list = new ArrayList<AuthData>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM auth";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(readAuth(rs));
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {
        var auth = getAuth(authToken);
        return auth != null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var token = rs.getString("authToken");
        return new AuthData(token, username);
    }

    private final String[] createAuthTable = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `userID` int NOT NULL AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `authToken` varchar(256) NOT NULL,
            PRIMARY KEY (`userID`)
            )
            """
    };
}
