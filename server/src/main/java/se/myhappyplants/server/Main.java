package se.myhappyplants.server;

import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    private static final DatabaseConnectionHandler dbch = new DatabaseConnectionHandler();
    private static final Gson gson = new Gson();
    private static Javalin app;


    public static void main(String[] args) {
        app = getApp();

        app.start(7888);

        app.get("/", ctx -> ctx.result("Plantopedia API"));

        setUpGetPlant();
        setUpSearch();
        setupRegister();
    }

    private static void setUpGetPlant() {
        app.get("/plants/{id}", ctx -> ctx.async(() -> {
            Plant plant = dbch.databaseRequest(new Message(MessageType.getPlant, ctx.pathParam("id"))).getPlant();

            if (plant == null) {
                ctx.status(404).result("No plant with this id was found.");
            } else {
                ctx.status(200).json(plant);
            }
        }));
    }

    private static void setUpSearch() {
        app.get("/search/{search_term}", ctx -> ctx.async(() -> {
                            List<Plant> plantList = dbch.databaseRequest(
                                    new Message(MessageType.search, ctx.pathParam("search_term"))
                            ).getPlantArray();

                            if (plantList.isEmpty()) {
                                ctx.status(404).result("No plants found");
                            } else {
                                ctx.status(200).json(plantList);
                            }
                        }));
    }

    private static void setupRegister() {
        app.post("/register", ctx -> ctx.async(() -> {
                    User newUser = ctx.bodyAsClass(User.class);
                    boolean success = dbch.databaseRequest(new Message(MessageType.register, newUser)).isSuccess();

                    if (!success) {
                        ctx.status(404).result("There was an error adding the user.");
                    } else {
                        ctx.status(200).result("User registered.");
                    }
                }));
    }

    private static Javalin getApp() {
        return Javalin.create(config -> {
            config.jsonMapper(new JsonMapper() {
                @NotNull
                @Override
                public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                    return gson.toJson(obj, type);
                }

                @NotNull
                @Override
                public InputStream toJsonStream(@NotNull Object obj, @NotNull Type type) {
                    return new ByteArrayInputStream(gson.toJson(obj, type).getBytes(StandardCharsets.UTF_8));
                }

                @NotNull
                @Override
                public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                    return gson.fromJson(json, targetType);
                }

                @NotNull
                @Override
                public <T> T fromJsonStream(@NotNull InputStream json, @NotNull Type targetType) {
                    return gson.fromJson(new InputStreamReader(json, StandardCharsets.UTF_8), targetType);
                }
            });
            // 10 sec
            config.http.asyncTimeout = 10000;
            // TODO: figure out appropriate size
            config.http.maxRequestSize = 10000;
        });
    }
}


