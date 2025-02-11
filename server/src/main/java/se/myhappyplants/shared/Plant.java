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
    private int plantId;
    private String commonName;
    private String scientificName;
    private String familyName;
    private String imageURL;
    private String nickname;
    private String maintenance;
    private String light;
    // time in millisecond form
    private long waterFrequency;
    // in the PostgreSQL database it can be true, false, or unknown (null).
    // to show relevant information, instead of defaulting to false, it's used as a String in Java
    private String poisonousToPets;
    // TODO: maybe change to real date? or long? maybe this should not be stored in Plant
    private Date lastWatered;

    public Plant(int plantId, String commonName, String scientificName, String familyName,
                 String imageURL, String maintenance, String light, long waterFrequency, String poisonousToPets) {
        this.plantId = plantId;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.familyName = familyName;
        this.imageURL = imageURL;
        this.maintenance = maintenance;
        this.light = light;
        this.waterFrequency = waterFrequency;
        this.poisonousToPets = poisonousToPets;
        lastWatered = new Date(System.currentTimeMillis());
    }


    public String toString() {
        return String.format("Scientific name: %s \tCommon name: %s \tFamily name: %s ", scientificName, commonName, familyName);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public int getPlantId() {
        return plantId;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    /**
     * Image location for selected plant
     *
     * @return URL location of image
     */
    public String getImageURL() {
        if(imageURL == null) {
            imageURL = PictureRandomizer.getRandomPictureURL();
        }
        return imageURL.replace("https", "http");
    }

    public Date getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(LocalDate localDate) {
        Date date = java.sql.Date.valueOf(localDate);
        this.lastWatered = date;
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

    /**
     * Compares the length of time since the plant was watered
     * with recommended frequency of watering. Returns a decimal value
     * that can be used in a progress bar or indicator
     *
     * @return Double between 0.02 (max time elapsed) and 1.0 (min time elapsed)
     */
    // TODO: FIX
    public double getProgress() {
        long difference = System.currentTimeMillis() - lastWatered.getTime();
        difference -= 43000000l;
        double progress = 1.0 - ((double) difference / (double) waterFrequency);
        if (progress <= 0.02) {
            progress = 0.02;
        }
        else if (progress >= 0.95) {
            progress = 1.0;
        }
        return progress;
    }

    /**
     * Converts time since last water from milliseconds
     * into days, then returns the value as
     * an explanation text
     *
     * @return Days since last water
     */

    // TODO: FIX
    public String getDaysUntilWater() {
        long millisSinceLastWatered = System.currentTimeMillis() - lastWatered.getTime();
        long millisUntilNextWatering = waterFrequency - millisSinceLastWatered;
        long millisInADay = 86400000;

        double daysExactlyUntilWatering = (double) millisUntilNextWatering / (double) millisInADay;

        int daysUntilWatering = (int) daysExactlyUntilWatering;
        double decimals = daysExactlyUntilWatering - (int) daysExactlyUntilWatering;

        if (decimals > 0.5) {
            daysUntilWatering = (int) daysExactlyUntilWatering + 1;
        }

        String strToReturn = String.format("Needs water in %d days", daysUntilWatering);
        if (getProgress() == 0.02 || daysUntilWatering == 0) {
            strToReturn = "You need to water this plant now!";
        }

        return strToReturn;
    }
}