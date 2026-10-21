package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import data.AuthData;
import data.GameData;
import data.UserData;
import dataaccess.DataAccessException;
import service.*;

import javax.xml.crypto.Data;
import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.Map;

public class GeneralHandler {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson serializer;

    public GeneralHandler(){
        authService = new AuthService();
        gameService = new GameService();
        userService = new UserService();
        serializer = new Gson();
    }

    public void clear() throws DataAccessException {
        try {
            authService.clear();
            gameService.clear();
            userService.clear();
        }catch(Exception e){
            throw new RuntimeException("Error: problem detected! " + e.getMessage());
        }
    }

    public String register(String request) throws BadInputException, DataAccessException {
        try {
            var regRequest = serializer.fromJson(request, UserData.class);
            if(!JsonValidator.validateUserData(regRequest)){
                throw new JsonSyntaxException("Error: bad input");
            }
            userService.register(regRequest);
            AuthData data = authService.registerAuth(regRequest);
            return serializer.toJson(data);
        } catch (JsonSyntaxException e) {
            throw new BadInputException("Error: Invalid Json input");
        }
    }

    public String login(String request) throws BadInputException, DataAccessException {
        try {
            var loginRequest = serializer.fromJson(request, LoginRequest.class);
            if(!JsonValidator.validateLoginData(loginRequest)){
                throw new JsonSyntaxException("Error: bad input");
            }
            userService.verifyPassword(loginRequest);
            var data = authService.loginAuth(loginRequest);
            return serializer.toJson(data);
        } catch (JsonSyntaxException e) {
            throw new BadInputException("Error: Invalid Json input");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        authService.logout(authToken);
    }

    public String createGame(String request, String authToken) throws DataAccessException {
        if (authService.verifyAuth(authToken)) {
            String gameName = serializer.fromJson(request, GameName.class).gameName();
            if (gameName == null) {
                throw new BadInputException("Error: bad input");
            }
            var result = new GameCreateResult(gameService.createGame(gameName));
            return serializer.toJson(result);
        } else {
            throw new UnauthorizedException("Error: invalid authToken");
        }
    }

    public String listGames(String authToken) throws DataAccessException {
        if(authService.verifyAuth(authToken)){
            var map = Map.of("games", gameService.listGames());
            return serializer.toJson(map);
        }else{
            throw new UnauthorizedException("Error: invalid authToken");
        }
    }

    public void joinGame(String request, String authToken) throws DataAccessException {
        if (authService.verifyAuth(authToken)) {
            var joinReq = serializer.fromJson(request, JoinRequest.class);
            if (joinReq.playerColor() == null || joinReq.gameID() == 0) {
                throw new BadInputException("Error: invalid input");
            }
            String username = authService.getUsername(authToken);
            gameService.joinGame(joinReq, username);
        } else {
            throw new UnauthorizedException("Error: invalid authToken");
        }
    }

    public ArrayList<UserData> listUsers() throws DataAccessException {
        return userService.listUsers();
    }
    public ArrayList<AuthData> listAuth() throws DataAccessException {
        return authService.listAuth();
    }
    public GameData getGame(int gameID) throws DataAccessException {
        return gameService.getGame(gameID);
    }
}
