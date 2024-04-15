package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import com.example.finalproject.CustomRecyclerViewSubjectsPresence;
import com.example.finalproject.models.Subjects;
import com.example.finalproject.database.DbHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.models.StudentSubject;
import com.example.finalproject.models.Students;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CustomRecyclerViewAllStudents extends RecyclerView.Adapter<CustomRecyclerViewAllStudents.ViewHolder> {


    Context context;
    ArrayList<Students> data;
    onItemClickListener listener;
    DbHelper dbHelper;

    public CustomRecyclerViewAllStudents(Context context, ArrayList<Students> data, onItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_students, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Students student = data.get(position);

        holder.tvStudentName.setText(student.getFirstName() + " " + student.getLastName());
        int age = calculateAge(student.getBirthdate());

        DbHelper dbHelper = new DbHelper(context);

        ArrayList<StudentSubject> studentSubjects = dbHelper.getStudentSubjects(student.getId());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the StudentDetails activity
                Intent intent = new Intent(context, StudentDetails.class);

                // Pass the student details to the activity
                intent.putExtra("studentFullName", student.getFirstName() + " " + student.getLastName());
                intent.putExtra("studentDateOfBirth", student.getBirthdate());
                intent.putExtra("studentAge", age);

                // Create a HashMap to store subject names and presence percentages
                HashMap<String, Double> subjectPresenceMap = new HashMap<>();

                // Add subject names and presence percentages to the map
                for (StudentSubject studentSubject : studentSubjects) {
                    Subjects subject = studentSubject.getSubject();
                    if (subject != null) {
                        String subjectName = subject.getName();
                        double presencePercentage = dbHelper.getPresencePercentageForStudentAndSubject(student.getId(), subject.getId());
                        subjectPresenceMap.put(subjectName, presencePercentage);
                    }
                }

                // Pass the subject presence map
                intent.putExtra("subjectPresenceMap", subjectPresenceMap);

                context.startActivity(intent);
            }
        });




        holder.ivDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onDelete(student.getId(), position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        ImageView ivDelete1;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            ivDelete1 = itemView.findViewById(R.id.ivDelete1);
            cardView = itemView.findViewById(R.id.cvAllStudents);

        }

    }


    interface onItemClickListener {
        void onClick(Students students);
        void onDelete(int id, int position);
    }

    private int calculateAge(String dateOfBirth) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(dateOfBirth);
            Calendar dob = Calendar.getInstance();
            dob.setTime(birthDate);

            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
                age--;
            } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }






}