package com.example.janbookappv2;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int id;
    private String title;
    private String author;
    private String year;
    private String publisher;
    private int coverImageResId; // Resource ID for book cover image

    // Constructor with all fields
    public Book(int id, String title, String author, String year,
                String publisher, int coverImageResId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.coverImageResId = coverImageResId;
    }

    // Constructor for basic book info (without publisher)
    public Book(int id, String title, String author, String year, int coverImageResId) {
        this(id, title, author, year, "", coverImageResId);
    }

    // Simple constructor (minimum fields)
    public Book(String title, String author, String year) {
        this(0, title, author, year, "", 0);
    }

    // Parcelable Constructor - reads from Parcel
    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        year = in.readString();
        publisher = in.readString();
        coverImageResId = in.readInt();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getCoverImageResId() {
        return coverImageResId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setCoverImageResId(int coverImageResId) {
        this.coverImageResId = coverImageResId;
    }

    // Parcelable Methods
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(year);
        dest.writeString(publisher);
        dest.writeInt(coverImageResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // CREATOR - required for Parcelable
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}