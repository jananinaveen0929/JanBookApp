package com.example.janbookappv2;

import java.util.ArrayList;

/**
 * Singleton class to manage the list of books across the application
 * This ensures the book list persists across activities during the app session
 */
public class BookManager {

    private static BookManager instance;
    private ArrayList<Book> books;

    // Private constructor for singleton pattern
    private BookManager() {
        books = new ArrayList<>();
    }

    // Get singleton instance
    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    // Add a book to the list
    public void addBook(Book book) {
        books.add(book);
    }

    // Get all books
    public ArrayList<Book> getBooks() {
        return books;
    }

    // Remove a book
    public void removeBook(int position) {
        if (position >= 0 && position < books.size()) {
            books.remove(position);
        }
    }

    // Clear all books
    public void clearAllBooks() {
        books.clear();
    }

    // Get book count
    public int getBookCount() {
        return books.size();
    }
}
