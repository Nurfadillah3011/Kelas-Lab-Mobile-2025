package com.example.libraryapp;


import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
    private static int lastId = 0;

    private int id;
    private String title;
    private String author;
    private int publishYear;
    private String blurb;
    private String coverImageUri;
    private boolean isFavorite;
    private Date addedDate;
    private String genre;

    // Konstruktor untuk membuat objek Book baru
    public Book(String title, String author, int publishYear, String blurb, String coverImageUri, String genre) {
        this.id = ++lastId; // Auto increment ID
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.blurb = blurb;
        this.coverImageUri = coverImageUri;
        this.isFavorite = false; // Default bukan favorit
        this.addedDate = new Date(); // Waktu saat objek dibuat
        this.genre = genre;
    }


    public int getId() {
        return id;
    }
    public String getTitle()
    {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public int getPublishYear() {
        return publishYear;
    }
    public String getBlurb() {
        return blurb;
    }
    public String getCoverImageUri() {
        return coverImageUri;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public Date getAddedDate() {
        return addedDate;
    }
    public String getGenre() {
        return genre;
    }

    // Method untuk membalik status favorit (like/unlike)
    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
    }
}
