package com.tekenable.tdsec2.dto;

/**
 * Created by nbarrett on 03/06/2016.
 */
public class TrialDirectLoginRequest {

    private String userName;

    private String email;

    private String password;

    /**
     *
     */
    public TrialDirectLoginRequest() {

    }

    /**
     *
     * @param email
     * @param password
     */
    public TrialDirectLoginRequest(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
