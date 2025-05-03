package com.example.t3mobile_h071231080;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FeedAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private List<Post> postList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DataStorage.initializeDefaultPosts();

        postList = new ArrayList<>();
        postList.addAll(DataStorage.getAllPosts());

        adapter = new FeedAdapter(postList, this);
        recyclerView.setAdapter(adapter);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("username", postList.get(position).getUsername());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataStorage.isDataChanged()) {
            postList.clear();
            postList.addAll(DataStorage.getAllPosts());
            adapter.notifyDataSetChanged();
            DataStorage.setDataChanged(false);
        }
    }
}