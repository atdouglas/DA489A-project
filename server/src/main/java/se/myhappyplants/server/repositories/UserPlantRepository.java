package se.myhappyplants.server.repositories;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;
import se.myhappyplants.shared.UserPlant;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
                SELECT p.*, up.nickname, up.last_watered FROM plants p JOIN user_plants up ON p.id = up.plant_id WHERE up.user_id = ?
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
                    long waterFrequency = resultSet.getLong("water_frequency");
                    String nickname = resultSet.getString("nickname");
                    long lastWatered = resultSet.getLong("last_watered");
                    plantList.add(new UserPlant(plantId, scientificName, family, commonName, imageURL, light, maintenance, poisonousToPets, waterFrequency, nickname, lastWatered));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantList;
    }

    //TODO Update this method to work on the new implementation

    public UserPlant getUserPlant(int userId, int UniquePlantId) {
        UserPlant userPlant = null;
        String query = """
                        SELECT p.*, up.nickname, up.last_watered FROM plants p JOIN user_plants up ON p.id = up.plant_id WHERE up.id = ? AND up.user_id = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, UniquePlantId);
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
                    long waterFrequency = resultSet.getLong("water_frequency");
                    String nickname = resultSet.getString("nickname");
                    long lastWatered = resultSet.getLong("last_watered");
                    userPlant = new UserPlant(plantId, scientificName, family, commonName, imageURL, light, maintenance, poisonousToPets, waterFrequency, nickname, lastWatered);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return userPlant;
    }

    //TODO Update this method to work on the new implementation

    /*
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

    */

    //TODO Update this method to work on the new implementation


    /*
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

     */

    //TODO Update this method to work on the new implementation
    /*
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

        // TODO: adjust to new implementation
    */
    /*
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
    /*

    //TODO The remaining methods under here will probably get deleted but let the code stay at the moment.

    /*

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
     */

    /*
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
     */
}