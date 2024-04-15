package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalproject.databinding.ActivityMonthsBinding;

import java.util.ArrayList;

public class Months extends AppCompatActivity {


    ActivityMonthsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonthsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ArrayList <MonthsList> data = new ArrayList<>();
        data.add(new MonthsList(getText(R.string.january) + ""));
        data.add(new MonthsList(getText(R.string.february) + ""));
        data.add(new MonthsList(getText(R.string.march) + ""));
        data.add(new MonthsList(getText(R.string.april) + ""));
        data.add(new MonthsList(getText(R.string.may) + ""));
        data.add(new MonthsList(getText(R.string.june) + ""));
        data.add(new MonthsList(getText(R.string.july) + ""));
        data.add(new MonthsList(getText(R.string.august) + ""));
        data.add(new MonthsList(getText(R.string.september) + ""));
        data.add(new MonthsList(getText(R.string.october) + ""));
        data.add(new MonthsList(getText(R.string.november) + ""));
        data.add(new MonthsList(getText(R.string.december) + ""));




        CustomRecyclerView customRecyclerView = new CustomRecyclerView(this, data, new CustomRecyclerView.onItemClickListener() {
            @Override
            public void onClick(MonthsList monthsList) {
              Intent  intent = new Intent (getApplicationContext(),Days.class);
                intent.putExtra("selectedMonth", monthsList.getMonthName());
                intent.putExtra("subjectId", getIntent().getIntExtra("subjectId", 0));
                intent.putExtra("month", monthsList.getMonthName());
                startActivity(intent);
            }
        });
        binding.rvMonths.setAdapter(customRecyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.rvMonths.setLayoutManager(manager);


    }
}