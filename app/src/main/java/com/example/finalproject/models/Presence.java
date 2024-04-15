package com.example.finalproject.models;

public class Presence {


    private int id;
    private String month;
    private String day;
    private int student_id;
    private int subject_id;


    public static final String TABLE_NAME = "presence";
    public static final String COL_ID = "id";
    public static final String COL_MONTH = "month";
    public static final String COL_DAY = "day";
    public static final String COL_STUDENT_ID = "student_id";
    public static final String COL_SUBJECT_ID = "subject_id";


    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_MONTH + " TEXT, " +
            COL_DAY + " TEXT, " +
            COL_STUDENT_ID + " INTEGER, " +
            COL_SUBJECT_ID + " INTEGER, " +
            "FOREIGN KEY (" + COL_STUDENT_ID + ")" +
            " REFERENCES " + Students.TABLE_NAME + " (" + Students.COL_ID + ") ON DELETE CASCADE ON UPDATE NO ACTION, " +
            "FOREIGN KEY (" + COL_SUBJECT_ID + ")" +
            " REFERENCES " + Subjects.TABLE_NAME + " (" + Subjects.COL_ID + ") ON DELETE CASCADE ON UPDATE NO ACTION)";

    public Presence(int id, String month, String day, int student_id, int subject_id) {
        this.id = id;
        this.month = month;
        this.day = day;
        this.student_id = student_id;
        this.subject_id = subject_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }
}
