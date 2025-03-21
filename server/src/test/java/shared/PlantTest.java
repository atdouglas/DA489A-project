package shared;
import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.Plant;

import static org.junit.jupiter.api.Assertions.*;



public class PlantTest {


    @Test
    void testDefaultConstructor() {
        Plant plant = new Plant();
        assertNotNull(plant, "Default constructor should create a non-null Plant object.");
    }

    @Test
    void testSetImageUrl() {
        Plant plant = new Plant();
        plant.setImage_url("https://example.com/new_image.jpg");
        assertEquals("http://example.com/new_image.jpg", plant.getImage_url(), "Image URL should be updated and HTTPS replaced with HTTP.");
    }



    @Test
    void testGetImageUrlHttpsReplacement() {
        Plant plant = new Plant();
        plant.setImage_url("https://example.com/image.jpg");
        assertEquals("http://example.com/image.jpg", plant.getImage_url(), "HTTPS should be replaced with HTTP.");
    }

    @Test
    void testToString() {
        Plant plant = new Plant(
                123,
                "Scientific Name",
                "Family",
                "Common Name",
                "https://example.com/image.jpg",
                "Full sun",
                "Low",
                true,
                86400000L
        );

        String expectedToString = "{\n" +
                "id: 123,\n" +
                "scientific_name: Scientific Name,\n" +
                "family: Family,\n" +
                "common_name: Common Name,\n" +
                "image_url: https://example.com/image.jpg,\n" +
                "light: Full sun,\n" +
                "maintenance: Low,\n" +
                "poisonous_to_pets: true,\n" +
                "watering_frequency: 86400000\n" +
                "},\n";

        assertEquals(expectedToString, plant.toString(), "toString() should generate the expected JSON-like string.");
    }

    @Test
    void testSetDescription() {
        Plant plant = new Plant();
        plant.setDescription("This is a test description.");
        assertEquals("This is a test description.", plant.getDescription(), "Description should be updated.");
    }

    @Test
    void testGetWaterFrequency() {
        Plant plant = new Plant();
        plant.setWatering_frequency(172800000L); // 2 days in milliseconds
        assertEquals(172800000L, plant.getWaterFrequency(), "Watering frequency should be 172800000 ms.");
    }


    @Test
    void testGetWaterFrequencyNegative() {
        Plant plant = new Plant();
        plant.setWatering_frequency(-86400000L); // Negative value
        assertEquals(-86400000L, plant.getWaterFrequency(), "Watering frequency should handle negative values.");
    }

    @Test
    void testGetWaterFrequencyZero() {
        Plant plant = new Plant();
        plant.setWatering_frequency(0L); // Zero value
        assertEquals(0L, plant.getWaterFrequency(), "Watering frequency should handle zero values.");
    }
    @Test
    void testSetDescriptionNull() {
        Plant plant = new Plant();
        plant.setDescription(null);
        assertNull(plant.getDescription(), "Description should be null when set to null.");
    }

    @Test
    void testSetDescriptionEmpty() {
        Plant plant = new Plant();
        plant.setDescription("");
        assertEquals("", plant.getDescription(), "Description should be an empty string when set to empty.");
    }

    @Test
    void testGetIsPoisonoutToPetsFalse() {
        Plant plant = new Plant();
        plant.setPoisonous_to_pets(false);
        assertFalse(plant.getIsPoisonoutToPets(), "poisonous_to_pets should be false when explicitly set.");
    }

    @Test
    void testSetPoisonousToPetsTrue() {
        Plant plant = new Plant();
        plant.setPoisonous_to_pets(true);
        assertTrue(plant.getIsPoisonoutToPets(), "poisonous_to_pets should be true when set to true.");
    }

    @Test
    void testSetPoisonousToPetsFalse() {
        Plant plant = new Plant();
        plant.setPoisonous_to_pets(false);
        assertFalse(plant.getIsPoisonoutToPets(), "poisonous_to_pets should be false when set to false.");
    }


}