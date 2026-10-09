package dataaccess;

import data.AuthData;
import service.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO{
    private ArrayList<AuthData> data;

    public MemoryAuthDAO(){
        data = new ArrayList<>();
    }

    @Override
    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public void createAuth(AuthData data) {
        this.data.add(data);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        AuthData removeMe = null;
        for(AuthData auth : data){
            if(auth.authToken().equals(authToken)){
                removeMe = auth;
            }
        }
        data.remove(removeMe);
        if(removeMe == null){
            throw new NotFoundException("Error: authToken not found");
        }
    }

    @Override
    public ArrayList<AuthData> listAuth(){
        return data;
    }
}
