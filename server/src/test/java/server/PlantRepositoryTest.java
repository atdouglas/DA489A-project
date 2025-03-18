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
        int testNumber = 6;
        assertAll(
                "Testing correct input",
                () -> assertEquals(30, result.size(),
                        "The size of a search of " + searchQuery + " should be 30 :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals(2867,result.get(testNumber).getId(),
                        "The ID is " +result.get(testNumber).getId() + " It should be 2867:testSearchListCorrectFamilyInput failed"),
                () -> assertEquals("Euphorbia Characias 'Tasmanian Tiger'",result.get(testNumber).getScientific_name(),
                        "The family name is " +result.get(testNumber).getScientific_name() + " It should be Euphorbia amygdaloides subsp. robbiae :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals("Euphorbiaceae",result.get(testNumber).getFamily(),
                        "The family is " +result.get(testNumber).getFamily() + " It should be Euphorbiaceae :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals("Spurge",result.get(testNumber).getCommon_name(),
                        "The common name is " +result.get(testNumber).getCommon_name() + " It should be Spurge :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals("http://perenual.com/storage/species_image/2867_euphorbia_characias_tasmanian_tiger/og/25977518077_dabbb0a978_b.jpg",result.get(testNumber).getImage_url(),
                        "The image is " +result.get(testNumber).getImage_url() + " It should be http://perenual.com/storage/species_image/2867_euphorbia_characias_tasmanian_tiger/og/25977518077_dabbb0a978_b.jpg :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals("Full sun",result.get(testNumber).getLight(),
                        "The light level is " +result.get(testNumber).getLight() + " It should be Full sun :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals("Low",result.get(testNumber).getMaintenance(),
                        "The maintenance is " +result.get(testNumber).getMaintenance() + " It should be Low :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals(false,result.get(testNumber).getIsPoisonoutToPets(),
                        "The poison is " +result.get(testNumber).getIsPoisonoutToPets() + " It should be false :testSearchListCorrectFamilyInput failed"),
                () -> assertEquals(0,result.get(testNumber).getWaterFrequency(),
                        "The water frequency is " +result.get(testNumber).getWaterFrequency() + " It should be 0 :testSearchListCorrectFamilyInput failed"));
    }

    @Test
    void testSearchListNullInput(){
        String searchQuery = null;
        List<Plant> result = plantRepository.getResult(searchQuery);
        assertEquals(0, result.size(),
                "The size of a search of " + searchQuery + " should be 0 :testSearchListNullInput failed");
    }

    @Test
    void testSearchListEmptyInput(){
        String searchQuery = "";
        List<Plant> result = plantRepository.getResult(searchQuery);
        assertEquals(0, result.size(),
                "An empty search should return 0 results :testSearchListEmptyInput failed");
    }

    @Test
    void SearchListMadeUpValue(){
        String searchQuery = "DiggidiDooooggiiiiiiiiii";
        List<Plant> result = plantRepository.getResult(searchQuery);
        assertEquals(0, result.size(),
                "The size of a search of " + searchQuery + " should be 0 :testSearchListMadeUpValue failed");
    }
}
