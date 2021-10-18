package com.example.lab_5_milestone_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {
    int noteid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        EditText editText = (EditText) findViewById(R.id.editTextNewNote);
        Intent intent = getIntent();
        noteid = intent.getIntExtra("noteid", -1);

        if (noteid != -1) {
            Note note = NotesActivity.notes.get(noteid);
            String noteContent = note.getContent();
            editText.setText(noteContent);
        }
    }

    public void save(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextNewNote);
        String content = editText.getText().toString();

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        SharedPreferences sharedPreferences =
                getSharedPreferences("com.example.lab_5_milestone_1", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(MainActivity.usernameKey, "");

        String title;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if (noteid == -1) {
            title = "NOTE_" + (NotesActivity.notes.size() + 1);
            dbHelper.saveNotes(username, title, content, date);
        } else {
            title = "NOTE_" + (noteid + 1);
            dbHelper.updateNote(title, date, content, username);
        }

        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }
}