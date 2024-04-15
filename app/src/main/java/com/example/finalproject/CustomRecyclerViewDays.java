package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.models.Students;
import com.example.finalproject.models.Subjects;

import java.util.ArrayList;

public class CustomRecyclerViewDays extends RecyclerView.Adapter<CustomRecyclerViewDays.MyHolder> {

    Context context;
    ArrayList<DaysList> data;

    onItemClickListener listener;

    public CustomRecyclerViewDays(Context context, ArrayList<DaysList> data, onItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }




    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_days,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.tvDay.setText(data.get(position).getDays() + "");


        holder.cvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClick(data.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }






    public class MyHolder extends RecyclerView.ViewHolder{

        CardView cvDay;
        TextView tvDay;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            cvDay = itemView.findViewById(R.id.cvDays);
            tvDay = itemView.findViewById(R.id.tvDay);

        }
    }

    interface onItemClickListener {
        void onClick(DaysList daysList);

    }
}
