package com.example.leave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Triple;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    ArrayList<Triple<application, String, Integer>> list = new ArrayList<>();

    public TeacherAdapter(ArrayList<Triple<application, String, Integer>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        application student = list.get(position).getFirst();
        String UID = list.get(position).getSecond();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("roll").getValue(String.class);
                holder.heading.setText("Roll Number: "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.heading.setTextColor(Color.WHITE);
        holder.subheading.setText("From: " + student.getStart().toString());
        holder.approve.setText("To: " + student.getEnd().toString());
        holder.status.setText("Pending");

        int score = student.getScore();
        holder.score.setText("Rating: " +rate(score));
        if(score==0)holder.score.setTextColor(Color.RED);
        else if(score==1)holder.score.setTextColor(Color.parseColor("#FFA500"));
        else if(score==2)holder.score.setTextColor(Color.GREEN);
        int cnt = list.get(position).getThird();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowApplication.class);
                intent.putExtra("UID", UID);
                intent.putExtra("Start", student.getStart().toString());
                intent.putExtra("End", student.getEnd().toString());
                intent.putExtra("Reason", student.getReason().toString());
                intent.putExtra("cnt", cnt);
                context.startActivity(intent);
            }
        });

    }

    private String rate(int score) {
        if(score==0)return "Low";
        else if(score==1)return "Moderate";
        else if(score==2)return "High";
        return "";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading, subheading, approve, status, score;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.mainTeacherContainer);
            heading = itemView.findViewById(R.id.mainTeacherHead);
            subheading = itemView.findViewById(R.id.mainTeacherSubHead);
            approve = itemView.findViewById(R.id.mainTeacherApprove);
            status = itemView.findViewById(R.id.mainTeacherStatus);
            score = itemView.findViewById(R.id.score);
        }
    }
}

