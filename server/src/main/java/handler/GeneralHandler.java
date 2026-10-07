package handler;

import service.AuthService;
import service.GameService;
import service.UserService;

public class GeneralHandler {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;

    public GeneralHandler(){
        authService = new AuthService();
        gameService = new GameService();
        userService = new UserService();
    }

    public void clear(){
        authService.clear();
        gameService.clear();
        userService.clear();
    }
}
