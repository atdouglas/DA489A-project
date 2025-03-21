package se.myhappyplants.server.repositories;

import se.myhappyplants.shared.User;
import se.myhappyplants.shared.UserPlant;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about a users library.
 * Created by: Linn Borgström
 * Updated by: Frida Jacobsson 2021-05-21
 */
// TODO: fix this whole thing...
public class UserPlantRepository extends Repository {

    //TODO Update this class to work on the new implementation.

    public boolean savePlant(User user, UserPlant userPlant) {
        if (userPlant == null) {
            return false;
        }
        if (!checkPlantNickname(userPlant.getNickname())) {
            return false;
        }
        boolean success = false;

        String query = """
                INSERT INTO user_plants (user_id, nickname,last_watered, plant_id, image_url) VALUES (?, ?, ?, ?, ?);
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getUniqueId());
                preparedStatement.setString(2, userPlant.getNickname());
                preparedStatement.setLong(3, userPlant.getLastWatered());
                preparedStatement.setInt(4, userPlant.getId());
                preparedStatement.setString(5, userPlant.getImage_url());
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return success;
    }


    //TODO Update this method to work on the new implementation


    public ArrayList<UserPlant> getUserLibrary(int userId) {
        ArrayList<UserPlant> plantList = new ArrayList<UserPlant>();
        String query = """
                SELECT p.*, up.nickname, up.last_watered, up.id as user_plant_id FROM plants p 
                JOIN user_plants up ON p.id = up.plant_id WHERE up.user_id = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int plantId = resultSet.getInt("id");
                    String scientificName = resultSet.getString("scientific_name");
                    String family = resultSet.getString("family");
                    String commonName = resultSet.getString("common_name");
                    String imageURL = resultSet.getString("image_url");
                    String light = resultSet.getString("light");
                    String maintenance = resultSet.getString("maintenance");
                    boolean poisonousToPets = resultSet.getBoolean("poisonous_to_pets");
                    long waterFrequency = resultSet.getLong("watering_frequency");
                    String nickname = resultSet.getString("nickname");
                    long lastWatered = resultSet.getLong("last_watered");
                    int userPlantId = resultSet.getInt("user_plant_id");

                    plantList.add(new UserPlant(plantId, scientificName, family, commonName, imageURL, light, maintenance, poisonousToPets, waterFrequency, nickname, lastWatered, userPlantId));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantList;
    }

    public UserPlant getUserPlant(int userId, int userPlantId) {
        UserPlant userPlant = null;
        String query = """
                SELECT p.*, up.nickname, up.last_watered, up.id as user_plant_id FROM plants p 
                JOIN user_plants up ON p.id = up.plant_id WHERE up.id = ? AND up.user_id = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userPlantId);
                preparedStatement.setInt(2, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int plantId = resultSet.getInt("id");
                    String scientificName = resultSet.getString("scientific_name");
                    String family = resultSet.getString("family");
                    String commonName = resultSet.getString("common_name");
                    String imageURL = resultSet.getString("image_url");
                    String light = resultSet.getString("light");
                    String maintenance = resultSet.getString("maintenance");
                    boolean poisonousToPets = resultSet.getBoolean("poisonous_to_pets");
                    long wateringFrequency = resultSet.getLong("watering_frequency");
                    String nickname = resultSet.getString("nickname");
                    long lastWatered = resultSet.getLong("last_watered");
                    userPlant = new UserPlant(plantId, scientificName, family, commonName, imageURL, light, maintenance, poisonousToPets, wateringFrequency, nickname, lastWatered, userPlantId);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return userPlant;
    }

    public boolean deletePlant(int userId, int userPlantId) {
        boolean plantDeleted = false;

        String query = """
                        DELETE FROM user_plants WHERE id = ? AND user_id = ?;
                """;
        if (userPlantId <= 0) {
            return plantDeleted;
        }
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userPlantId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                plantDeleted = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantDeleted;
    }

    //TODO: current unimplemented

    public boolean changeLastWatered(int userId, int plant_id, long lastWatered) {
        boolean dateChanged = false;
        String query = """ 
                    UPDATE user_plants SET last_watered = ? WHERE id = ? AND user_id = ?;
                    """;
        if (userId <= 0 || plant_id <= 0 || lastWatered < 0) {
            return dateChanged;
        }
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, lastWatered);
                preparedStatement.setInt(2, plant_id);
                preparedStatement.setInt(3, userId);
                preparedStatement.executeUpdate();
                dateChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return dateChanged;
    }

    private boolean checkPlantNickname(String plantNickname) {
        if (plantNickname == null || plantNickname.isBlank()) {
            return false;
        }
        if (plantNickname.length() < 3 || plantNickname.length() > 50) {
            return false;
        }
        return true;
    }

    public boolean changePlantNickname(int userId, int plantId, String newNickname) {
        if (!checkPlantNickname(newNickname)) {
            return false;
        }
        boolean nicknameChanged = false;
        String query = """
                    UPDATE user_plants 
                    SET nickname = ? 
                    WHERE id = ? AND user_id = ?;
                   """;
        if (userId <= 0 || plantId <= 0) {
            return nicknameChanged;
        }
        try (Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newNickname);
                preparedStatement.setInt(2, plantId);
                preparedStatement.setInt(3, userId);
                preparedStatement.executeUpdate();
                nicknameChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return nicknameChanged;
    }

        // TODO: currently unimplemented

    public boolean changeAllToWatered(int userId) {
        boolean dateChanged = false;
        String query = """
                    UPDATE user_plants 
                    SET last_watered = ? 
                    WHERE user_id = ?;
                   """;
        if (userId <= 0) {
            return dateChanged;
        }
        try (Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, System.currentTimeMillis());
                preparedStatement.setInt(2, userId);
                dateChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return dateChanged;
    }
}