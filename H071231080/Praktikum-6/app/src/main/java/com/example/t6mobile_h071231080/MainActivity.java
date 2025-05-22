package com.example.t6mobile_h071231080;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCharacters;
    private CharacterAdapter adapter;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCharacters = findViewById(R.id.rv_characters);
        btnLoadMore = findViewById(R.id.btn_load_more);
        progressBar = findViewById(R.id.progress_bar);

        rvCharacters.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CharacterAdapter(this);
        rvCharacters.setAdapter(adapter);

        btnLoadMore.setVisibility(View.GONE);

        new Handler().postDelayed(() -> loadCharacters(currentPage), 2000);

        btnLoadMore.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btnLoadMore.setEnabled(false);

            new Handler().postDelayed(() -> {
                currentPage++;
                loadCharacters(currentPage);
            }, 2000);
        });
    }

    private void loadCharacters(int page) {
        if (!isNetworkAvailable()) {
            setContentView(R.layout.error);
            ImageButton btnReload = findViewById(R.id.btn_load);
            btnReload.setOnClickListener(v -> {
                if (isNetworkAvailable()) {
                    recreate();
                } else {
                    Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        RetrofitClient.getClient().getCharacters(page).enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnLoadMore.setVisibility(View.VISIBLE);
                btnLoadMore.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    adapter.addCharacters(response.body().getResults());

                    // Sembunyikan tombol jika tidak ada next page
                    if (response.body().getInfo().getNext() == null) {
                        btnLoadMore.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Gagal memuat data: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLoadMore.setEnabled(true);

                if (!isNetworkAvailable()) {
                    setContentView(R.layout.error);
                    ImageButton btnReload = findViewById(R.id.btn_load);
                    btnReload.setOnClickListener(v -> {
                        if (isNetworkAvailable()) {
                            recreate();
                        } else {
                            Toast.makeText(MainActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
