package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityAddSubjectBinding;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class Add_subject extends AppCompatActivity {

    ActivityAddSubjectBinding binding;

    Subjects subjects;
    DbHelper dbHelper;

    customRecycleViewHome customRecycleViewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dbHelper = new DbHelper(this);




        binding.btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addSubject = binding.etAddSubject.getText().toString();


                if (addSubject.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Please fill Adding subject field with a valid input", Toast.LENGTH_LONG).show();

                }else {
                    dbHelper.insertSubject(addSubject);


                    startActivity(new Intent(getApplicationContext(), Home.class));

                }
            }
        });


    }





    }
