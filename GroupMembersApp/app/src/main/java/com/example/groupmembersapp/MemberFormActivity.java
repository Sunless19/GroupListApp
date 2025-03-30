package com.example.groupmembersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class MemberFormActivity extends AppCompatActivity {

    private TextInputEditText editName, editRole, editImageUrl, editDetails, editWebUrl;
    private TextView textFormTitle;
    private Button buttonSave;
    private DatabaseHelper dbHelper;

    private int memberId = -1;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_form);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI components
        textFormTitle = findViewById(R.id.textFormTitle);
        editName = findViewById(R.id.editName);
        editRole = findViewById(R.id.editRole);
        editImageUrl = findViewById(R.id.editImageUrl);
        editDetails = findViewById(R.id.editDetails);
        editWebUrl = findViewById(R.id.editWebUrl);
        buttonSave = findViewById(R.id.buttonSave);

        dbHelper = new DatabaseHelper(this);

        // Check if we're editing an existing member
        if (getIntent().hasExtra("member_id")) {
            memberId = getIntent().getIntExtra("member_id", -1);
            isEditing = true;
            textFormTitle.setText("Edit Member");
            loadMemberData(memberId);
        }

        buttonSave.setOnClickListener(v -> saveMember());
    }

    private void loadMemberData(int id) {
        Member member = dbHelper.getMemberById(id);
        if (member != null) {
            editName.setText(member.getName());
            editRole.setText(member.getRole());
            editImageUrl.setText(member.getImageUrl());
            editDetails.setText(member.getDetails());
            editWebUrl.setText(member.getWebUrl());
        }
    }

    private void saveMember() {
        String name = editName.getText().toString().trim();
        String role = editRole.getText().toString().trim();
        String imageUrl = editImageUrl.getText().toString().trim();
        String details = editDetails.getText().toString().trim();
        String webUrl = editWebUrl.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Name is required");
            return;
        }

        if (role.isEmpty()) {
            editRole.setError("Role is required");
            return;
        }

        boolean success = false;

        try {
            if (isEditing) {
                // Update existing member
                Member member = new Member(memberId, name, role, imageUrl, details, webUrl);
                int updateResult = dbHelper.updateMember(member);
                success = updateResult > 0;
                Log.d("MemberFormActivity", "Update result: " + updateResult);
            } else {
                // Create new member
                // Generate a random ID (in a real app, you'd use autoincrement)
                Random random = new Random();
                memberId = random.nextInt(1000) + 10; // Avoiding potential conflicts

                Member member = new Member(memberId, name, role, imageUrl, details, webUrl);
                long insertResult = dbHelper.insertMember(member);
                success = insertResult != -1;
                Log.d("MemberFormActivity", "Insert result: " + insertResult);
            }

            if (success) {
                Toast.makeText(this, isEditing ? "Member updated successfully" : "Member added successfully",
                        Toast.LENGTH_SHORT).show();
                finish(); // Return to previous activity
            } else {
                Toast.makeText(this, "Failed to save member", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MemberFormActivity", "Error saving member: " + e.getMessage());
            Toast.makeText(this, "Error saving member: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}