package com.example.lab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ThirdActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText phoneNumberEditText;
    private ImageView profileImageView;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        profileImageView = findViewById(R.id.profileImageView);

        // Load user details from SharedPreferences
        loadUserDetails();

        Button updateImageButton = findViewById(R.id.updateImageButton);
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the camera activity to update the image
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    private void loadUserDetails() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        // Load details from SharedPreferences and set them to EditTexts
        emailEditText.setText(preferences.getString("email", ""));
        nameEditText.setText(preferences.getString("name", ""));
        surnameEditText.setText(preferences.getString("surname", ""));
        phoneNumberEditText.setText(preferences.getString("phoneNumber", ""));

        // Load and set profile image
        Bitmap profileImage = loadImageFromInternalStorage("profile_image");
        if (profileImage != null) {
            profileImageView.setImageBitmap(profileImage);
        }
    }

    private void saveUserDetails() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Save details to SharedPreferences
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("name", nameEditText.getText().toString());
        editor.putString("surname", surnameEditText.getText().toString());
        editor.putString("phoneNumber", phoneNumberEditText.getText().toString());

        // Apply the changes
        editor.apply();
    }

    private void saveImageToInternalStorage(Bitmap bitmap, String imageName) {
        try {
            File internalStorage = getDir("images", MODE_PRIVATE);
            File imageFile = new File(internalStorage, imageName);

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromInternalStorage(String imageName) {
        try {
            File internalStorage = getDir("images", MODE_PRIVATE);
            File imageFile = new File(internalStorage, imageName);

            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Get the captured image
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            // Save the image to internal storage
            saveImageToInternalStorage(imageBitmap, "profile_image");

            // Update the ImageView
            profileImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save user details when the activity is paused
        saveUserDetails();
    }
}
