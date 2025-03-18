package se.myhappyplants.server.addplantsutility;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// uses a new object, PlantToAdd, that accurately matches the parameters of
// the Json objects -- unlike Plant
public class AddAllPlantsUtility {
    PlantToAdd[] plantsToAdd;
    Dotenv dotenv = Dotenv.load();

    AddAllPlantsUtility() {
        Gson gson = new Gson();
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new FileReader("src/main/java/se/myhappyplants/server/addplantsutility/plants.json"));
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

    public void replaceNull() {
        for (PlantToAdd plantToAdd : plantsToAdd) {
            if (plantToAdd != null) {
                if (plantToAdd.scientific_name == null) {
                    plantToAdd.scientific_name = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    String[] split = plantToAdd.scientific_name.split(" ");
                    StringBuilder newString = new StringBuilder();
                    for (String string : split) {
                        char[] chars = string.toCharArray();
                        chars[0] = Character.toUpperCase(chars[0]);
                        newString.append(chars);
                        newString.append(' ');
                    }
                    if (!newString.isEmpty() && newString.charAt(newString.length() - 1) == ' ') {
                        newString.deleteCharAt(newString.length() - 1);
                    }
                    plantToAdd.scientific_name = newString.toString();
                }
                if (plantToAdd.family == null) {
                    plantToAdd.family = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    String[] split = plantToAdd.family.split(" ");
                    StringBuilder newString = new StringBuilder();
                    for (String string : split) {
                        char[] chars = string.toCharArray();
                        chars[0] = Character.toUpperCase(chars[0]);
                        newString.append(chars);
                        newString.append(' ');
                    }
                    if (!newString.isEmpty() && newString.charAt(newString.length() - 1) == ' ') {
                        newString.deleteCharAt(newString.length() - 1);
                    }
                    plantToAdd.family = newString.toString();
                }
                if (plantToAdd.common_name == null) {
                    plantToAdd.common_name = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    String[] split = plantToAdd.common_name.split(" ");
                    StringBuilder newString = new StringBuilder();
                    for (String string : split) {
                        char[] chars = string.toCharArray();
                        chars[0] = Character.toUpperCase(chars[0]);
                        newString.append(chars);
                        newString.append(' ');
                    }
                    if (!newString.isEmpty() && newString.charAt(newString.length() - 1) == ' ') {
                        newString.deleteCharAt(newString.length() - 1);
                    }
                    plantToAdd.common_name = newString.toString();
                }
                if (plantToAdd.light == null) {
                    plantToAdd.light = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.light.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.light = new String(chars);
                }
                if (plantToAdd.maintenance == null) {
                    plantToAdd.maintenance = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.maintenance.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.maintenance = new String(chars);
                }
            }
        }
    }

    public void addAllPlants() {
        for (PlantToAdd plant : plantsToAdd) {
            java.sql.Connection connection;
            try {
                connection = DriverManager.getConnection(dotenv.get("DB_SERVER_ADDRESS"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"));
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                return;
            }
            String query = """
                    INSERT INTO plants (id, common_name, family, scientific_name, image_url, light, maintenance, poisonous_to_pets, watering_frequency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, plant.id);
                preparedStatement.setString(2, plant.common_name);
                preparedStatement.setString(3, plant.family);
                preparedStatement.setString(4, plant.scientific_name);
                preparedStatement.setString(5, plant.image_url);
                preparedStatement.setString(6, plant.light);
                preparedStatement.setString(7, plant.maintenance);
                preparedStatement.setBoolean(8, plant.poisonous_to_pets);
                preparedStatement.setLong(9, plant.watering_frequency);
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        AddAllPlantsUtility test = new AddAllPlantsUtility();
        test.replaceNull();
        test.testFirst10();
        test.addAllPlants();
    }
}
