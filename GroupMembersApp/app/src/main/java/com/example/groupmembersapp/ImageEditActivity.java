package com.example.groupmembersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ImageEditActivity extends AppCompatActivity {

    private ImageView imagePreview;
    private EditText editImageUrl;
    private TextView textMemberDetails;
    private Button buttonSaveImageUrl, buttonCancel;

    private int memberId;
    private String currentImageUrl;
    private Member member;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Image");
        }

        // Initialize UI components
        imagePreview = findViewById(R.id.imagePreview);
        editImageUrl = findViewById(R.id.editImageUrl);
        textMemberDetails = findViewById(R.id.textMemberDetails);
        buttonSaveImageUrl = findViewById(R.id.buttonSaveImageUrl);
        buttonCancel = findViewById(R.id.buttonCancel);

        dbHelper = new DatabaseHelper(this);

        // Get data from intent
        if (getIntent().hasExtra("member_id")) {
            memberId = getIntent().getIntExtra("member_id", -1);
            loadMemberData();
        } else {
            Toast.makeText(this, "Error: Missing member ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set click listeners
        buttonSaveImageUrl.setOnClickListener(v -> saveImageUrl());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void loadMemberData() {
        member = dbHelper.getMemberById(memberId);
        if (member != null) {
            currentImageUrl = member.getImageUrl();
            editImageUrl.setText(currentImageUrl);

            // Display member details
            String details = "Name: " + member.getName() + "\n" +
                    "Role: " + member.getRole() + "\n\n" +
                    "Details: " + member.getDetails();
            textMemberDetails.setText(details);

            // Load image
            loadImagePreview(currentImageUrl);
        } else {
            Toast.makeText(this, "Error: Member not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadImagePreview(String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imagePreview);
        } else {
            imagePreview.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private void saveImageUrl() {
        String newImageUrl = editImageUrl.getText().toString().trim();

        if (newImageUrl.isEmpty()) {
            editImageUrl.setError("Image URL cannot be empty");
            return;
        }

        // Update the member in database with new image URL
        member.setImageUrl(newImageUrl);
        int result = dbHelper.updateMember(member);

        if (result > 0) {
            Toast.makeText(this, "Image URL updated successfully", Toast.LENGTH_SHORT).show();

            // Send result back to DetailActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updated_image_url", newImageUrl);
            setResult(RESULT_OK, resultIntent);

            finish();
        } else {
            Toast.makeText(this, "Failed to update image URL", Toast.LENGTH_SHORT).show();
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