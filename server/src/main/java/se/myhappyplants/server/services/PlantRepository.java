package se.myhappyplants.server.services;

import se.myhappyplants.shared.WaterCalculator;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.PlantDetails;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about plants.
 * Created by: Frida Jacobsson 2021-03-30
 * Updated by: Christopher O'Driscoll
 */
public class PlantRepository {

    private DatabaseConnection connection;

    public PlantRepository(DatabaseConnection connection) {
        this.connection = connection;
    }

    // TODO: adjust to new implementation
    public ArrayList<Plant> getResult(String plantSearch) {
        ArrayList<Plant> plantList = new ArrayList<>();
        String query = """
        SELECT id, common_name, scientific_name, family, image_url FROM plants WHERE scientific_name LIKE ? OR common_name LIKE ? OR family LIKE ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, plantSearch);
            preparedStatement.setString(2, plantSearch);
            preparedStatement.setString(3, plantSearch);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int plantId = resultSet.getInt("id");
                String commonName = resultSet.getString("common_name");
                String scientificName = resultSet.getString("scientific_name");
                String familyName = resultSet.getString("family");
                String imageURL = resultSet.getString("image_url");
                String maintenance = resultSet.getString("maintenance");
                long wateringFrequency = resultSet.getLong("watering_frequency");
                String poisonousToPets = resultSet.getString("poisonous_to_pets");
                String light = resultSet.getString("light");
                plantList.add(new Plant(plantId, commonName, scientificName, familyName, imageURL,
                        maintenance, light, wateringFrequency, poisonousToPets));
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            plantList = null;
        } finally {
            connection.closeConnection();
        }
        return plantList;
    }

    // TODO: adjust to new implementation. likely remove
    public PlantDetails getPlantDetails(Plant plant) {
        PlantDetails plantDetails = null;
        String query = """
        SELECT scientific_name, light, water_frequency, family FROM species WHERE id = ?; 
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, plant.getPlantId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String scientificName = resultSet.getString("scientific_name");
                String lightText = resultSet.getString("light");
                long waterLong = resultSet.getLong("watering_frequency");
                String family = resultSet.getString("family");

                int light = (isNumeric(lightText)) ? Integer.parseInt(lightText) : -1;
                int water = (int) waterLong;

                plantDetails = new PlantDetails(scientificName, light, water, family);
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return plantDetails;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
