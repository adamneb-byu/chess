package websocket.commands;

public class UserConnectCommand extends UserGameCommand{
    public UserConnectCommand(String authToken, int gameID){
        super(CommandType.CONNECT, authToken, gameID);
    }
}
