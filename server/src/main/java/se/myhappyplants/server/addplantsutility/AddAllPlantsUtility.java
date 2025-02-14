package se.myhappyplants.server.addplantsutility;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class AddAllPlantsUtility {
    PlantToAdd[] plantsToAdd;
    AddAllPlantsUtility() {
        Gson gson = new Gson();
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new FileReader("server/addplantsutility/plants.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        plantsToAdd = gson.fromJson(jsonReader, PlantToAdd[].class);
    }

    public void testFirst10() {
        for (int i = 0; i < 10; i++) {
            System.out.println(plantsToAdd[i].toString());
        }
    }

    public static void main(String[] args) {
        AddAllPlantsUtility test = new AddAllPlantsUtility();
        test.testFirst10();

    }
}
