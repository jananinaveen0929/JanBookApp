package com.example.janbookappv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAuthor, editTextYear;
    private Button btnSubmit, btnViewBooks;

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "BookPrefs";
    private static final String KEY_BOOKS = "books";

    private ArrayList<Book> bookList;
    private int nextBookId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextYear = findViewById(R.id.editTextYear);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnViewBooks = findViewById(R.id.btnViewBooks);

        // Check if views are found
        if (editTextTitle == null || editTextAuthor == null || editTextYear == null || btnSubmit == null) {
            Log.e(TAG, "ERROR: One or more views not found!");
            Toast.makeText(this, "Error: Views not initialized", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "All views initialized successfully");

        // Load existing books from SharedPreferences
        loadBooks();

        // Submit button - Add book
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
                Book book = new Book(nextBookId, title, author, year, android.R.drawable.ic_menu_gallery);
                nextBookId++;

                Log.d(TAG, "Book object created: " + book.getTitle() + " with ID: " + book.getId());

                // Add book to the list
                bookList.add(book);
                saveBooks();

                // Show success message
                Toast.makeText(MainActivity.this, "✅ Book added to collection!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Book added to list. Total books: " + bookList.size());

                // Clear input fields
                editTextTitle.setText("");
                editTextAuthor.setText("");
                editTextYear.setText("");

                // Focus on title field for next entry
                editTextTitle.requestFocus();
            }
        });

        // View Books button - Go to BookListActivity
        btnViewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View Books button clicked");
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload books when returning from BookListActivity
        loadBooks();
        Log.d(TAG, "Books reloaded. Total: " + bookList.size());
    }

    private void loadBooks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String booksJson = prefs.getString(KEY_BOOKS, null);

        if (booksJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Book>>(){}.getType();
            bookList = gson.fromJson(booksJson, type);

            // Update nextBookId to be one more than the highest ID
            for (Book book : bookList) {
                if (book.getId() >= nextBookId) {
                    nextBookId = book.getId() + 1;
                }
            }
            Log.d(TAG, "Loaded " + bookList.size() + " books from SharedPreferences");
        } else {
            bookList = new ArrayList<>();
            Log.d(TAG, "No saved books found. Starting with empty list.");
        }
    }

    private void saveBooks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String booksJson = gson.toJson(bookList);
        editor.putString(KEY_BOOKS, booksJson);
        editor.apply();

        Log.d(TAG, "Saved " + bookList.size() + " books to SharedPreferences");
    }
}