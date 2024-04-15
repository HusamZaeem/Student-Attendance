package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;

    DbHelper dbHelper;

    customRecycleViewHome adapter;
    Button positive;
    Button negative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.cvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), EditUser.class));


            }
        });


        binding.floatAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Add_subject.class));


            }
        });


        binding.floatAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AddStudent.class));


            }
        });


        binding.btnShowStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),AllStudent.class));

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        dbHelper = new DbHelper(this);


        String username = dbHelper.getLoggedInUsername();
        String email = dbHelper.getLoggedInEmail();

        if (username != null) {
            binding.tvUsername.setText(getString(R.string.username) + ": " + username);
        }

        if (email != null) {
            binding.tvEmail.setText(getString(R.string.email) + ": " + email);
        }





        ArrayList<Subjects> subjectsList = dbHelper.getAllSubjects();

        // Create the adapter and pass the subject list and click listener
        adapter = new customRecycleViewHome(this, subjectsList, new customRecycleViewHome.onItemClickListener() {
            @Override
            public void onClick(Subjects subjects) {
                Intent intent = new Intent(getApplicationContext(), Months.class);
                intent.putExtra("subjectId", subjects.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(int id, int position) {

                View alertCustomDialog = LayoutInflater.from(Home.this).inflate(R.layout.custom_alert_dialog,null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
                alertDialog.setView(alertCustomDialog);


                negative = (Button) alertCustomDialog.findViewById(R.id.alertNo);
                positive = (Button) alertCustomDialog.findViewById(R.id.alertYes);



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
                        deleteSubject(id, position);
                        dialog.cancel();

                    }
                });


                dialog.setCancelable(false);



            }
        });

        // Set the adapter on the RecyclerView
        binding.rvHome.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        binding.rvHome.setLayoutManager(manager);
    }
    private void deleteSubject(int id, int position) {
        // Remove the subject from the RecyclerView
        adapter.data.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        // Delete the subject from the Subjects table
        dbHelper.deleteSubject(id);
    }

    }
