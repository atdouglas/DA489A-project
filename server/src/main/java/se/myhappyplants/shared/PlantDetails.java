package se.myhappyplants.shared;

import java.io.Serializable;

/**
 * Container class for more detailed information about a plant
 * Created by: Frida Jacobsson
 * Updated by:
 **/
public class PlantDetails implements Serializable {

    private String scientificName;
    private int light;
    private int waterFrequency;
    private String family;

    public PlantDetails(String scientificName, int light, int waterFrequency, String family) {
        this.scientificName = scientificName;
        this.light = light;
        this.waterFrequency = waterFrequency;
        this.family = family;
    }

    public String getScientificName() {
        return scientificName;
    }

    public int getLight() {
        return light;
    }

    public int getWaterFrequency() {
        return waterFrequency;
    }

    public String getFamily() {
        return family;
    }
}
