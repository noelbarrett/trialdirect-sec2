package com.tekenable.tdsec2.dto;

import java.io.Serializable;

/**
 * Created by nbarrett on 03/06/2016.
 */
public class TdLoginValidationResponse implements Serializable {

    private String email;
    private String name;

    public TdLoginValidationResponse() {}

    public TdLoginValidationResponse(String email, String name) {

        this.email = email;
        this.name = name;
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
}
