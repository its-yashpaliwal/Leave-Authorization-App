package com.example.leave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import kotlin.Triple;

public class TeacherDashboard extends AppCompatActivity {
    ArrayList<Triple<application, String, Integer>> list = new ArrayList<>();
    TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        loadData();
        adapter = new TeacherAdapter(list, this);
        RecyclerView teacherDash = (RecyclerView) findViewById(R.id.teacherRecyclerView);
        teacherDash.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        teacherDash.setLayoutManager(layoutManager);
        Button logout = findViewById(R.id.logout_teacher);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), FirstPage.class);
                startActivity(intent);
            }
        });
    }

    public void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database.getReference().child("Applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()) {
                    String key = x.getKey();
                    for (DataSnapshot y : x.getChildren()) {
                        Integer index = Integer.parseInt(y.getKey());
                        application ap = y.getValue(application.class);
                        if (ap.getPending() == true) list.add(new Triple<>(ap, key, index));
                    }
                }
                if (list.size() == 0)
                    Toast.makeText(TeacherDashboard.this, "No new applications at this time!", Toast.LENGTH_SHORT).show();
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent intent = new Intent(getApplicationContext(), FirstPage.class);
        startActivity(intent);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        FirebaseAuth mauth = FirebaseAuth.getInstance();
//        mauth.signOut();
//        Intent intent = new Intent(getApplicationContext(), FirstPage.class);
//        startActivity(intent);
//    }

}
