package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class customRecycleViewHome extends RecyclerView.Adapter<customRecycleViewHome.MyHolder> {


    Context context;

    ArrayList<Subjects> data;
    onItemClickListener listener;

    DbHelper dbHelper = new DbHelper(context);

    public customRecycleViewHome(Context context, ArrayList<Subjects> data, onItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }




    @NonNull
    @Override
    public customRecycleViewHome.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_home,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Subjects subject = data.get(position);

        holder.tvSubject.setText(subject.getName());

        int subjectId = data.get(position).getId();




        holder.cvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(subject);
            }
        });



        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onDelete(subject.getId(), position);

            }
        });


    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView tvSubject;
        CardView cvHome;
        ImageView ivDelete;


        public MyHolder(@NonNull View itemView) {
            super(itemView);


            tvSubject = itemView.findViewById(R.id.tvHome);
            cvHome = itemView.findViewById(R.id.cvHome);
            ivDelete = itemView.findViewById(R.id.ivDelete);


        }


    }

    public void setData(ArrayList<Subjects> subjectsList) {
        this.data = subjectsList;
        notifyDataSetChanged();
    }

    interface onItemClickListener {
        void onClick(Subjects subjects);
        void onDelete(int id, int position);
    }


}
