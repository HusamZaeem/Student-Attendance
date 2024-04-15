package com.example.finalproject.models;

public class StudentSubject {


    private int id;
    private int student_id;
    private int subject_id;
    private Subjects subject;

    public static final String TABLE_NAME = "students_subject";
    public static final String COL_ID = "id";
    public static final String COL_STUDENT_ID = "student_id";
    public static final String COL_SUBJECT_ID = "subject_id";


    public static final String CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME +
            "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_STUDENT_ID + " TEXT, " +
            COL_SUBJECT_ID + " TEXT, " +
            "FOREIGN KEY (" + COL_STUDENT_ID + ")" +
            " REFERENCES " + Students.TABLE_NAME + " (" + Students.COL_ID + ") ON DELETE CASCADE ON UPDATE NO ACTION," +
            "FOREIGN KEY (" + COL_SUBJECT_ID + ")" +
            " REFERENCES " + Subjects.TABLE_NAME + " (" + Subjects.COL_ID + ") ON DELETE CASCADE ON UPDATE NO ACTION)";


    public StudentSubject(int id, int student_id, int subject_id) {
        this.id = id;
        this.student_id = student_id;
        this.subject_id = subject_id;
    }

    public StudentSubject(int studentId, Subjects subject) {
        this.student_id = studentId;
        this.subject = subject;
    }

    public Subjects getSubject() {
        return subject;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
