package server;

import com.google.gson.JsonArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.myhappyplants.server.repositories.PerenualRepository;
import se.myhappyplants.shared.Plant;

import static org.junit.jupiter.api.Assertions.*;


/**
 * WARNING: These tests consume a limited amount of API calls.
 * Additionally, cannot be guaranteed to behave as expected when/if the API is down.
 * The test has been designed to (ideally) only use 1 API call per method every time the test is run.
 */
public class PerenualRepositoryTest {

    private final PerenualRepository perenualRepository = new PerenualRepository();
    private Plant testPlant;

    @BeforeEach
    public void setup() {
        testPlant = new Plant(2895, "Eurybia Divaricata", "Asteraceae", "White Wood Aster",
                "https://perenual.com/storage/species_image/2895_eurybia_divaricata/og/7515068602_0626782d24_b.jpg",
                "Part shade", "Low", false, 0);
    }

    @Test
    public void testGetDescription() {
        String description = perenualRepository.getDescription(testPlant.getId());

        assertAll(
                () -> assertNotNull(description, "Description should not be null"),
                () -> assertFalse(description.isEmpty(), "Description should not be empty"),
                () -> assertEquals("The white wood aster (Eurybia divaricata) is an amazing plant species for many reasons. Its tall, upright stems are adorned with small, white star-like flowers with yellow centers. It has a showy fragrant blossom that can add an air of sophistication to the garden. Its leaves are lobed and toothed, making a wonderful background to the brightness of the flowers. Its blooms attracts butterflies and other pollinators. Finally, the white wood aster is incredibly hardy and can thrive in a variety of conditions making it a great choice for landscaping.",
                        description, "Description should match the description of the given plant."),
                () -> assertNotEquals("hi", description, "Description should not match an arbitrary string")
        );
    }

    @Test
    public void testGetPlantGuides() {
        JsonArray guides = perenualRepository.getPlantGuides(testPlant.getId());

        assertAll(
                () -> assertFalse(guides.isEmpty(), "Plant guides should contain at least one guide"),
                () -> assertNotNull(guides.get(0).getAsJsonObject().get("type"), "Guide type should not be null"),
                () -> assertEquals("watering", guides.get(0).getAsJsonObject().get("type").getAsString(), "Guide type should be watering"),
                () -> assertNotNull(guides.get(0).getAsJsonObject().get("description"), "Guide description should not be null"),
                () -> assertEquals("White wood aster plants should be watered when the top inch of soil is dry. This species thrives best in evenly moist, well-draining soils. During the summer months, water the plant deeply about once a week, allowing the water to thoroughly soak through the soil. During the winter, reduce the waterings to less frequent and shallower.",
                        guides.get(0).getAsJsonObject().get("description").getAsString(), "Guide description should match the description of the given plant."),

                () -> assertNotNull(guides.get(1).getAsJsonObject().get("type"), "Guide type should not be null"),
                () -> assertEquals("sunlight", guides.get(1).getAsJsonObject().get("type").getAsString(), "Guide type should be watering"),
                () -> assertNotNull(guides.get(1).getAsJsonObject().get("description"), "Guide description should not be null"),
                () -> assertEquals("White wood aster (Eurybia divaricata) prefers a sunny spot in the garden, though it can tolerate some shade. This plant species needs full sun (at least 6 hours a day) for successful growth and flowering. In moderate climates, White wood aster will tolerate partial shade, but will produce fewer flowers than when planted in full sun. In hot climates, this plant species should be in a spot with some shade during the hottest parts of the day.",
                        guides.get(1).getAsJsonObject().get("description").getAsString(), "Guide description should match the description of the given plant."),

                () -> assertNotNull(guides.get(2).getAsJsonObject().get("type"), "Guide type should not be null"),
                () -> assertEquals("pruning", guides.get(2).getAsJsonObject().get("type").getAsString(), "Guide type should be watering"),
                () -> assertNotNull(guides.get(2).getAsJsonObject().get("description"), "Guide description should not be null"),
                () -> assertEquals("White wood aster (Eurybia divaricata) should be lightly pruned in the spring after flowering. Pruning should involve cutting back the plant by about 1-third of its total height. Pruning any more than this could result in fewer flower blooms in the future.",
                        guides.get(2).getAsJsonObject().get("description").getAsString(), "Guide description should match the description of the given plant.")
        );
    }

    @Test
    public void testGetPlantGuidesInvalidInput() {
        assertAll(
                () -> assertNull(perenualRepository.getPlantGuides(0), "Should return null if ID out of range"),
                () -> assertNull(perenualRepository.getPlantGuides(3001), "Should return null if ID out of range")
        );
    }

    @Test
    public void testGetDescriptionInvalidInput() {
        assertAll(
                () -> assertNull(perenualRepository.getDescription(0), "Should return null if ID out of range"),
                () -> assertNull(perenualRepository.getDescription(3001), "Should return null if ID out of range")
        );
    }
}
