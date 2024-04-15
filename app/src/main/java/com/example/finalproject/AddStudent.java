package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityAddStudentBinding;
import com.example.finalproject.models.Subjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddStudent extends AppCompatActivity {

    ActivityAddStudentBinding binding;
    DbHelper dbHelper = new DbHelper(this);
    private int year, month, day;
    private ArrayList<Subjects> selectedSubjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.tvDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDatePickerDialog();

            }
        });

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);





        ArrayList<Subjects> subjectsList = dbHelper.getAllSubjects();


        customRecyclerViewStudentsSubjects customRecyclerViewStudentsSubjects = new customRecyclerViewStudentsSubjects(this, subjectsList,selectedSubjects);


        binding.rvAddStudent.setAdapter(customRecyclerViewStudentsSubjects);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.rvAddStudent.setLayoutManager(manager);


        binding.btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = binding.etFirstName.getText().toString().trim();
                String lastName = binding.etFamilyName.getText().toString().trim();
                String birthdate = binding.tvDateOfBirth.getText().toString().trim();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !birthdate.isEmpty()) {
                    if (selectedSubjects.isEmpty()) {
                        Toast.makeText(AddStudent.this, "Please select at least one subject", Toast.LENGTH_SHORT).show();
                    } else {
                        // Call the insertStudent method with the entered values and selected subjects
                        boolean success = dbHelper.insertStudent(firstName, lastName, birthdate, selectedSubjects);
                        if (success) {
                            Toast.makeText(AddStudent.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                            // Reset the form and selected subjects
                            binding.etFirstName.setText("");
                            binding.etFamilyName.setText("");
                            binding.tvDateOfBirth.setText("");
                            selectedSubjects.clear();
                            customRecyclerViewStudentsSubjects.notifyDataSetChanged();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                        } else {
                            Toast.makeText(AddStudent.this, "Failed to add student", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(AddStudent.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }







    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date to the EditText
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                binding.tvDateOfBirth.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }





    private long convertDateToTimestamp(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date parsedDate = dateFormat.parse(date);
            if (parsedDate != null) {
                return parsedDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



}