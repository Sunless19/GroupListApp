package com.example.groupmembersapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.IOException;
import android.text.method.LinkMovementMethod;

public class DetailActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int IMAGE_EDIT_REQUEST_CODE = 100;

    private ImageView imageView;
    private EditText nameEditText, roleEditText, detailsEditText, editTextDetailWebUrl;
    private Button saveButton, deleteButton, editImageButton;

    private int memberId;
    private String imageUrl;
    private DatabaseHelper dbHelper;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DatabaseHelper(this);

        imageView = findViewById(R.id.imageDetail);
        editImageButton = findViewById(R.id.buttonEditImage);
        nameEditText = findViewById(R.id.editTextDetailName);
        roleEditText = findViewById(R.id.editTextDetailRole);
        detailsEditText = findViewById(R.id.editTextDetailInfo);
        editTextDetailWebUrl = findViewById(R.id.editTextDetailWebUrl); // Added Web URL field
        saveButton = findViewById(R.id.buttonSaveChanges);
        deleteButton = findViewById(R.id.buttonDeleteMember);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, ImageEditActivity.class);
            intent.putExtra("member_id", memberId);
            startActivityForResult(intent, IMAGE_EDIT_REQUEST_CODE);
        });

        memberId = getIntent().getIntExtra("member_id", -1);

        if (memberId != -1) {
            loadMemberData();
        } else {
            Toast.makeText(this, "Error: Invalid member ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        editImageButton.setOnClickListener(v -> openImageChooser());
        saveButton.setOnClickListener(v -> saveMemberChanges());
        deleteButton.setOnClickListener(v -> confirmDelete());



        editTextDetailWebUrl.setMovementMethod(LinkMovementMethod.getInstance());
        editTextDetailWebUrl.setOnClickListener(v -> {
            String url = editTextDetailWebUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                Intent intent = new Intent(DetailActivity.this, WebLinkActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("member_name", member.getName());
                startActivity(intent);
            }
        });

        Button buttonOpenWebUrl = findViewById(R.id.buttonOpenWebUrl);
        buttonOpenWebUrl.setOnClickListener(v -> {
            String url = editTextDetailWebUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                Intent intent = new Intent(DetailActivity.this, WebLinkActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("member_name", member.getName());
                startActivity(intent);
            } else {
                Toast.makeText(this, "No URL provided", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void loadMemberData() {
        member = dbHelper.getMemberById(memberId);
        if (member != null) {
            nameEditText.setText(member.getName());
            roleEditText.setText(member.getRole());
            detailsEditText.setText(member.getDetails());
            imageUrl = member.getImageUrl();
            editTextDetailWebUrl.setText(member.getWebUrl()); // Load Web URL

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        } else {
            Toast.makeText(this, "Error: Member not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_EDIT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Refresh the image with new URL
            String updatedImageUrl = data.getStringExtra("updated_image_url");
            if (updatedImageUrl != null && !updatedImageUrl.isEmpty()) {
                imageUrl = updatedImageUrl;

                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);

                // Also reload the member to ensure we have the latest data
                loadMemberData();
            }
        }
    }

    private void saveMemberChanges() {
        String newName = nameEditText.getText().toString().trim();
        String newRole = roleEditText.getText().toString().trim();
        String newDetails = detailsEditText.getText().toString().trim();
        String webUrl = editTextDetailWebUrl.getText().toString().trim();

        if (newName.isEmpty() || newRole.isEmpty() || newDetails.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        member.setWebUrl(webUrl);
        member.setName(newName);
        member.setRole(newRole);
        member.setDetails(newDetails);
        member.setImageUrl(imageUrl); // Update image URL

        int rowsUpdated = dbHelper.updateMember(member);
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Member updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Ștergere Membru")
                .setMessage("Sigur vrei să ștergi acest membru?")
                .setPositiveButton("Da", (dialog, which) -> {
                    dbHelper.deleteMember(memberId);
                    Toast.makeText(this, "Membru șters", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Nu", null)
                .show();
    }
}
