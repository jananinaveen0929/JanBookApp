package com.example.janbookapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String title;
    private String author;
    private String year;

    // Constructor
    public Book(String title, String author, String year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Parcelable Constructor - reads from Parcel
    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        year = in.readString();
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    // Parcelable Methods
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(year);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // CREATOR - required for Parcelable to pass data between activities via Intent
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