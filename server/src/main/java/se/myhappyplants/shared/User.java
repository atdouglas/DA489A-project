package se.myhappyplants.shared;

import java.io.*;

/**
 * Container class that defines a User
 * Created by: Linn Borgström
 * Updated by: Linn Borgström, 2021-05-17
 */
public class User {

    private int uniqueId;
    private String email;
    private String username;
    private String password;
    private String avatarURL;
    private boolean isNotificationsActivated = true;
    private boolean funFactsActivated = true;

    /**
     * Constructor used when registering a new user account
     */
    public User(String email, String username, String password, boolean isNotificationsActivated) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isNotificationsActivated = isNotificationsActivated;
    }

    /**
     * Simple constructor for login requests
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int uniqueID, String email, String username, boolean notificationsActivated) {
        this.uniqueId = uniqueID;
        this.email = email;
        this.username = username;
        this.isNotificationsActivated = notificationsActivated;

    }
    /**
     * Constructor used to return a users details from the database
     */
    public User(int uniqueId, String email, String username, boolean isNotificationsActivated, boolean funFactsActivated) {

        this.uniqueId = uniqueId;
        this.email = email;
        this.username = username;
        this.isNotificationsActivated = isNotificationsActivated;
        this.funFactsActivated = funFactsActivated;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean areNotificationsActivated() {
        return isNotificationsActivated;
    }

    public void setIsNotificationsActivated(boolean notificationsActivated) {
        this.isNotificationsActivated = notificationsActivated;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatar(String pathToImg) {
        this.avatarURL = new File(pathToImg).toURI().toString();
    }

    public boolean areFunFactsActivated() {
        return funFactsActivated;
    }

    public void setFunFactsActivated(boolean funFactsActivated) {
        this.funFactsActivated = funFactsActivated;
    }
}
