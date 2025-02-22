package se.myhappyplants.server;

import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    private static final DatabaseConnectionHandler dbch = new DatabaseConnectionHandler();

    public static void main(String[] args) {

        // Create a Gson instance
        Gson gson = new Gson();

        // Create Javalin and configure a custom JsonMapper
        Javalin app = Javalin.create(config -> {
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

        }).start(7888);

        // Simple test endpoint
        app.get("/", ctx -> ctx.result("Hello World"));

        // Example of an endpoint that returns a list of Plants
        app.get("/search/{search_term}", ctx -> {
            List<Plant> plantList = dbch.databaseRequest(
                    new Message(MessageType.search, ctx.pathParam("search_term"))
            ).getPlantArray();

            if (plantList.isEmpty()) {
                ctx.status(404).result("No plants found");
            } else {
                ctx.json(plantList);
            }
        });
    }
}
