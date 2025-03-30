package com.example.groupmembersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAddMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        dbHelper.checkDatabaseStatus();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddMember = findViewById(R.id.fabAddMember);
        fabAddMember.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MemberFormActivity.class);
            startActivity(intent);
        });

        loadMembers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }

    private void loadMembers() {
        List<Member> memberList = dbHelper.getAllMembers();

        adapter = new MemberAdapter(memberList, this);
        recyclerView.setAdapter(adapter);
    }
}