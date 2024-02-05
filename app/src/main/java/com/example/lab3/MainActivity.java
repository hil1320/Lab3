package com.example.lab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        // Load saved details from SharedPreferences
        loadUserDetails();

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save details to SharedPreferences
                saveUserDetails();

                // Start SecondActivity and pass the email as an intent extra
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("email", emailEditText.getText().toString());
                startActivity(intent);
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
}
