package se.myhappyplants.server;

import io.javalin.Javalin;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;

import java.util.List;

public class Main {
    private static final DatabaseConnectionHandler dbch = new DatabaseConnectionHandler();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get("/", ctx -> ctx.result("Hello World"));


        // search endpoint TODO: needs to do task asynchronously and needs smarter json conversion
        app.get("/search/{search_term}", ctx -> {
                    List<Plant> plantList = dbch.databaseRequest(new Message(MessageType.search,
                            ctx.pathParam("search_term"))).getPlantArray();
                    StringBuilder result = new StringBuilder();
                    if (!plantList.isEmpty()) {
                        for (Plant plant : plantList) {
                            result.append(plant.toString()).append("\n");
                        }
                        ctx.json(result.toString());
                    } else {
                        ctx.result("No plants found");
                    }
                }
        );
    }
}
