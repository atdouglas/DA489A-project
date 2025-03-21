package se.myhappyplants.server;

import com.google.gson.JsonArray;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import se.myhappyplants.server.repositories.PerenualRepository;
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
    private static final PerenualRepository perenualRepository = new PerenualRepository();

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
        setupPostUserLibrary();
        setupGetSecurityQuestion();
        setupPostSecurityQuestion();
        setupPostUpdatePassword();
        setUpDeleteFromUserLibrary();
        setupUpdateUserLibraryPlant();
        setupGetCareGuides();
        setupGetNotificationsActivated();
        setupUpdateNotificationsActivated();
    }

    private static void setupGetPlant() {
        app.get("/plants/{id}", ctx -> ctx.async(() -> {
            int plantID = Integer.parseInt(ctx.pathParam("id"));

            Plant plant = plantRepository.getPlantDetails(plantID);

            String description = perenualRepository.getDescription(plantID);

            if(!description.isBlank()){
                plant.setDescription(description);
            }

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

    private static void setUpDeleteFromUserLibrary(){
        app.delete("/library/{user_id}/{plant_id}", ctx -> ctx.async(() -> {
            int userID = Integer.parseInt(ctx.pathParam("user_id"));
            int plantID = Integer.parseInt(ctx.pathParam("plant_id"));

            System.out.println(userID);
            System.out.println(plantID);
            String token = ctx.queryParam("token");
            TokenStatus tokenStatus = userRepository.verifyAccessToken(userID, token);
            if (tokenStatus == TokenStatus.NO_MATCH) {
                ctx.status(401).result("401 You are unauthorized to access this data.");
            } else if (tokenStatus == TokenStatus.EXPIRED) {
                ctx.status(419).result("419 Your token has expired.");
            } else if (tokenStatus == TokenStatus.VALID){
                boolean result = userPlantRepository.deletePlant(userID,plantID);
                ctx.status(200).json(result);
            }else {
                ctx.status(404).result("An error has occurred.");
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

    private static void setupGetSecurityQuestion(){
        app.get("/security_question", ctx -> ctx.async(() -> {
            String email = ctx.queryParam("email");            String question = null;

            if(email == null){
                ctx.status(400).result("Bad request. No email provided.");
            }else {
                question = userRepository.getSecurityQuestion(email);
            }

            if(question == null || question.isBlank()){
                ctx.status(404).result("Security question could not be found");
            } else {
                ctx.status(200).json(question);
            }
        }));
    }

    private static void setupPostSecurityQuestion(){
        app.post("/security_question", ctx -> ctx.async(() -> {
            User user = ctx.bodyAsClass(User.class);
            boolean verified = false;

            if(user.getEmail() == null){
                ctx.status(400).result("Bad request. No email provided.");
                return;
            }else if(user.getSecurityAnswer() == null){
                ctx.status(400).result("Bad request. No answer provided.");
                return;
            }else {
                verified = userRepository.verifySecurityQuestion(user.getEmail(), user.getSecurityAnswer());
            }

            if(!verified){
                ctx.status(401).result("Wrong security answer.");
            }else {
                ctx.status(200).result("Correct answer, password may now be updated.");
            }
        }));
    }

    private static void setupPostUpdatePassword(){
        app.post("/update_password", ctx -> ctx.async(() -> {
            User user = ctx.bodyAsClass(User.class);
            boolean updated = false;

            if(user.getEmail() == null){
                ctx.status(400).result("Bad request. No email provided.");
                return;
            }else if(user.getSecurityAnswer() == null) {
                ctx.status(400).result("Bad request. No answer provided.");
                return;
            }else if(user.getPassword() == null){
                ctx.status(400).result("Bad request. No password provided.");

            }else {
                updated = userRepository.updatePasswordWithSecurityQuestion(user.getEmail(), user.getSecurityAnswer(), user.getPassword());
            }

            if(!updated){
                ctx.status(401).result("Wrong security answer.");
            }else {
                ctx.status(200).result("Correct answer, password updated.");
            }
        }));
    }

    private static void setupPostUserLibrary(){
        app.post("/library/{user_id}", ctx -> ctx.async(() -> {
            int userID = Integer.parseInt(ctx.pathParam("user_id"));
            String token = ctx.queryParam("token");
            String plantIDString = ctx.queryParam("plantID");
            String nickname = ctx.queryParam("nickname");

            TokenStatus tokenStatus = userRepository.verifyAccessToken(userID, token);

            if (tokenStatus == TokenStatus.NO_MATCH) {
                ctx.status(401).result("401 You are unauthorized to access this data.");
            } else if (tokenStatus == TokenStatus.EXPIRED) {
                ctx.status(419).result("419 Your token has expired.");
            } else if(plantIDString == null || plantIDString.isBlank()) {
                ctx.status(400).result("Plant ID can't be empty.");

            } else if(nickname == null || nickname.isBlank()) {
                ctx.status(400).result("Nickname can't be empty.");

            } else if (tokenStatus == TokenStatus.VALID){
                int plantID = Integer.parseInt(plantIDString);


                userPlantRepository.savePlant(new User(token, userID), createUserPlant(plantID, nickname));

                ctx.status(200).result("Plant added to user library.");
            }else {
                ctx.status(404).result("An error has occurred.");
            }
        }));
    }

    private static void setupUpdateUserLibraryPlant(){
        app.patch("/library/{user_id}/{user_plant_id}", ctx -> ctx.async(() -> {
            int userID = Integer.parseInt(ctx.pathParam("user_id"));
            int userPlantID = Integer.parseInt(ctx.pathParam("user_plant_id"));
            String token = ctx.queryParam("token");
            String nickname = ctx.queryParam("nickname");
            String lastWateredString = ctx.queryParam("last_watered");

            TokenStatus tokenStatus = userRepository.verifyAccessToken(userID, token);

            if (tokenStatus == TokenStatus.NO_MATCH) {
                ctx.status(401).result("401 You are unauthorized to access this data.");
            } else if (tokenStatus == TokenStatus.EXPIRED) {
                ctx.status(419).result("419 Your token has expired.");
            } else if (tokenStatus == TokenStatus.VALID){

                if(nickname != null && !nickname.isBlank()){
                    userPlantRepository.changePlantNickname(userID, userPlantID, nickname);
                    ctx.status(200).result("Nickname updated.");

                } else if (lastWateredString != null && !lastWateredString.isBlank()) {
                    long lastWatered = Long.parseLong(lastWateredString);
                    userPlantRepository.changeLastWatered(userID, userPlantID, lastWatered);
                    ctx.status(200).result("Last watered changed.");

                }else{
                    ctx.status(400).result("Both nickname and last_watered can't be empty.");
                }
            }else {
                ctx.status(404).result("An error has occurred.");
            }

        }));
    }

    public static void setupGetCareGuides(){
        app.get("/plants/{id}/guides", ctx -> ctx.async(() -> {
            int plantID = Integer.parseInt(ctx.pathParam("id"));

            JsonArray guides = perenualRepository.getPlantGuides(plantID);

            if (guides == null || guides.isEmpty()) {
                ctx.status(404).result("No guides for this plant was found.");
            } else {
                ctx.status(200).json(guides);
            }
        }));
    }

    public static void setupGetNotificationsActivated(){
        app.get("/notif/{user_id}", ctx -> ctx.async(() -> {
            int userID = Integer.parseInt(ctx.pathParam("user_id"));
            String token = ctx.queryParam("token");

            TokenStatus tokenStatus = userRepository.verifyAccessToken(userID, token);

            if (tokenStatus == TokenStatus.NO_MATCH){
                ctx.status(401).result("401 You are unauthorized to access this data.");
            }else if (tokenStatus == TokenStatus.EXPIRED){
                ctx.status(419).result("419 Your token has expired.");
            } else if (tokenStatus == TokenStatus.VALID) {
                String email = userRepository.getEmailByUserID(userID);
                User user = userRepository.getUserDetails(email);
                if (user != null){
                    ctx.json(user.areNotificationsActivated()).status(200);
                }else{
                    ctx.status(400).result("User not found");
                }
            }else {
                ctx.status(500).result("Internal server error");
            }
        }));
    }

    public static void setupUpdateNotificationsActivated(){
        app.patch("/notif/{user_id}", ctx -> ctx.async(() -> {
            int userID = Integer.parseInt(ctx.pathParam("user_id"));
            String token = ctx.queryParam("token");
            String flag =  ctx.queryParam("flag");

            if (flag == null){
                ctx.status(400).result("Missing flag");
                return;
            }
            boolean booli = Boolean.parseBoolean(flag);
            TokenStatus tokenStatus = userRepository.verifyAccessToken(userID, token);

            if (tokenStatus == TokenStatus.NO_MATCH){
                ctx.status(401).result("401 You are unauthorized to access this data.");
            }else if (tokenStatus == TokenStatus.EXPIRED){
                ctx.status(419).result("419 Your token has expired.");
            } else if (tokenStatus == TokenStatus.VALID) {
                String email = userRepository.getEmailByUserID(userID);
                if (email == null){
                    ctx.status(400).result("User not found");
                    return;
                }
                boolean success = userRepository.changeNotifications(email, booli);
                if (success){
                    ctx.status(200).result("Notifications updated");
                }else {
                    ctx.status(400).result("Updated failed");
                }
            }else {
                ctx.status(500).result("Internal server error");
            }

        }));
    }

    private static UserPlant createUserPlant(int plantID, String nickname){
        Plant plant = plantRepository.getPlantDetails(plantID);

        return new UserPlant(
                plant.getId(),
                plant.getScientific_name(),
                plant.getFamily(),
                plant.getCommon_name(),
                plant.getImage_url(),
                plant.getLight(),
                plant.getMaintenance(),
                plant.getIsPoisonoutToPets(),
                plant.getWaterFrequency(),
                nickname,
                System.currentTimeMillis());
    }


    private static Javalin getApp() {
        return Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors ->{
                cors.addRule(corsConfig -> {
                    corsConfig.anyHost();
                });
            });
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


