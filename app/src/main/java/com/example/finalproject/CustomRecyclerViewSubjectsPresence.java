package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.models.StudentSubject;
import com.example.finalproject.models.Students;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomRecyclerViewSubjectsPresence extends RecyclerView.Adapter<CustomRecyclerViewSubjectsPresence.ViewHolder> {

    Context context;
    ArrayList<Subjects> subjectsList;
    Map<Integer, Double> subjectPresenceMap;


    public CustomRecyclerViewSubjectsPresence(Context context, ArrayList<Subjects> subjectsList, Map<Integer, Double> subjectPresenceMap) {
        this.context = context;
        this.subjectsList = subjectsList;
        this.subjectPresenceMap = subjectPresenceMap;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subjects subject = subjectsList.get(position);
        double presencePercentage = subjectPresenceMap.getOrDefault(subject.getId(), 0.0);
        int roundedPercentage = (int) Math.round(presencePercentage);

        holder.tvStdSubjectName.setText(subject.getName());
        holder.tvStdSubjectPresence.setText(roundedPercentage + "%");

        if (roundedPercentage > 80) {
            holder.tvStdSubjectPresence.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (roundedPercentage >= 50) {
            holder.tvStdSubjectPresence.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.tvStdSubjectPresence.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }


    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStdSubjectName;
        public TextView tvStdSubjectPresence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStdSubjectName = itemView.findViewById(R.id.tvStdSubjectName);
            tvStdSubjectPresence = itemView.findViewById(R.id.tvStdSubjectPresence);
        }
    }


}