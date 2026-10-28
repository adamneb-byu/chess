package client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerFacade {
    String serverUrl;

    public ServerFacade(){

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
