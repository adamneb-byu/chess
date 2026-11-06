package websocket.commands;

public class UserResignCommand extends UserGameCommand{
    public UserResignCommand(String authToken, int gameID){
        super(CommandType.RESIGN, authToken, gameID);
    }
}
