package com.example.janbookappv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private Button btnOpenBrowser, btnSendEmail;
    private String bookTitle, bookAuthor, bookYear;

    private static final String TAG = "ThirdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Initialize views
        editTextSearch = findViewById(R.id.editTextSearch);
        btnOpenBrowser = findViewById(R.id.btnOpenBrowser);
        btnSendEmail = findViewById(R.id.btnSendEmail);

        // Get book data from intent (optional - can use for email)
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            bookTitle = receivedIntent.getStringExtra("BOOK_TITLE");
            bookAuthor = receivedIntent.getStringExtra("BOOK_AUTHOR");
            bookYear = receivedIntent.getStringExtra("BOOK_YEAR");

            Log.d(TAG, "Received book data:");
            Log.d(TAG, "Title: " + bookTitle);
            Log.d(TAG, "Author: " + bookAuthor);
            Log.d(TAG, "Year: " + bookYear);
        }

        // Search when user presses Enter/Search on keyboard
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchInBrowser();
                return true;
            }
            return false;
        });

        // Open Browser Intent with Custom Search Query
        btnOpenBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInBrowser();
            }
        });

        // Send Email Intent
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailAboutBook();
            }
        });
    }

    private void searchInBrowser() {
        // Get the search query from EditText
        String searchQuery = editTextSearch.getText().toString().trim();

        // Check if query is empty
        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Search query: " + searchQuery);

        try {
            // Encode the query for URL (handles spaces and special characters)
            String encodedQuery = URLEncoder.encode(searchQuery, "UTF-8");

            // Create Google search URL
            String searchUrl = "https://www.google.com/search?q=" + encodedQuery;

            Log.d(TAG, "Search URL: " + searchUrl);

            // Open browser with search URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(searchUrl));

            startActivity(browserIntent);
            Log.d(TAG, "Browser opened successfully");

            // Clear the search field after searching (optional)
            // editTextSearch.setText("");

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding URL: " + e.getMessage());
            Toast.makeText(this, "Error creating search URL", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error opening browser: " + e.getMessage());
            Toast.makeText(this, "No browser app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailAboutBook() {
        // Build email content with book information
        String emailSubject = "Book Recommendation";
        String emailBody = "Check out this amazing book app!";

        if (bookTitle != null && !bookTitle.isEmpty()) {
            emailSubject = "Book Recommendation: " + bookTitle;
            emailBody = "I wanted to share this book with you:\n\n";
            emailBody += "Title: " + bookTitle + "\n";

            if (bookAuthor != null && !bookAuthor.isEmpty()) {
                emailBody += "Author: " + bookAuthor + "\n";
            }

            if (bookYear != null && !bookYear.isEmpty()) {
                emailBody += "Year: " + bookYear + "\n";
            }

            emailBody += "\nCheck it out!";
        }

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@email.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using:"));
            Log.d(TAG, "Email app opened successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error opening email: " + e.getMessage());
            Toast.makeText(ThirdActivity.this,
                    "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}


