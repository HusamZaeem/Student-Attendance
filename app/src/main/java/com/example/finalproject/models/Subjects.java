package com.example.finalproject.models;

import java.io.Serializable;

public class Subjects  {

    private int id;
    private String name;

    public static final String TABLE_NAME = "subjects";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";



    public static final String  CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT)";


    public Subjects(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
