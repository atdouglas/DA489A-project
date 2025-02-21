package se.myhappyplants.shared;

import java.sql.Date;
import java.time.LocalDate;

public class UserPlant extends Plant {
    private String nickname;
    private long last_watered;

    public UserPlant(int id, String scientific_name, String family, String common_name, String image_url, String light, String maintenance, boolean poisonous_to_pets, long water_frequency, String nickname, long last_watered) {
        super(id, common_name, scientific_name, family, image_url, maintenance, light, poisonous_to_pets, water_frequency);
        this.nickname = nickname;
        this.last_watered = last_watered;
    }

    // TODO: FIX Shall return in milliseconds how long it has been since the plant has been watered.
    // TODO Use the water_frequency variable to calculate this.
    public double getProgress(long currentTime) {
        long difference = currentTime - last_watered;
        difference -= 43000000;
        double progress = 1.0 - ((double) difference / (double) getWaterFrequency());
        if (progress <= 0.02) {
            progress = 0.02;
        }
        else if (progress >= 0.95) {
            progress = 1.0;
        }
        return progress;
    }

    // TODO: FIX Shall return a string to indicate how long it has been since the plant has been watered.
    // TODO: Under one day in this format (12h). If it's over one day then use this format (1d 2h)
    /*


    public String getFormattedDaysSinceWatered(long currentTime) {
        //long millisSinceLastWatered = System.currentTimeMillis() - last_watered.getTime();
        //long millisUntilNextWatering = getWaterFrequency() - millisSinceLastWatered;
        long millisInADay = 86400000;

       // double daysExactlyUntilWatering = (double) millisUntilNextWatering / (double) millisInADay;

        int daysUntilWatering = (int) daysExactlyUntilWatering;
        double decimals = daysExactlyUntilWatering - (int) daysExactlyUntilWatering;

        if (decimals > 0.5) {
            daysUntilWatering = (int) daysExactlyUntilWatering + 1;
        }

        String strToReturn = String.format("Needs water in %d days", daysUntilWatering);

        return strToReturn;
    }
     */


    public long getLastWatered() {
        return last_watered;
    }

    // TODO: Maybe remove this?
    public void setLastWatered(LocalDate localDate) {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
