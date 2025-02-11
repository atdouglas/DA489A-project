package se.myhappyplants.server.services;

import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;
import se.myhappyplants.shared.WaterCalculator;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about a users library.
 * Created by: Linn Borgström
 * Updated by: Frida Jacobsson 2021-05-21
 */
// TODO: fix this whole thing...
public class UserPlantRepository {
    
    private DatabaseConnection connection;

    /**
     * Constructor that creates a connection to the database.
     *
     * @throws SQLException
     * @throws UnknownHostException
     */
    public UserPlantRepository(DatabaseConnection connection) {
        this.connection = connection;

    }

    /**
     * Method to save a new plant in database
     * Author: Frida Jacobsson
     * Updated Frida Jacobsson 2021-04-29
     *
     * @param plant an instance of a newly created plant by user
     * @return a boolean value, true if the plant was stored successfully
     */

    public boolean savePlant(User user, Plant plant) {
        boolean success = false;
        String query = """
        INSERT INTO user_plants (user_id, nickname, plant_id, last_watered, image_url) VALUES (?, ?, ?, ?, ?);
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, user.getUniqueId());
            preparedStatement.setString(2, plant.getNickname());
            preparedStatement.setInt(3, plant.getPlantId());
            preparedStatement.setDate(4, plant.getLastWatered());
            preparedStatement.setString(5, plant.getImageURL());
            success = true;
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return success;
    }

    /**
     * Method that returns all the plants connected to the logged in user.
     * Author: Linn Borgström,
     * Updated by: Frida Jacobsson
     *
     * @return an arraylist if plants stored in the database
     */
    public ArrayList<Plant> getUserLibrary(int userId) {
        ArrayList<Plant> plantList = new ArrayList<Plant>();
        String query = """
        SELECT nickname, plant_id, last_watered, image_url FROM user_plants WHERE user_id = ?
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nickname = resultSet.getString("nickname");
                int plantId = resultSet.getInt("plant_id");
                Date lastWatered = resultSet.getDate("last_watered");
                String imageURL = resultSet.getString("image_url");
                long waterFrequency = getWaterFrequency(plantId);
                plantList.add(new Plant(nickname, plantId, lastWatered, waterFrequency, imageURL));
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantList;
    }

    /**
     * Method that returns one specific plant based on nickname.
     *
     * @param nickname
     * @return an instance of a specific plant from the database, null if no plant with the specific nickname exists
     */
    public Plant getPlant(User user, String nickname) {
        Plant plant = null;
        String sqlSafeNickname = nickname.replace("'", "''");
        String query = "SELECT nickname, plant_id, last_watered, image_url FROM \"Plant\" WHERE user_id =" + user.getUniqueId() + "AND nickname = '" + sqlSafeNickname + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            String plantId = resultSet.getString("plant_id");
            Date lastWatered = resultSet.getDate("last_watered");
            String imageURL = resultSet.getString("image_url");
            long waterFrequency = plantRepository.getWaterFrequency(plantId);
            plant = new Plant(nickname, plantId, lastWatered, waterFrequency, imageURL);
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plant;
    }

    /**
     * Method that makes a query to delete a specific plant from table Plant
     *
     * @param user     the user that owns the plant
     * @param nickname nickname of the plant
     * @return boolean result depending on the result, false if exception
     */
    public boolean deletePlant(User user, String nickname) {
        boolean plantDeleted = false;
        String sqlSafeNickname = nickname.replace("'", "''");
        String query = "DELETE FROM plant WHERE user_id =" + user.getUniqueId() + "AND nickname = '" + sqlSafeNickname + "';";
        try {
            database.executeUpdate(query);
            plantDeleted = true;
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
        return plantDeleted;
    }

    /**
     * Method that makes a query to change the last watered date of a specific plant in table Plant
     *
     * @param user     the user that owns the plant
     * @param nickname nickname of the plant
     * @param date     new data to change to
     * @return boolean result depending on the result, false if exception
     */
    public boolean changeLastWatered(User user, String nickname, LocalDate date) {
        boolean dateChanged = false;
        String sqlSafeNickname = nickname.replace("'", "''");
        String query = "UPDATE plant SET last_watered = '" + date + "' WHERE user_id = " + user.getUniqueId() + " AND nickname = '" + sqlSafeNickname + "';";
        try {
            database.executeUpdate(query);
            dateChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dateChanged;
    }

    public boolean changeNickname(User user, String nickname, String newNickname) {
        boolean nicknameChanged = false;
        String sqlSafeNickname = nickname.replace("'", "''");
        String sqlSafeNewNickname = newNickname.replace("'", "''");
        String query = "UPDATE plant SET nickname = '" + sqlSafeNewNickname + "' WHERE user_id = " + user.getUniqueId() + " AND nickname = '" + sqlSafeNickname + "';";
        try {
            database.executeUpdate(query);
            nicknameChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return nicknameChanged;
    }

    public boolean changeAllToWatered(User user) {
        boolean dateChanged = false;
        LocalDate date = java.time.LocalDate.now();
        String query = "UPDATE plant SET last_watered = '" + date + "' WHERE user_id = " + user.getUniqueId() + ";";
        try {
            database.executeUpdate(query);
            dateChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dateChanged;
    }

    public boolean changePlantPicture(User user, Plant plant) {
        boolean pictureChanged = false;
        String nickname = plant.getNickname();
        String sqlSafeNickname = nickname.replace("'", "''");
        String query = "UPDATE plant SET image_url = '" + plant.getImageURL() + "' WHERE user_id = " + user.getUniqueId() + " AND nickname = '" + sqlSafeNickname + "';";
        try {
            database.executeUpdate(query);
            pictureChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return pictureChanged;
    }

    // TODO: adjust to new implementation
    public long getWaterFrequency(int plantId) {
        long waterFrequency = -1;
        String query = """
        SELECT water_frequency FROM species WHERE id = ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, plantId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long waterLong = resultSet.getLong("water_frequency");
                int water = (int) waterLong;
                waterFrequency = WaterCalculator.calculateWaterFrequencyForWatering(water);
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return waterFrequency;
    }
}