package server;

import org.junit.jupiter.api.Test;
import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.shared.Plant;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PlantRepositoryTest {

    private PlantRepository plantRepository = new PlantRepository();

    @Test
    void testSearchListCorrectFamilyInput(){
        String searchQuery = "spurge";
        List<Plant> result = plantRepository.getResult(searchQuery);
        assertAll(
                "Testing correct input",
                () -> assertEquals(17, result.size(), "The size of a search of " + searchQuery + " should be 17"),
                () -> assertEquals(2873,result.get(2).getId(), "The ID is " +result.get(2).getId() + " It should be 2873"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("spurge",result.get(2).getFamily()), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae"),
                () -> assertEquals("Euphorbiaceae",result.get(2).getCommon_name(), "The common name is " +result.get(2).getCommon_name() + " It should be Euphorbiaceae")
        );
    }

    @Test
    void testSearchListNullInput(){
        String searchQuery = null;
        List<Plant> result = plantRepository.getResult(searchQuery);
        assertEquals(0, result.size(), "The size of a search of " + searchQuery + " should be 0");
    }

    @Test
    void testSearchListEmptyInput(){
        String searchQuery = "";
        List<Plant> result = plantRepository.getResult(searchQuery);
        assertEquals(0, result.size(), "An empty search should return 0 results");
    }
}
