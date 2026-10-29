package client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {
    String serverUrl;
    String authToken;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public void clear(){
        makeRequest("DELETE","/db", null, String.class);
        authToken = "";
    }

    public void register(String name, String password, String email){
        var request = Map.of("username", name, "password", password, "email", email);
        var result = makeRequest("POST","/user",request,Map.class);
        authToken = result.get("authToken").toString();
    }

    public void login(String name, String password){
        var request = Map.of("username", name, "password", password);
        var result = makeRequest("POST","/session",request,Map.class);
        authToken = result.get("authToken").toString();
    }

    public void logout(){
        var request = Map.of("authToken", authToken);
        makeRequest("DELETE","/session",request,Map.class);
        authToken = "";
    }

    public Map listGames(){
        var request = Map.of("authToken",authToken);
        return makeRequest("GET","/game",request,Map.class);
    }

    public int createGame(String name){
        var request = Map.of("gameName", name);
        var result = makeRequest("POST","/session",request,String.class);
        return Integer.parseInt(result);
    }

    public void join(String color, int gameID){
        var request = Map.of("playerColor", color, "gameID", gameID);
        var result = makeRequest("POST","/session",request,Map.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass){
        try{
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("Authorization",authToken);

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

    private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if(status - 200 > 99){
            throw new RuntimeException();
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if(http.getContentLength() > 0){
            try(InputStream responseBody = http.getInputStream()){
                InputStreamReader inReader = new InputStreamReader(responseBody);
                if(responseClass != null){
                    response = new Gson().fromJson(inReader, responseClass);
                }
            }
        }
        return response;
    }
}
