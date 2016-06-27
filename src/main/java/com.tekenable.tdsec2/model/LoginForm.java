package com.tekenable.tdsec2.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by nbarrett on 21/06/2016.
 */
public class LoginForm {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String userName;
    @NotEmpty
    @Size(min = 1, max = 20)
    private String password;

    private String email;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
