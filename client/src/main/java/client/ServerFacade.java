package client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ServerFacade {
    String serverUrl;
    String authToken;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public void clear(){
        makeRequest("DELETE","/db", null, String.class);
    }

    public void register(String name, String password, String email){
        var request = Map.of("username", name, "password", password, "email", email);
        var result = makeRequest("POST","/user",request,String.class);
        authToken = result;
    }

    public void login(String name, String password){
        var request = Map.of("username", name, "password", password);
        var result = makeRequest("POST","/session",request,String.class);
        authToken = result;
    }

    public void logout(){
        var request = Map.of("authToken", authToken);
        makeRequest("POST","/session",request,String.class);
        authToken = "";
    }

    public void listGames(){
        var request = Map.of("authToken",authToken);
        var result = makeRequest("GET","/game",request,String.class);
    }

    public void createGame(String name){
        var request = Map.of("gameName", name);
        var result = makeRequest("POST","/session",request,String.class);
    }

    public void join(String color, int gameID){
        var request = Map.of("playerColor", color, "gameID", gameID);
        var result = makeRequest("POST","/session",request,String.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass){
        try{
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if(request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try(OutputStream outStream = http.getOutputStream()){
                outStream.write(requestData.getBytes());
            }
        }
    }

    private static void throwIfNotSuccessful(HttpURLConnection http){

    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass){

        return null;
    }
}
