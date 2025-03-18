package se.myhappyplants.shared;

import java.io.*;

public class User {

    private int uniqueId;
    private String email;
    private String password;
    private String avatarURL;
    private String accessToken;
    private String securityQuestion;
    private String securityAnswer;
    private boolean isNotificationsActivated = true;

    /**
     * Constructor used when registering a new user account or for login requests
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor used for token authentication
     */
    public User(String accessToken, int uniqueId){
        this.uniqueId = uniqueId;
        this.accessToken = accessToken;
    }

    /**
     * Constructor used for testing purposes.
     * @author Douglas Almö Thorsell
     */
    public User(String email, String password, String securityQuestion, String securityAnswer) {
        this.email = email;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public User(int uniqueID, String email) {
        this.uniqueId = uniqueID;
        this.email = email;
    }
    /**
     * Constructor used to return a users details from the database
     */
    public User(int uniqueId, String email, boolean isNotificationsActivated) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.isNotificationsActivated = isNotificationsActivated;
    }

    /**
     * Constructor used for testing purposes.
     * @author Douglas Almö Thorsell
     */
    public User(int uniqueId, String email, String password, String accessToken, boolean isNotificationsActivated) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.isNotificationsActivated = isNotificationsActivated;
    }

    public int getUniqueId() {
        return uniqueId;
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

    public String getAccessToken(){
        return this.accessToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setIsNotificationsActivated(boolean notificationsActivated) {
        this.isNotificationsActivated = notificationsActivated;
    }

    public void setUniqueId(int uniqueId){
        this.uniqueId = uniqueId;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }
}
