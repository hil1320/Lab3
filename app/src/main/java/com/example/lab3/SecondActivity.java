package com.example.lab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private ImageView profileImageView;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        profileImageView = findViewById(R.id.profileImageView);

        // Retrieve email from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");
            Toast.makeText(this, "Email: " + email, Toast.LENGTH_SHORT).show();
        }

        // Load phone number from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedPhoneNumber = preferences.getString("phoneNumber", "");
        phoneNumberEditText.setText(savedPhoneNumber);

        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(view -> makePhoneCall());

        Button changePictureButton = findViewById(R.id.changePictureButton);
        changePictureButton.setOnClickListener(view -> takePicture());
    }

    private void makePhoneCall() {
        // Load phone number from EditText and initiate a phone call
        String phoneNumber = phoneNumberEditText.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(android.net.Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    private void takePicture() {
        // Start the camera activity to capture a new picture
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Handle the captured image
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Save the image to disk
            saveImageToDisk(imageBitmap);

            // Update the ImageView with the new image
            profileImageView.setImageBitmap(imageBitmap);
        }
    }

    private void saveImageToDisk(Bitmap bitmap) {
        // Save the image to a file
        File imageFile = new File(getFilesDir(), "profile_image.png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
