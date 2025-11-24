package com.example.janbookappv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG = "BookListActivity";
    private static final String PREFS_NAME = "BookPrefs";
    private static final String KEY_BOOKS = "books";

    private RecyclerView recyclerViewBooks;
    private EditText editTextSearch;
    private ImageButton btnClearSearch;
    private TextView textViewBookCount;
    private LinearLayout emptyStateLayout;
    private FloatingActionButton fabAddBook;

    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;
    private ArrayList<Book> filteredBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // Initialize views
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        editTextSearch = findViewById(R.id.editTextSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        textViewBookCount = findViewById(R.id.textViewBookCount);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        fabAddBook = findViewById(R.id.fabAddBook);

        // Load books
        loadBooks();
        filteredBookList = new ArrayList<>(bookList);

        // Setup RecyclerView
        setupRecyclerView();

        // Update UI
        updateBookCount();
        updateEmptyState();

        // Setup search functionality
        setupSearch();

        // FAB click - Go to MainActivity to add book
        fabAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload books when returning from other activities
        loadBooks();
        filterBooks(editTextSearch.getText().toString());
        updateBookCount();
        updateEmptyState();
        Log.d(TAG, "Books reloaded. Total: " + bookList.size());
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBooks.setLayoutManager(layoutManager);

        bookAdapter = new BookAdapter(this, filteredBookList);
        recyclerViewBooks.setAdapter(bookAdapter);

        // Set click listener for book items
        bookAdapter.setOnBookClickListener(new BookAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(Book book, int position) {
                Log.d(TAG, "Book clicked: " + book.getTitle());

                // Go to SecondActivity with book details
                Intent intent = new Intent(BookListActivity.this, SecondActivity.class);
                intent.putExtra("BOOK_DATA", book);
                startActivity(intent);
            }
        });

        Log.d(TAG, "RecyclerView setup complete");
    }

    private void setupSearch() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                filterBooks(query);

                // Show/hide clear button
                if (query.isEmpty()) {
                    btnClearSearch.setVisibility(View.GONE);
                } else {
                    btnClearSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Clear search button
        btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
                btnClearSearch.setVisibility(View.GONE);
            }
        });
    }

    private void filterBooks(String query) {
        filteredBookList.clear();

        if (query.isEmpty()) {
            // No search query - show all books
            filteredBookList.addAll(bookList);
        } else {
            // Filter books by title, author, or year
            String lowerCaseQuery = query.toLowerCase();
            for (Book book : bookList) {
                if (book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        book.getAuthor().toLowerCase().contains(lowerCaseQuery) ||
                        book.getYear().toLowerCase().contains(lowerCaseQuery)) {
                    filteredBookList.add(book);
                }
            }
        }

        bookAdapter.updateBooks(filteredBookList);
        updateBookCount();
        updateEmptyState();

        Log.d(TAG, "Filtered books: " + filteredBookList.size() + " out of " + bookList.size());
    }

    private void updateBookCount() {
        int count = filteredBookList.size();
        int total = bookList.size();

        if (count == total) {
            textViewBookCount.setText(count + (count == 1 ? " book" : " books"));
        } else {
            textViewBookCount.setText(count + " of " + total + " books");
        }
    }

    private void updateEmptyState() {
        if (filteredBookList.isEmpty()) {
            recyclerViewBooks.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewBooks.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void loadBooks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String booksJson = prefs.getString(KEY_BOOKS, null);

        if (booksJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Book>>(){}.getType();
            bookList = gson.fromJson(booksJson, type);
            Log.d(TAG, "Loaded " + bookList.size() + " books from SharedPreferences");
        } else {
            bookList = new ArrayList<>();
            Log.d(TAG, "No saved books found. Starting with empty list.");
        }
    }
}