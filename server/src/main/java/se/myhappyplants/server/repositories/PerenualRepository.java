package se.myhappyplants.server.repositories;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PerenualRepository {
    private Dotenv dotenv = Dotenv.load();
    String description = "";

    public String getDescription(int plantID){
        if (!checkValidID(plantID)){
            return null;
        }
        String urlString = "https://perenual.com/api/v2/species/details/" + plantID
                + "?key=" + dotenv.get("PERENUAL_KEY");

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            description = jsonObject.get("description").getAsString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return description;
    }

    public JsonArray getPlantGuides(int plantID){
        if (!checkValidID(plantID)){
            return null;
        }
        String urlString = "https://perenual.com/api/species-care-guide-list"
                + "?key=" + dotenv.get("PERENUAL_KEY")
                + "&species_id=" + plantID;


        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            if(jsonObject == null){
                return null;
            }

            return extractCareGuides(jsonObject);



        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkValidID(int plantID) {
        return plantID >= 1 && plantID <= 3000;
    }

    private JsonArray extractCareGuides(JsonObject data){
        JsonArray guides = new JsonArray();

        JsonArray sectionArray = data
                .getAsJsonArray("data")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("section");

        for (int i = 0; i < sectionArray.size(); i++) {
            JsonObject section = sectionArray.get(i).getAsJsonObject();
            JsonObject guide = new JsonObject();
            guide.addProperty("type", section.get("type").getAsString());
            guide.addProperty("description", section.get("description").getAsString());
            guides.add(guide);
        }

        return guides;
    }
}


