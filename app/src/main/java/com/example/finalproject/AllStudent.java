package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityAllStudentBinding;
import com.example.finalproject.models.Students;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class AllStudent extends AppCompatActivity {

    ActivityAllStudentBinding binding;

    private RecyclerView recyclerView;
    private CustomRecyclerViewAllStudents adapter;

    private ArrayList<Students> studentsList;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dbHelper = new DbHelper(this); // Initialize the dbHelper

        studentsList = dbHelper.getAllStudents();

        adapter = new CustomRecyclerViewAllStudents(this, studentsList, new CustomRecyclerViewAllStudents.onItemClickListener() {
            @Override
            public void onClick(Students students) {



            }

            @Override
            public void onDelete(int id, int position) {
                View alertCustomDialog = LayoutInflater.from(AllStudent.this).inflate(R.layout.custom_alert_dialog_delete_student,null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllStudent.this);
                alertDialog.setView(alertCustomDialog);


              Button  negative = (Button) alertCustomDialog.findViewById(R.id.alertNo);
              Button  positive = (Button) alertCustomDialog.findViewById(R.id.alertYes);



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
                        deleteStudent(id, position);
                        dialog.cancel();

                    }
                });


                dialog.setCancelable(false);



            }
        });

        binding.rvAllStudents.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.rvAllStudents.setLayoutManager(manager);


    }

    private void deleteStudent(int id, int position) {
        // Remove the subject from the RecyclerView
        adapter.data.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        // Delete the subject from the Subjects table
        dbHelper.deleteStudent(id);
    }


}