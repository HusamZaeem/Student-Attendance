package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class customRecyclerViewStudentsSubjects extends RecyclerView.Adapter<customRecyclerViewStudentsSubjects.MyHolder> {

    private Context context;
    private ArrayList<Subjects> data;
    private ArrayList<Subjects> selectedSubjects; // Add this line

    public customRecyclerViewStudentsSubjects(Context context, ArrayList<Subjects> data, ArrayList<Subjects> selectedSubjects) {
        this.context = context;
        this.data = data;
        this.selectedSubjects = selectedSubjects; // Add this line
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_student, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Subjects subject = data.get(position);
        holder.checkBox.setText(subject.getName());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    selectedSubjects.add(data.get(position));
                } else {
                    selectedSubjects.remove(data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cbSubjectStudent);
        }
    }
}
