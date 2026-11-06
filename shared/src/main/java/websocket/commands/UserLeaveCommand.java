package websocket.commands;

public class UserLeaveCommand extends UserGameCommand{
    public UserLeaveCommand(String authToken, int gameID){
        super(CommandType.LEAVE, authToken, gameID);
    }
}
