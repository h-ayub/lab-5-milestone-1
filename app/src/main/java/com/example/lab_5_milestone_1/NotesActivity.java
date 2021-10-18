package com.example.lab_5_milestone_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    private TextView welcomeText;
    public static ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Display welcome message
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        // Intent intent = getIntent();
        // String username = intent.getStringExtra("username");
        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.lab_5_milestone_1", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(MainActivity.usernameKey, "");
        welcomeText.setHint("Welcome " + username);

        // Get SQLiteDatabase instance
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);

        // Initiate the notes class variable using readNotes method implemented in DBHelper class.
        // Use the username got from sharedPreferences as a parameter to readNotes method
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        notes = dbHelper.readNotes(username);

        // Create ArrayList<String> object by iterating over notes object
        ArrayList<String> displayNotes = new ArrayList<>();
        for (Note note : notes) {
            displayNotes.add(String.format("Title: %s\nDate: %s", note.getTitle(), note.getDate()));
        }

        // Use ListView view to display notes on screen
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        ListView listView = (ListView) findViewById(R.id.notesListView);
        listView.setAdapter(adapter);

        // Add onItemClickListener for ListView item, a note in our case
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // initialize intent to take user to third activity
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                // Add the position of the item that was clicked on as "noteid"
                intent.putExtra("noteid", i);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        switch(item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPreferences =
                        getSharedPreferences("com.example.lab_5_milestone_1",
                                Context.MODE_PRIVATE);
                sharedPreferences.edit().remove(MainActivity.usernameKey).apply();
                startActivity(intent);
                return true;
            case R.id.addNote:
                intent = new Intent(this, NewNoteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}