package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityDaysBinding;
import com.example.finalproject.models.Students;

import java.util.ArrayList;

public class Days extends AppCompatActivity {

    ActivityDaysBinding binding;
    DbHelper dbHelper = new DbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int subjectId = getIntent().getIntExtra("subjectId", 0);
        String month = getIntent().getStringExtra("month");


       int monthlyPercentage = dbHelper.getMonthlyPresencePercentage(subjectId,month);
       String presencePercentage = String.valueOf(monthlyPercentage);

       binding.tvPresencePercentage.setText(presencePercentage + "%");

        ArrayList<DaysList> data = new ArrayList<>();

        for (int i = 1; i <31 ; i++) {
            data.add(new DaysList(i));
        }

        String selectedMonth = getIntent().getStringExtra("selectedMonth");

        binding.tvMonthName.setText(getText(R.string.month) + " " + selectedMonth);


        CustomRecyclerViewDays customRecyclerViewDays = new CustomRecyclerViewDays(this, data, new CustomRecyclerViewDays.onItemClickListener() {
            @Override
            public void onClick(DaysList daysList) {
                Intent intent = new Intent(getApplicationContext(), StudentsName.class);
                intent.putExtra("subjectId", getIntent().getIntExtra("subjectId", 0));
                intent.putExtra("month", getIntent().getStringExtra("month"));
                intent.putExtra("day", daysList.getDays());
                startActivity(intent);
            }
        });

        binding.rvDays.setAdapter(customRecyclerViewDays);

        GridLayoutManager manager = new GridLayoutManager(this,5);
        binding.rvDays.setLayoutManager(manager);




    }
}