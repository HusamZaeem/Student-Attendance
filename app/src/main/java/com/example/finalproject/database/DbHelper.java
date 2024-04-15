package com.example.finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.finalproject.models.Admin;
import com.example.finalproject.models.Presence;
import com.example.finalproject.models.StudentSubject;
import com.example.finalproject.models.Students;
import com.example.finalproject.models.Subjects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper {



    public DbHelper(@Nullable Context context) {
        super(context, "PresenceDb", null, 1);
    }






    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Admin.CREATE_TABLE);
        db.execSQL(Students.CREATE_TABLE);
        db.execSQL(Subjects.CREATE_TABLE);
        db.execSQL(StudentSubject.CREATE_TABLE);
        db.execSQL(Presence.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Admin.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Students.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Subjects.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StudentSubject.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Presence.TABLE_NAME);
        onCreate(db);

    }



    public boolean insertAdmin(String username, String email, String password) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Admin.COL_USERNAME, username);
        values.put(Admin.COL_EMAIL, email);
        values.put(Admin.COL_PASSWORD, password);

        long row = db.insert(Admin.TABLE_NAME, null, values);

        return row > 0;

    }


    public boolean insertSubject(String name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Subjects.COL_NAME, name);


        long row = db.insert(Subjects.TABLE_NAME, null, values);

        return row > 0;

    }




    public ArrayList<Subjects> getAllSubjects() {
        ArrayList<Subjects> subjectsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {Subjects.COL_ID, Subjects.COL_NAME};
        Cursor cursor = db.query(
                Subjects.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Subjects.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Subjects.COL_NAME));
                Subjects subject = new Subjects(id, name);
                subjectsList.add(subject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjectsList;
    }

    public boolean deleteSubject(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = Subjects.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = db.delete(Subjects.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    public boolean insertStudent(String firstName, String lastName, String birthdate, ArrayList<Subjects> subjectsList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Students.COL_FIRSTNAME, firstName);
        values.put(Students.COL_LASTNAME, lastName);
        values.put(Students.COL_BIRTHDATE, birthdate);

        long row = db.insert(Students.TABLE_NAME, null, values);
        if (row > 0) {
            int studentId = (int) row;
            return insertStudentSubjects(studentId, subjectsList);
        }
        return false;
    }

    private boolean insertStudentSubjects(int studentId, ArrayList<Subjects> subjectsList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        for (Subjects subject : subjectsList) {
            values.clear();
            values.put(StudentSubject.COL_STUDENT_ID, studentId);
            values.put(StudentSubject.COL_SUBJECT_ID, subject.getId());
            long row = db.insert(StudentSubject.TABLE_NAME, null, values);
            if (row <= 0) {
                // Error occurred while inserting a subject for the student
                return false;
            }
        }

        return true;
    }









//    public boolean insertStudent(String firstName, String lastName, int birthdate) {
//
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(Students.COL_FIRSTNAME, firstName);
//        values.put(Students.COL_LASTNAME, lastName);
//        values.put(Students.COL_BIRTHDATE, birthdate);
//
//
//        long row = db.insert(Students.TABLE_NAME, null, values);
//
//        return row > 0;
//
//    }


    public boolean insertPresence(String month, String day, int studentId, int subjectId) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Presence.COL_MONTH, month);
        values.put(Presence.COL_DAY, day);
        values.put(Presence.COL_STUDENT_ID, studentId);
        values.put(Presence.COL_SUBJECT_ID, subjectId);


        long row = db.insert(Presence.TABLE_NAME, null, values);

        return row > 0;

    }



    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = Admin.COL_USERNAME + " = ? AND " + Admin.COL_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        // Set all the logged_in flags to 0 initially
        ContentValues resetValues = new ContentValues();
        resetValues.put(Admin.COL_LOGGED_IN, 0);
        db.update(Admin.TABLE_NAME, resetValues, null, null);

        // Update the logged_in flag for the current user to 1
        ContentValues loggedInValues = new ContentValues();
        loggedInValues.put(Admin.COL_LOGGED_IN, 1);
        db.update(Admin.TABLE_NAME, loggedInValues, selection, selectionArgs);

        Cursor cursor = db.query(
                Admin.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        boolean credentialsValid = (cursor.getCount() > 0);
        cursor.close();
        return credentialsValid;
    }




    public void updateLoggedInFlag(String username, boolean isLoggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Admin.COL_LOGGED_IN, isLoggedIn ? 1 : 0);

        db.update(Admin.TABLE_NAME, values, Admin.COL_USERNAME + " = ?", new String[]{username});
    }

    public String getLoggedInUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = null;

        // Query the Admin table to get the username of the logged-in user
        Cursor cursor = db.rawQuery("SELECT " + Admin.COL_USERNAME + " FROM " + Admin.TABLE_NAME + " WHERE " + Admin.COL_LOGGED_IN + " = 1", null);

        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow(Admin.COL_USERNAME));
        }

        cursor.close();
        return username;
    }

    public String getLoggedInEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        String email = null;

        // Query the Admin table to get the email of the logged-in user
        Cursor cursor = db.rawQuery("SELECT " + Admin.COL_EMAIL + " FROM " + Admin.TABLE_NAME + " WHERE " + Admin.COL_LOGGED_IN + " = 1", null);

        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(Admin.COL_EMAIL));
        }

        cursor.close();
        return email;
    }


    public Admin getAdminByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Admin admin = null;

        // Query to retrieve the admin row with the specified username
        String query = "SELECT * FROM " + Admin.TABLE_NAME + " WHERE " + Admin.COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            // Retrieve the admin details from the cursor
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Admin.COL_ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(Admin.COL_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(Admin.COL_PASSWORD));

            // Create the Admin object with the retrieved details
            admin = new Admin(id, username, email, password);
        }

        cursor.close();

        return admin;
    }


    public boolean updateAdmin(String oldUsername, String newUsername, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();

        // Retrieve the old admin details from the database
        Admin oldAdmin = getAdminByUsername(oldUsername);
        if (oldAdmin == null) {
            // The old username does not exist in the database
            return false;
        }

        // Update the old admin details with the new values
        oldAdmin.setUsername(newUsername);
        oldAdmin.setEmail(email);
        oldAdmin.setPassword(password);

        ContentValues values = new ContentValues();
        values.put(Admin.COL_USERNAME, newUsername);
        values.put(Admin.COL_EMAIL, email);
        values.put(Admin.COL_PASSWORD, password);

        // Update the admin row with the new details
        int rowsAffected = db.update(Admin.TABLE_NAME, values, Admin.COL_USERNAME + " = ?", new String[]{oldUsername});

        // Return true if at least one row was affected
        return rowsAffected > 0;
    }


    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Students.TABLE_NAME, Students.COL_ID + "=?", new String[]{String.valueOf(studentId)});
        db.close();
    }

    public ArrayList<Students> getAllStudents() {
        ArrayList<Students> studentsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Students.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Students.COL_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_LASTNAME));
                String birthdate = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_BIRTHDATE));
                Students student = new Students(id, firstName, lastName, birthdate);
                studentsList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentsList;
    }



    public ArrayList<Students> getStudentsBySubjectId(int subjectId) {
        ArrayList<Students> studentsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Query to retrieve the students' names and IDs for the specified subject ID
        String query = "SELECT " + Students.TABLE_NAME + "." + Students.COL_ID + ", " + Students.COL_FIRSTNAME + ", " + Students.COL_LASTNAME +
                " FROM " + Students.TABLE_NAME +
                " INNER JOIN " + StudentSubject.TABLE_NAME +
                " ON " + Students.TABLE_NAME + "." + Students.COL_ID + " = " + StudentSubject.TABLE_NAME + "." + StudentSubject.COL_STUDENT_ID +
                " WHERE " + StudentSubject.TABLE_NAME + "." + StudentSubject.COL_SUBJECT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(subjectId)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                // Retrieve the student ID, first name, and last name
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Students.TABLE_NAME + "." + Students.COL_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_LASTNAME));

                // Create a Students object with the ID and name
                Students student = new Students(studentId, firstName, lastName);

                // Add the student to the list of students
                studentsList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return studentsList;
    }



    public int getMonthlyPresencePercentage(int subjectId, String month) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {Presence.COL_STUDENT_ID};
        String selection = Presence.COL_SUBJECT_ID + " = ? AND " + Presence.COL_MONTH + " = ?";
        String[] selectionArgs = {String.valueOf(subjectId), month};

        Cursor cursor = db.query(
                Presence.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int totalDays = cursor.getCount();
        int totalStudents = getStudentsCountBySubjectId(subjectId);
        int presencePercentage = (totalDays * 100) / (totalStudents * 30); // Assuming 30 days in a month

        cursor.close();

        return presencePercentage;
    }

    public int getStudentsCountBySubjectId(int subjectId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {StudentSubject.COL_ID};
        String selection = StudentSubject.COL_SUBJECT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(subjectId)};
        Cursor cursor = db.query(
                StudentSubject.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }



    public double getPresencePercentageForStudentAndSubject(int studentId, int subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + Presence.TABLE_NAME +
                " WHERE " + Presence.COL_STUDENT_ID + " = " + studentId +
                " AND " + Presence.COL_SUBJECT_ID + " = " + subjectId;

        Cursor cursor = db.rawQuery(query, null);
        int attendanceCount = 0;
        if (cursor != null && cursor.moveToFirst()) {
            attendanceCount = cursor.getInt(0);
            cursor.close();
        }

        query = "SELECT COUNT(*) FROM " + Presence.TABLE_NAME +
                " WHERE " + Presence.COL_SUBJECT_ID + " = " + subjectId;

        cursor = db.rawQuery(query, null);
        int totalAttendanceCount = 0;
        if (cursor != null && cursor.moveToFirst()) {
            totalAttendanceCount = cursor.getInt(0);
            cursor.close();
        }

        if (totalAttendanceCount == 0) {
            return 0.0;
        }

        return (attendanceCount * 100.0) / totalAttendanceCount;
    }

    public ArrayList<StudentSubject> getStudentSubjects(int studentId) {
        ArrayList<StudentSubject> studentSubjects = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Query to retrieve the student subjects for the specified student ID
        String query = "SELECT * FROM " + StudentSubject.TABLE_NAME +
                " WHERE " + StudentSubject.COL_STUDENT_ID + " = " + studentId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow(StudentSubject.COL_SUBJECT_ID));

                // Retrieve the subject details using the subjectId
                Subjects subject = getSubject(subjectId);

                // Create the StudentSubject object with the retrieved details
                StudentSubject studentSubject = new StudentSubject(studentId, subject);
                studentSubjects.add(studentSubject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentSubjects;
    }





    public Subjects getSubject(int subjectId) {
        SQLiteDatabase db = getReadableDatabase();
        Subjects subject = null;

        // Query to retrieve the subject details for the specified subject ID
        String query = "SELECT * FROM " + Subjects.TABLE_NAME +
                " WHERE " + Subjects.COL_ID + " = " + subjectId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Subjects.COL_NAME));

            // Create the Subjects object with the retrieved details
            subject = new Subjects(subjectId, name);
        }
        cursor.close();
        return subject;
    }



    public ArrayList<Students> getStudentsBySubjectName(String subjectName) {
        ArrayList<Students> studentsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Query to retrieve the students' details for the specified subject name
        String query = "SELECT " + Students.TABLE_NAME + "." + Students.COL_ID + ", " + Students.COL_FIRSTNAME + ", " + Students.COL_LASTNAME +
                " FROM " + Students.TABLE_NAME +
                " INNER JOIN " + StudentSubject.TABLE_NAME +
                " ON " + Students.TABLE_NAME + "." + Students.COL_ID + " = " + StudentSubject.TABLE_NAME + "." + StudentSubject.COL_STUDENT_ID +
                " INNER JOIN " + Subjects.TABLE_NAME +
                " ON " + StudentSubject.TABLE_NAME + "." + StudentSubject.COL_SUBJECT_ID + " = " + Subjects.TABLE_NAME + "." + Subjects.COL_ID +
                " WHERE " + Subjects.TABLE_NAME + "." + Subjects.COL_NAME + " = ?";
        String[] selectionArgs = {subjectName};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                // Retrieve the student ID, first name, and last name
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Students.TABLE_NAME + "." + Students.COL_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(Students.COL_LASTNAME));

                // Create a Students object with the ID and name
                Students student = new Students(studentId, firstName, lastName);

                // Add the student to the list of students
                studentsList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return studentsList;
    }


    public ArrayList<Subjects> getSubjectsByStudentId(int studentId) {
        ArrayList<Subjects> subjectsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Query to retrieve the subjects' names and IDs for the specified student ID
        String query = "SELECT " + Subjects.TABLE_NAME + "." + Subjects.COL_ID + ", " + Subjects.COL_NAME +
                " FROM " + Subjects.TABLE_NAME +
                " INNER JOIN " + StudentSubject.TABLE_NAME +
                " ON " + Subjects.TABLE_NAME + "." + Subjects.COL_ID + " = " + StudentSubject.TABLE_NAME + "." + StudentSubject.COL_SUBJECT_ID +
                " WHERE " + StudentSubject.TABLE_NAME + "." + StudentSubject.COL_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                // Retrieve the subject ID and name
                int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow(Subjects.TABLE_NAME + "." + Subjects.COL_ID));
                String subjectName = cursor.getString(cursor.getColumnIndexOrThrow(Subjects.COL_NAME));

                // Create a Subjects object with the ID and name
                Subjects subject = new Subjects(subjectId, subjectName);

                // Add the subject to the list of subjects
                subjectsList.add(subject);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return subjectsList;
    }




}
