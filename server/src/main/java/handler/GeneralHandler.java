package handler;

import com.google.gson.Gson;
import service.AuthService;
import service.GameService;
import service.RegisterRequest;
import service.UserService;

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

    public void clear(){
        authService.clear();
        gameService.clear();
        userService.clear();
    }

    public void register(String request) throws BadInputException{
        var reqMap = serializer.fromJson(request, Map.class);
        if(reqMap.containsKey("username") && reqMap.containsKey("password")
        && reqMap.containsKey("email")){
            userService.addUser(new RegisterRequest(
                    (String) reqMap.get("username"),
                    (String) reqMap.get("password"),
                    (String) reqMap.get("email")
            ));
        }else{
            throw new BadInputException("Error: bad request");
        }
    }
}
