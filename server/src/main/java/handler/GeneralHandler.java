package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import data.AuthData;
import data.GameData;
import data.UserData;
import service.AuthService;
import service.GameService;
import service.NotFoundException;
import service.UserService;

import javax.xml.validation.Validator;
import java.util.ArrayList;

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

    public void clear(){
        authService.clear();
        gameService.clear();
        userService.clear();
    }

    public String register(String request) throws BadInputException{
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

    public String login(String request) throws BadInputException{
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

    public void logout(String authToken){
        authService.logout(authToken);
    }

    public String createGame(String request, String authToken){
        if(authService.verifyAuth(authToken)){
            String gameName = serializer.fromJson(request, GameName.class).gameName();
            if(gameName == null){
                throw new BadInputException("Error: bad input");
            }
            var result = new GameCreateResult(gameService.createGame(request));
            return serializer.toJson(result);
        }else{
            throw new NotFoundException("Error: invalid authToken");
        }
    }

    public ArrayList<UserData> listUsers(){
        return userService.listUsers();
    }
    public ArrayList<AuthData> listAuth(){
        return authService.listAuth();
    }
}
