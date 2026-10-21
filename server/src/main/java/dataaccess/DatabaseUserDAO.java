package dataaccess;

import data.AuthData;
import data.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseUserDAO implements UserDAO{
    public DatabaseUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createUserTable);
    }
    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.executeStatement("TRUNCATE user");
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        String statement = "INSERT INTO user (username, email, password) VALUES ('" +
                data.username() + "', '" + data.email() + "', '" + hashedPassword + "')";
        DatabaseManager.executeStatement(statement);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, email, password FROM user WHERE username='" +
                    username + "'";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<UserData> listUsers() throws DataAccessException {
        var list = new ArrayList<UserData>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, email, password FROM user";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(readUser(rs));
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException("Unable to get data from database:" + e.getMessage());
        }
        return list;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var passHashed = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, passHashed, email);
    }

    private final String[] createUserTable = {
            """
            CREATE TABLE IF NOT EXISTS user (
            `userID` int NOT NULL AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            PRIMARY KEY (`userID`)
            )
            """
    };
}
