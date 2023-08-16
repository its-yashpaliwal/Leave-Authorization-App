package com.example.leave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StudentDashboard extends AppCompatActivity {

    ArrayList<application> list = new ArrayList<>();
    StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        loadData();
        adapter = new StudentAdapter(list, this);
        RecyclerView studentDash = (RecyclerView) findViewById(R.id.studentRecyclerView);
        studentDash.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        studentDash.setLayoutManager(layoutManager);
    }

    public void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database.getReference().child("Applications").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()) {
                    application ap = x.getValue(application.class);
                    list.add(ap);
                }
                Collections.reverse(list);
                if(list.size()==0) Toast.makeText(getApplicationContext(), "No past applications found", Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
