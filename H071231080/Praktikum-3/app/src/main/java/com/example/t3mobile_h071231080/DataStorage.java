package com.example.t3mobile_h071231080;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static List<Post> newPosts = new ArrayList<>();
    private static List<Post> allPosts = new ArrayList<>();
    private static boolean dataChanged = false;


    public static List<Post> getNewPosts() {
        return newPosts;
    }


    public static List<Post> getAllPosts() {
        return allPosts;
    }


    public static void addNewPost(Post post) {
        newPosts.add(0, post);
        allPosts.add(0, post);
        dataChanged = true;
    }


    public static boolean isDataChanged() {
        return dataChanged;
    }


    public static void setDataChanged(boolean changed) {
        dataChanged = changed;
    }


    public static void initializeDefaultPosts() {
        if (allPosts.isEmpty()) {
            allPosts.add(new Post(1, "novi_reazra", R.drawable.feed1, R.drawable.feed1, "Beautiful sunset at the beach #vacation", 120, 25));
            allPosts.add(new Post(2, "dizasazkia", R.drawable.feed2, R.drawable.feed2, "My new painting #art #creativity", 89, 15));
            allPosts.add(new Post(3, "nauradwi__", R.drawable.feed3, R.drawable.feed3, "Mountains are calling! #travel #adventure", 210, 34));
            allPosts.add(new Post(4, "rsky.auliyah", R.drawable.feed4, R.drawable.feed4, "Homemade pasta #foodie #cooking", 178, 42));
            allPosts.add(new Post(5, "harmeliayra", R.drawable.feed5, R.drawable.feed5, "Morning workout routine #fitness #health", 95, 12));
            allPosts.add(new Post(6, "salssaabiil", R.drawable.feed6, R.drawable.feed6, "Just got this new gadget! #tech #innovation", 156, 28));
            allPosts.add(new Post(7, "dilsky.png", R.drawable.feed7, R.drawable.feed7, "Current read. Any recommendations? #books #reading", 67, 22));
            allPosts.add(new Post(8, "cholynsharon_", R.drawable.feed8, R.drawable.feed8, "My furry friend #pets #catlover", 200, 45));
            allPosts.add(new Post(9, "sisfouh23", R.drawable.feed9, R.drawable.feed9, "At the concert last night #music #livemusic", 145, 19));
            allPosts.add(new Post(10, "labsisfo.uh", R.drawable.feed10, R.drawable.feed10, "Found this hidden lake #nature #hiking", 179, 31));

            allPosts.add(new Post(101, "fadillanstura", R.drawable.profile, R.drawable.pict1, "City lights #nightlife", 98, 12));
            allPosts.add(new Post(102, "fadillanstura", R.drawable.profile, R.drawable.pict2, "Finding peace in quiet moments.", 120, 18));
            allPosts.add(new Post(103, "fadillanstura", R.drawable.profile, R.drawable.pict3, "Embracing the spotlight", 155, 22));
            allPosts.add(new Post(104, "fadillanstura", R.drawable.profile, R.drawable.pict4, "Radiating sunshine energy today", 87, 9));
            allPosts.add(new Post(105, "fadillanstura", R.drawable.profile, R.drawable.pict5, "Colors of serenity", 210, 31));
        }
    }


    public static List<Post> getPostsByUsername(String username) {
        List<Post> userPosts = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getUsername().equals(username)) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }
}