package websocket;

import jakarta.websocket.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private HashMap<Integer, ArrayList<Session>> sessions;
    public SessionManager(){
        sessions = new HashMap<Integer, ArrayList<Session>>() {};
    }

    public void addSession(int gameID, Session session){
        ArrayList<Session> idList = sessions.get(gameID);
        if(idList == null){
            idList = new ArrayList<>();
        }
        if(!idList.contains(session)){
            sessions.get(gameID).add(session);
        }
    }

    public void removeSession(int gameID, Session session){
        if(sessions.get(gameID) != null){
            sessions.get(gameID).remove(session);
        }
    }
}
