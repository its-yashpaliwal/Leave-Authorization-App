package com.example.leave;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    ArrayList<application> list = new ArrayList<>();

    public StudentAdapter(ArrayList<application> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_list_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        application student = list.get(position);
        holder.heading.setText(student.getReason().toString());
        holder.subheading.setText("From: " + student.getStart().toString());
        holder.approve.setText("To: " + student.getEnd().toString());
        String stat = student.getStatus();
        if(stat.equals("Pending")){
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FFA500"));
        }
        else if(stat.equals("Approve")){
            holder.status.setText("Approved");
            holder.status.setTextColor(Color.GREEN);
        }
        else if(stat.equals("Deny")){
            holder.status.setText("Denied");
            holder.status.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading, subheading, approve, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.mainStudentHead);
            subheading = itemView.findViewById(R.id.mainStudentSubHead);
            approve = itemView.findViewById(R.id.mainStudentApprove);
            status = itemView.findViewById(R.id.mainStudentStatus);
        }
    }
}

