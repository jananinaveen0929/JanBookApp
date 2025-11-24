package com.example.janbookappv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private OnBookClickListener listener;

    // Interface for handling book item clicks
    public interface OnBookClickListener {
        void onBookClick(Book book, int position);
    }

    // Constructor 1: No arguments (for MainActivity)
    public BookAdapter() {
        this.bookList = new ArrayList<>();
    }

    // Constructor 2: With Context and ArrayList (for BookListActivity)
    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList != null ? bookList : new ArrayList<>();
    }

    // Constructor 3: With Context only
    public BookAdapter(Context context) {
        this.context = context;
        this.bookList = new ArrayList<>();
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get context from parent if not set
        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bind(book, position);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // Method to add a book to the list
    public void addBook(Book book) {
        bookList.add(book);
        notifyItemInserted(bookList.size() - 1);
    }

    // Method to update the entire list (used by MainActivity)
    public void setBooks(List<Book> books) {
        this.bookList = books != null ? books : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Method to update the entire list (used by BookListActivity) - ADDED FOR COMPATIBILITY
    public void updateBooks(List<Book> books) {
        this.bookList = books != null ? books : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Method to get all books
    public List<Book> getBooks() {
        return bookList;
    }

    // Method to clear all books
    public void clearBooks() {
        bookList.clear();
        notifyDataSetChanged();
    }

    // Method to remove a book at position
    public void removeBook(int position) {
        if (position >= 0 && position < bookList.size()) {
            bookList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookList.size());
        }
    }

    // Method to update a book at position
    public void updateBook(int position, Book book) {
        if (position >= 0 && position < bookList.size()) {
            bookList.set(position, book);
            notifyItemChanged(position);
        }
    }

    // ViewHolder class
    class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewBookTitle;
        private TextView textViewBookAuthor;
        private TextView textViewBookYear;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewBookAuthor = itemView.findViewById(R.id.textViewBookAuthor);
            textViewBookYear = itemView.findViewById(R.id.textViewBookYear);
        }

        public void bind(final Book book, final int position) {
            textViewBookTitle.setText(book.getTitle());
            textViewBookAuthor.setText("by " + book.getAuthor());
            textViewBookYear.setText("Year: " + book.getYear());

            // Set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onBookClick(book, position);
                    }
                }
            });
        }
    }
}