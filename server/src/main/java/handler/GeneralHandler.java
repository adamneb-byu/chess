package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import data.AuthData;
import data.UserData;
import service.AuthService;
import service.GameService;
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

    public ArrayList<UserData> listUsers(){
        return userService.listUsers();
    }
}
