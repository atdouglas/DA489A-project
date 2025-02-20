package shared;

import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.UserPlant;

/**
 * Test class for the UserPlant class.
 * @see se.myhappyplants.shared.UserPlant
 * @author Douglas Almö Thorsell
 */

public class UserPlantTest {
    private UserPlant testPlant = new UserPlant(
            527,
            "Actaea racemosa",
            "Ranunculaceae",
            "black cohosh",
            "https://perenual.com/storage/species_image/527_actaea_racemosa/og/28204469216_e9680ed0a4_b.jpg",
            "full sun",
            "Low",
            false,
            734400000,
            "testPlant"
    );

    @Test
    void getWaterProgress(){

    }
}
