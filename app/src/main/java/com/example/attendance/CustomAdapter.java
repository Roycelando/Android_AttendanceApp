package com.example.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    //i have made all the changes here, for example when students list is added it creates horizontal
    //lines on screen so dont change anything in this code.
    ArrayList<StudenDataModelClass> studenData;
    Context context;
    int pos;

    public CustomAdapter(ArrayList<StudenDataModelClass> studenData, Context context, int pos) {
        this.studenData = studenData;
        this.context = context;
        this.pos = pos;
    }

    public CustomAdapter(ArrayList<StudenDataModelClass> studenData, Context context) {
        this.studenData = studenData;
        this.context = context;
        pos = -1;
    }


    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewHolder viewHolder = null;
        View contactView = inflater.inflate(R.layout.activity_student_view, parent, false);
        viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {

        holder.tv_number.setText(String.valueOf(position + 1));
        holder.tv_name.setText(studenData.get(position).getName());
        holder.tv_attd.setText(studenData.get(position).getStatus());
        if (pos == 2) {
            holder.tv_attd.setVisibility(View.GONE);
        } else {
            holder.tv_attd.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return studenData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_attd, tv_name, tv_number;

        public ViewHolder(View view) {
            super(view);
            tv_attd = view.findViewById(R.id.tv_attd);
            tv_name = view.findViewById(R.id.tv_name);
            tv_number = view.findViewById(R.id.tv_number);
        }
    }
}
