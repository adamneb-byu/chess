package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private HashMap<Integer, ArrayList<Session>> sessions;
    public SessionManager(){
        sessions = new HashMap<Integer, ArrayList<Session>>() {};
    }

    public void addSession(int gameID, Session session){
        ArrayList<Session> idList = sessions.computeIfAbsent(gameID, k -> new ArrayList<>());
        if(!idList.contains(session)){
            sessions.get(gameID).add(session);
        }
    }

    public void removeSession(int gameID, Session session){
        if(sessions.get(gameID) != null){
            sessions.get(gameID).remove(session);
        }
    }

    public void broadcast(int gameID, ServerMessage serverMessage) throws IOException {
        String msg = new Gson().toJson(serverMessage);
        for(Session sess : sessions.get(gameID)){
            if(sess.isOpen()){
                sess.getRemote().sendString(msg);
            }
        }
    }
}
