package client;

public class REPL {
    public ServerFacade facade;
    public ChessClient parentClient;
    public static String[] processInput(String input){
        String[] result = input.split(" ");
        if(result.length == 0){
            return new String[]{""};
        }
        return result;
    }
}
