package com.example.lab_5_milestone_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    static final String usernameKey = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.lab_5_milestone_1", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(usernameKey, "").equals("")) {
            startNotesActivity(sharedPreferences.getString(usernameKey, ""));
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    public void login(View view) {
        // Get username and password
        EditText usernameField = (EditText) findViewById(R.id.editTextUsername);
        EditText passwordField = (EditText) findViewById(R.id.editTextPassword);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (password.isEmpty()) {
            PasswordDialog passwordDialog = new PasswordDialog();
            passwordDialog.show(getSupportFragmentManager(), "no password");
            return;
        }

        // Add username to SharedPreferences object
        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.lab_5_milestone_1", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username", username).apply();

        // Start the second activity
        startNotesActivity(username);
    }

    private void startNotesActivity(String username) {
        Intent intent = new Intent(this, NotesActivity.class);
        // intent.putExtra("username", username);
        startActivity(intent);
    }
}