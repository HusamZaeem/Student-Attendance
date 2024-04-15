package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityStudentDetailsBinding;
import com.example.finalproject.models.StudentSubject;
import com.example.finalproject.models.Subjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentDetails extends AppCompatActivity {

    ActivityStudentDetailsBinding binding;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DbHelper(this);

        // Retrieve student details from the intent extras
        String studentFullName = getIntent().getStringExtra("studentFullName");
        String studentDateOfBirth = getIntent().getStringExtra("studentDateOfBirth");
        int studentAge = getIntent().getIntExtra("studentAge", 0);

        // Set student details in the TextViews
        binding.tvStudentName.setText(studentFullName);
        binding.tvStudentDateOfBirth.setText(studentDateOfBirth);
        binding.tvStudentAgeView.setText(String.valueOf(studentAge));

        // Retrieve studentSubjects from the database using the dbHelper
        int studentId = getIntent().getIntExtra("studentId", -1);
        ArrayList<Subjects> studentSubjects = dbHelper.getSubjectsByStudentId(studentId);

        // Create a map to store subject presence percentages
        Map<Integer, Double> subjectPresenceMap = new HashMap<>();

        // Calculate and store presence percentages for each subject
        for (Subjects subject : studentSubjects) {
            double presencePercentage = dbHelper.getPresencePercentageForStudentAndSubject(studentId, subject.getId());
            subjectPresenceMap.put(subject.getId(), presencePercentage);
        }

        // Set up RecyclerView for subjects and presence percentages
        CustomRecyclerViewSubjectsPresence adapter = new CustomRecyclerViewSubjectsPresence(this, studentSubjects, subjectPresenceMap);
        binding.rvRegisteredSubjects.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvRegisteredSubjects.setLayoutManager(manager);
    }
}