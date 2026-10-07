package server;

import handler.GeneralHandler;
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
        context.result("LOGGING IN");
    }

    public void register(@NotNull Context context) {
        context.result("REGISTERING");
    }

    public void clear(@NotNull Context context) {
        handler.clear();
        context.result("CLEARING");
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

}
