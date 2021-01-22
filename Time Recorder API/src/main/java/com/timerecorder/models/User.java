package com.timerecorder.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 255)
    @Column(unique = true)
    @NotNull
    private String email;

    @Size(min = 8, max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    @Size(max = 255)
    @NotNull
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    @NotNull
    private String position;

    @Column(scale = 2)
    @Max(value = 99)
    @Min(value = 0)
    private short hoursAWeek;

    @NotNull
    private boolean isAdmin;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<TimeRecord> timeRecords;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<TimeRecord> getTimeRecords() {
        return timeRecords;
    }
}