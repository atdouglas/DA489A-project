package se.myhappyplants.shared;

import java.sql.Date;
import java.time.LocalDate;

public class UserPlant extends Plant {
    private Date lastWatered;
    private String nickname;

    public UserPlant(int id, String commonName, String scientificName, String familyName, String imageURL, String maintenance, String light, long waterFrequency, String poisonousToPets) {
        super(id, commonName, scientificName, familyName, imageURL, maintenance, light, waterFrequency, poisonousToPets);
        lastWatered = new Date(System.currentTimeMillis());
    }

    // TODO: FIX
    public double getProgress() {
        long difference = System.currentTimeMillis() - lastWatered.getTime();
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
        long millisSinceLastWatered = System.currentTimeMillis() - lastWatered.getTime();
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
        return lastWatered;
    }

    public void setLastWatered(LocalDate localDate) {
        Date date = java.sql.Date.valueOf(localDate);
        this.lastWatered = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
