package se.myhappyplants.server;

import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    private static final PlantRepository plantRepository = new PlantRepository();
    private static final UserPlantRepository userPlantRepository = new UserPlantRepository();
    private static final UserRepository userRepository = new UserRepository();

    private static final Gson gson = new Gson();
    private static Javalin app;


    public static void main(String[] args) {
        app = getApp();

        app.start(7888);

        app.get("/", ctx -> ctx.result("Plantopedia API"));

        setupGetPlant();
        setupSearch();
        setupRegister();
        setupLogin();
        setupGetUserLibrary();
    }

    private static void setupGetPlant() {
        app.get("/plants/{id}", ctx -> ctx.async(() -> {
            int plantID = Integer.parseInt(ctx.pathParam("id"));

            Plant plant = plantRepository.getPlantDetails(plantID);

            if (plant == null) {
                ctx.status(404).result("No plant with this id was found.");
            } else {
                ctx.status(200).json(plant);
            }
        }));
    }

    private static void setupSearch() {
        app.get("/search/{search_term}", ctx -> ctx.async(() -> {
          List<Plant> plantList = plantRepository.getResult(ctx.pathParam("search_term"));

            if (plantList.isEmpty()) {
                ctx.status(404).result("No plants found");
            } else {
                ctx.status(200).json(plantList);
            }
        }));
    }

    private static void setupLogin() {
        app.post("/login", ctx -> ctx.async(() -> {
            User user = ctx.bodyAsClass(User.class);
            String email = user.getEmail();
            String password = user.getPassword();

            boolean success = userRepository.checkLogin(email, password);
            user = userRepository.getUserDetails(user.getEmail());

            if (!success) {
                ctx.status(404).result("Login error. User not found.");
            } else {
                user.setPassword(password);
                user.setAccessToken(userRepository.getNewAccessToken(user.getEmail(), user.getPassword()));
                ctx.status(200).json(user);
            }
        }));
    }

    private static void setupRegister() {
        app.post("/register", ctx -> ctx.async(() -> {
            User newUser = ctx.bodyAsClass(User.class);
            boolean success = userRepository.saveUser(newUser);

            if (!success) {
                ctx.status(404).result("There was an error adding the user.");
            } else {
                ctx.status(200).result("User registered.");
            }
        }));
    }

    private static void setupGetUserLibrary(){
        app.get("/library/{user_id}", ctx -> ctx.async(() -> {
            int userID = Integer.parseInt(ctx.pathParam("user_id"));
            String token = ctx.queryParam("token");

            TokenStatus tokenStatus = userRepository.verifyAccessToken(userID, token);


            if (tokenStatus == TokenStatus.NO_MATCH) {
                ctx.status(401).result("401 You are unauthorized to access this data.");
            } else if (tokenStatus == TokenStatus.EXPIRED) {
                ctx.status(419).result("419 Your token has expired.");
            } else if (tokenStatus == TokenStatus.VALID){
                List<UserPlant> plantList = userPlantRepository.getUserLibrary(userID);
                ctx.status(200).json(plantList);
            }else {
                ctx.status(404).result("An error has occurred.");
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


