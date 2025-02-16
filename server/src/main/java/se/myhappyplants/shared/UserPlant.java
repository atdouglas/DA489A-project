package se.myhappyplants.shared;

import java.sql.Date;
import java.time.LocalDate;

public class UserPlant extends Plant {
    private String nickname;
    private Date last_watered;

    public UserPlant(int id, String scientific_name, String family, String common_name, String image_url, String light, String maintenance, boolean poisonous_to_pets, long water_frequency, String nickname) {
        super(id, common_name, scientific_name, family, image_url, maintenance, light, poisonous_to_pets, water_frequency);
        this.nickname = nickname;
        last_watered = new Date(System.currentTimeMillis());
    }

    // TODO: FIX
    public double getProgress() {
        long difference = System.currentTimeMillis() - last_watered.getTime();
        difference -= 43000000l;
        double progress = 1.0 - ((double) difference / (double) getWaterFrequency());
        if (progress <= 0.02) {
            progress = 0.02;
        }
        else if (progress >= 0.95) {
            progress = 1.0;
        }
        return progress;
    }

    // TODO: FIX
    public String getDaysUntilWater() {
        long millisSinceLastWatered = System.currentTimeMillis() - last_watered.getTime();
        long millisUntilNextWatering = getWaterFrequency() - millisSinceLastWatered;
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

    public Date getLastWatered() {
        return last_watered;
    }

    // TODO: fix?
    public void setLastWatered(LocalDate localDate) {
        Date date = java.sql.Date.valueOf(localDate);
        this.last_watered = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
