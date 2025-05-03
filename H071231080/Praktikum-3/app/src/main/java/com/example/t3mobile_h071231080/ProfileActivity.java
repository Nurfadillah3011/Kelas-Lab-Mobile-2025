package com.example.t3mobile_h071231080;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String DEFAULT_USERNAME = "fadillanstura";
    private RecyclerView recyclerViewPosts, recyclerViewHighlights;
    private List<Post> profilePosts;
    private List<Story> highlightStories = new ArrayList<>();
    private String displayUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        displayUsername = getIntent().getStringExtra("username");
        if (displayUsername == null) displayUsername = DEFAULT_USERNAME;

        Log.d("ProfileActivity", "Setting username to: " + displayUsername);

        DataStorage.initializeDefaultPosts();
        List<Post> userPosts = DataStorage.getPostsByUsername(displayUsername);

        setupProfileInfo(userPosts);
        setupButtons();
        setupRecyclerViews(userPosts);
        setupBottomNavigation();
    }

    private void setupProfileInfo(List<Post> userPosts) {
        setText(R.id.profileUsername, displayUsername);
        setText(R.id.postsCount, String.valueOf(userPosts.size()));

        if (displayUsername.equals(DEFAULT_USERNAME)) {
            setText(R.id.followersCount, "254");
            setText(R.id.followingCount, "43");
        }

        ImageView profileImageView = findViewById(R.id.profileImage);
        int profileImageRes = !userPosts.isEmpty() ? userPosts.get(0).getProfilePicture() : R.drawable.profile;
        profileImageView.setImageResource(profileImageRes);

        setText(R.id.profileName, displayUsername);
    }

    private void setupButtons() {
        Button primaryButton = findViewById(R.id.primaryProfileButton);
        Button secondaryButton = findViewById(R.id.secondaryProfileButton);

        if (primaryButton == null || secondaryButton == null) return;

        if (displayUsername.equals(DEFAULT_USERNAME)) {
            primaryButton.setText("Edit Profile");
            secondaryButton.setText("Bagikan");

            primaryButton.setOnClickListener(v ->
                    Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            );



            secondaryButton.setOnClickListener(v ->
                    Toast.makeText(this, "Bagikan Profile clicked", Toast.LENGTH_SHORT).show()
            );
        } else {
            primaryButton.setText("Following");
            secondaryButton.setText("Message");

            primaryButton.setOnClickListener(v -> {
                String currentText = primaryButton.getText().toString();
                primaryButton.setText(currentText.equals("Following") ? "Follow" : "Following");
            });

            secondaryButton.setOnClickListener(v ->
                    Toast.makeText(this, "Message " + displayUsername, Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void setupRecyclerViews(List<Post> userPosts) {
        recyclerViewPosts = findViewById(R.id.recyclerViewProfilePosts);
        recyclerViewPosts.setLayoutManager(new GridLayoutManager(this, 3));
        profilePosts = userPosts;
        recyclerViewPosts.setAdapter(new ProfilePostAdapter(profilePosts));

        recyclerViewHighlights = findViewById(R.id.recyclerViewHighlights);
        recyclerViewHighlights.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        if (displayUsername.equals(DEFAULT_USERNAME)) {
            initHighlightsData();
            recyclerViewHighlights.setAdapter(new StoryAdapter(highlightStories));
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                if (!displayUsername.equals(DEFAULT_USERNAME)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                }
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, PostActivity.class));
                return true;
            }

            return false;
        });
    }

    private void initHighlightsData() {
        highlightStories.add(new Story(1, "Monster Inc", R.drawable.pict1sorotan));
        highlightStories.add(new Story(2, "Val Little", R.drawable.pict2sorotan));
        highlightStories.add(new Story(3, "Sully", R.drawable.pict3sorotan));
        highlightStories.add(new Story(4, "Mike", R.drawable.pict4sorotan));
        highlightStories.add(new Story(5, "Boo", R.drawable.pict5sorotan));
        highlightStories.add(new Story(6, "Tylor Tuskmon", R.drawable.pict6sorotan));
    }

    private void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null) tv.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataStorage.isDataChanged()) {

            profilePosts = DataStorage.getPostsByUsername(displayUsername);
            recyclerViewPosts.setAdapter(new ProfilePostAdapter(profilePosts));
            setText(R.id.postsCount, String.valueOf(profilePosts.size()));
            DataStorage.setDataChanged(false);
        }
    }
}