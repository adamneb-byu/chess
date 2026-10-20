package dataaccess;

import data.AuthData;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseAuthDAO implements AuthDAO{
    public DatabaseAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        executeStatement("TRUNCATE auth");
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        String statement = "INSERT INTO auth (username, authToken) VALUES ('" +
                data.username() + "', '" + data.authToken() + "')";
        executeStatement(statement);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM auth WHERE authToken='" +
                    authToken + "'";
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<AuthData> listAuth() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean verifyAuth(String authToken) {
        throw new RuntimeException("Not implemented");
    }

    private void executeStatement(String statement) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch(Exception e){
            throw new DataAccessException("Unable to clear table: " + e.getMessage());
        }
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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
        for(String statement : createAuthTable){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        }
        } catch (Exception e) {
            throw new DataAccessException("Unable to configure database:" + e.getMessage());
        }
    }
}
