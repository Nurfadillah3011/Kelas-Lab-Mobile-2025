package com.example.t3mobile_h071231080;

public class Story {
    private int id;
    private String title;
    private int coverImage;

    public Story(int id, String title, int coverImage) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCoverImage() {
        return coverImage;
    }
}