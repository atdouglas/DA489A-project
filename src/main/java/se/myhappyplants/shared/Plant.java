package se.myhappyplants.shared;

import se.myhappyplants.client.model.PictureRandomizer;

/**
 * Class defining a plant
 * Created by: Frida Jacobsson
 * Updated by: Linn Borgström, Eric Simonson, Susanne Vikström
 */
// TODO: cleanup class
public class Plant {

    private int id;
    private String common_name;
    private String scientific_name;
    private String family;
    private String image_url;
    private String maintenance;
    private String light;
    // time in millisecond form
    private long watering_frequency;
    private boolean poisonous_to_pets;

    public Plant(int id, String scientific_name, String family, String common_name,
                 String image_url, String light, String maintenance, boolean poisonous_to_pets, long watering_frequency) {
        this.id = id;
        this.scientific_name = scientific_name;
        this.family = family;
        this.common_name = common_name;
        this.image_url = image_url;
        this.light = light;
        this.maintenance = maintenance;
        this.poisonous_to_pets = poisonous_to_pets;
        this.watering_frequency = watering_frequency;
    }


    @Override
    public String toString() {
        return String.format("Scientific name: %s \tCommon name: %s \tFamily name: %s ", scientific_name, common_name, family);
    }

    public String getCommon_name() {
        return common_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public int getId() {
        return id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        if(image_url == null) {
            image_url = PictureRandomizer.getRandomPictureURL();
        }
        return image_url.replace("https", "http");
    }

    public String getIsPoisonoutToPets() {
        return poisonous_to_pets;
    }

    public String getLight() {
        return light;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public long getWaterFrequency() {
        return watering_frequency;
    }

}