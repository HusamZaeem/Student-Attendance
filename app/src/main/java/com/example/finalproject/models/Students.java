package com.example.finalproject.models;

import java.util.Date;

public class Students {

    private int id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private boolean isChecked; // New property

    public static final String TABLE_NAME = "students";
    public static final String COL_ID = "id";
    public static final String COL_FIRSTNAME = "firstName";
    public static final String COL_LASTNAME = "lastName";
    public static final String COL_BIRTHDATE = "birthdate";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_FIRSTNAME + " TEXT, " +
            COL_LASTNAME + " TEXT, " +
            COL_BIRTHDATE + " Text)";

    public Students(int id, String firstName, String lastName, String birthdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.isChecked = false; // Initialize isChecked to false by default
    }

    public Students(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isChecked = false; // Initialize isChecked to false by default
    }

    public Students(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isChecked = false; // Initialize isChecked to false by default
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}