package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityStudentsNameBinding;
import com.example.finalproject.models.Students;

import java.util.ArrayList;

public class StudentsName extends AppCompatActivity {


    ActivityStudentsNameBinding binding;
    DbHelper dbHelper = new DbHelper(this);
    ArrayList<Students> studentsList;
    CustomeRecyclerViewStudentsName customeRecyclerViewStudentsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentsNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int subjectId = getIntent().getIntExtra("subjectId", 0);


        // Retrieve student names from the database
        studentsList = dbHelper.getStudentsBySubjectId(subjectId);

        // Create a list of Students objects with the retrieved names
        ArrayList<Students> students = new ArrayList<>();
        for (Students student : studentsList) {
            String fullName = student.getFirstName() + " " + student.getLastName();
            Students newStudent = new Students(student.getId(), student.getFirstName(), student.getLastName());
            students.add(newStudent);
        }

        customeRecyclerViewStudentsName = new CustomeRecyclerViewStudentsName(this, students, new CustomeRecyclerViewStudentsName.onItemClickListener() {
            @Override
            public void onClick(Students students) {

            }
        });

        binding.rvStudentsName.setAdapter(customeRecyclerViewStudentsName);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvStudentsName.setLayoutManager(manager);

        binding.btnRegisterPresence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View alertCustomDialog = LayoutInflater.from(StudentsName.this).inflate(R.layout.custome_alert_dialog_presence, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentsName.this);
                alertDialog.setView(alertCustomDialog);


                Button negative = (Button) alertCustomDialog.findViewById(R.id.alertNo);
                Button positive = (Button) alertCustomDialog.findViewById(R.id.alertYes);


                final AlertDialog dialog = alertDialog.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Handle register presence button click here
                        ArrayList<Students> selectedStudents = customeRecyclerViewStudentsName.getSelectedStudents();
                        for (Students student : selectedStudents) {
                            int day = getIntent().getIntExtra("day", 0);
                            String month = getIntent().getStringExtra("month");
                            int subjectId = getIntent().getIntExtra("subjectId", 0);
                            String dayString = String.valueOf(day);

                            if (dbHelper.insertPresence(month, dayString, student.getId(), subjectId)) {
                                Toast.makeText(StudentsName.this, "Presence added successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                            } else {
                                Toast.makeText(StudentsName.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }


                        dialog.cancel();

                    }
                });


                dialog.setCancelable(false);


            }
        });


    }
}