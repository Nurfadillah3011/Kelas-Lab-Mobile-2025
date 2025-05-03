package com.example.t3mobile_h071231080;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StoryDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);


        String title = getIntent().getStringExtra("storyTitle");
        int storyImage = getIntent().getIntExtra("storyImage", R.drawable.profile);

        TextView titleTextView = findViewById(R.id.storyTitle);
        ImageView storyImageView = findViewById(R.id.storyImage);
        ImageView closeButton = findViewById(R.id.closeButton);


        titleTextView.setText(title);
        storyImageView.setImageResource(storyImage);


        closeButton.setOnClickListener(v -> finish());
    }
}
