package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.MyHolder> {

    Context context;
    ArrayList<MonthsList> data;
    onItemClickListener listener;



    public CustomRecyclerView(Context context, ArrayList<MonthsList> data, onItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }







    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_months,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

          holder.tvMonths.setText(data.get(position).getMonthName());


          holder.cvMonths.setOnClickListener(new View.OnClickListener() {
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






    public class MyHolder extends RecyclerView.ViewHolder {

        CardView cvMonths;
        TextView tvMonths;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

          cvMonths =  itemView.findViewById(R.id.cvMonths);
          tvMonths =  itemView.findViewById(R.id.tvMonths);

        }
    }



    interface onItemClickListener {
        void onClick(MonthsList monthsList);

    }


}
