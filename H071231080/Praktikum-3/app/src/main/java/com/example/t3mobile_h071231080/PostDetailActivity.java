package com.example.t3mobile_h071231080;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int profilePic = intent.getIntExtra("profilePic", R.drawable.profile);
        String caption = intent.getStringExtra("caption");
        int likes = intent.getIntExtra("likes", 0);
        int comments = intent.getIntExtra("comments", 0);
        String imageUri = intent.getStringExtra("imageUri");
        int imageRes = intent.getIntExtra("postImage", -1);
        boolean usingUri = intent.getBooleanExtra("usingUri", false);


        ImageView profileImageView = findViewById(R.id.detailProfileImage);
        TextView usernameTextView = findViewById(R.id.detailUsername);
        ImageView postImageView = findViewById(R.id.detailPostImage);
        TextView captionTextView = findViewById(R.id.detailCaption);
        TextView likesTextView = findViewById(R.id.detailLikes);
        TextView commentsTextView = findViewById(R.id.detailComments);

        profileImageView.setImageResource(profilePic);
        usernameTextView.setText(username);
        captionTextView.setText(caption);
        likesTextView.setText(likes + " likes");
        commentsTextView.setText("View all " + comments + " comments");

        if (usingUri && imageUri != null && !imageUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(imageUri);
                if (uri.toString().startsWith("file:")) {
                    File imageFile = new File(uri.getPath());
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        postImageView.setImageBitmap(bitmap);
                    } else {
                        postImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } else {
                    postImageView.setImageURI(uri);
                }

                // Check if image loaded successfully
                if (postImageView.getDrawable() == null) {
                    postImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } catch (Exception e) {
                postImageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else if (imageRes != -1) {
            postImageView.setImageResource(imageRes);
        } else {
            postImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }


        usernameTextView.setOnClickListener(v -> {
            Intent profileIntent = new Intent(PostDetailActivity.this, ProfileActivity.class);
            profileIntent.putExtra("username", username);
            startActivity(profileIntent);
        });


        profileImageView.setOnClickListener(v -> {
            Intent profileIntent = new Intent(PostDetailActivity.this, ProfileActivity.class);
            profileIntent.putExtra("username", username);
            startActivity(profileIntent);
        });
    }
}
