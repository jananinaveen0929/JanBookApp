package com.example.janbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAuthor, editTextYear;
    private Button btnSubmit;

    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_YEAR = "year";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextYear = findViewById(R.id.editTextYear);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Check if views are found
        if (editTextTitle == null || editTextAuthor == null || editTextYear == null || btnSubmit == null) {
            Log.e(TAG, "ERROR: One or more views not found!");
            Toast.makeText(this, "Error: Views not initialized", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "All views initialized successfully");

        // Restore saved state if exists
        if (savedInstanceState != null) {
            editTextTitle.setText(savedInstanceState.getString(KEY_TITLE));
            editTextAuthor.setText(savedInstanceState.getString(KEY_AUTHOR));
            editTextYear.setText(savedInstanceState.getString(KEY_YEAR));
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit button clicked");

                String title = editTextTitle.getText().toString().trim();
                String author = editTextAuthor.getText().toString().trim();
                String year = editTextYear.getText().toString().trim();

                Log.d(TAG, "Title: " + title);
                Log.d(TAG, "Author: " + author);
                Log.d(TAG, "Year: " + year);

                // Validation
                if (title.isEmpty() || author.isEmpty() || year.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Validation failed: Empty fields");
                    return;
                }

                // Create Book object
                Book book = new Book(title, author, year);
                Log.d(TAG, "Book object created: " + book.getTitle());

                // Create intent and pass book object
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("BOOK_DATA", book);
                Log.d(TAG, "Starting SecondActivity with book data");

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, editTextTitle.getText().toString());
        outState.putString(KEY_AUTHOR, editTextAuthor.getText().toString());
        outState.putString(KEY_YEAR, editTextYear.getText().toString());
    }
}