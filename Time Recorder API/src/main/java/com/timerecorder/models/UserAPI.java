package com.timerecorder.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserAPI {
    @NotNull
    private Integer id;

    @Size(max = 255)
    @NotNull
    private String email;

    @Size(max = 255)
    @NotNull
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    @NotNull
    private String position;

    @Max(value = 99)
    @Min(value = 0)
    private short hoursAWeek;

    @NotNull
    private boolean isAdmin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public short getHoursAWeek() {
        return hoursAWeek;
    }

    public void setHoursAWeek(short hoursAWeek) {
        this.hoursAWeek = hoursAWeek;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
