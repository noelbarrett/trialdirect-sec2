package com.tekenable.tdsec2.model;

import java.io.Serializable;

/**
 * Created by nbarrett on 22/06/2016.
 */
public class TdLoginValidationResponse implements Serializable {

    private String email;
    private String name;
    private String userType;


    public TdLoginValidationResponse(String email, String name, String userType) {

        this.email= email;
        this.name = name;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
