package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.models.Students;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class CustomeRecyclerViewStudentsName extends RecyclerView.Adapter<CustomeRecyclerViewStudentsName.ViewHolder>{

    Context context;
    ArrayList<Students> data;
    onItemClickListener listener;
    private ArrayList<Students> selectedStudents = new ArrayList<>();
    public CustomeRecyclerViewStudentsName(Context context, ArrayList<Students> data, onItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomeRecyclerViewStudentsName.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_students_name, parent, false);
        return new CustomeRecyclerViewStudentsName.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeRecyclerViewStudentsName.ViewHolder holder, int position) {
        Students student = data.get(position);
        holder.cbStudentName.setText(student.getFirstName() + " " + student.getLastName());
        holder.cbStudentName.setChecked(selectedStudents.contains(student));

        holder.cbStudentName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedStudents.add(student);
            } else {
                selectedStudents.remove(student);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbStudentName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbStudentName = itemView.findViewById(R.id.cbStudentName);
        }
    }

    public ArrayList<Students> getSelectedStudents() {
        return selectedStudents;
    }

    interface onItemClickListener {
        void onClick(Students students);
    }
}