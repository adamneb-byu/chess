package server;

import handler.AlreadyTakenException;
import handler.BadInputException;
import handler.GeneralHandler;
import service.IncorrectPasswordException;
import service.NotFoundException;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class Server {

    private final Javalin javalin;

    private final GeneralHandler handler;

    public Server() {
        handler = new GeneralHandler();
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame);
    }

    public void joinGame(@NotNull Context context) {
        context.result("JOINING");
    }

    public void createGame(@NotNull Context context) {
        context.result("CREATING");
    }

    public void listGames(@NotNull Context context) {
        context.result("LISTING");
    }

    public void logout(@NotNull Context context) {
        context.result("LOGGING OUT");
    }

    public void login(@NotNull Context context) {
        try{
            var result = handler.login(context.body());
            context.result(result);
        }catch(BadInputException e){
            context.status(400);
            context.result("{\"message\": \"Error: bad request\"}");
        }catch(NotFoundException e){
            context.status(404).result("{\"message\": \"Error: user not found\"}");
        }catch(IncorrectPasswordException e){
            context.status(401).result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public void register(@NotNull Context context) {
        try {
            var result = handler.register(context.body());
            context.result(result);
        }catch(BadInputException e){
            context.status(400);
            context.result("{\"message\": \"Error: bad request\"}");
        }catch(AlreadyTakenException e){
            context.status(403);
            context.result("{\"message\": \"Error: username already taken\"}");
        }
    }

    public void clear(@NotNull Context context) {
       try {
           handler.clear();
           context.status(200);
       } catch (Exception e) {
           context.status(500);
       }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

}
