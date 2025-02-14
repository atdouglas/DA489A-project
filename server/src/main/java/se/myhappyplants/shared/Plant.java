package se.myhappyplants.shared;

import se.myhappyplants.client.model.PictureRandomizer;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Class defining a plant
 * Created by: Frida Jacobsson
 * Updated by: Linn Borgström, Eric Simonson, Susanne Vikström
 */
// TODO: cleanup class
public class Plant implements Serializable {

    private static final long serialVersionUID = 867522155232174497L;
    private int id;
    private String commonName;
    private String scientificName;
    private String family;
    private String imageURL;
    private String maintenance;
    private String light;
    // time in millisecond form
    private long waterFrequency;
    // in the PostgreSQL database it can be true, false, or unknown (null).
    // to show relevant information, instead of defaulting to false, it's used as a String in Java
    private String poisonousToPets;

    public Plant(int id, String commonName, String scientificName, String family,
                 String imageURL, String maintenance, String light, long waterFrequency, String poisonousToPets) {
        this.id = id;
        this.scientificName = scientificName;
        this.family = family;
        this.commonName = commonName;
        this.imageURL = imageURL;
        this.light = light;
        this.maintenance = maintenance;
        this.waterFrequency = waterFrequency;
        this.poisonousToPets = poisonousToPets;
    }


    @Override
    public String toString() {
        return String.format("Scientific name: %s \tCommon name: %s \tFamily name: %s ", scientificName, commonName, family);
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public int getId() {
        return id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        if(imageURL == null) {
            imageURL = PictureRandomizer.getRandomPictureURL();
        }
        return imageURL.replace("https", "http");
    }

    public String getIsPoisonoutToPets() {
        return poisonousToPets;
    }

    public String getLight() {
        return light;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public long getWaterFrequency() {
        return waterFrequency;
    }

}