package com.example.t6mobile_h071231080;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivCharacterDetail;
    private TextView tvCharacterNameDetail, tvStatus, tvSpeciesDetail, tvGender, tvOrigin, tvLocation;
    private ImageButton tvBack;

    private int characterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        characterId = getIntent().getIntExtra("CHARACTER_ID", -1);

        if (!isNetworkAvailable()) {
            showErrorLayout();
        } else if (characterId != -1) {
            setContentView(R.layout.activity_detail);
            setupViews();
            loadCharacterDetail(characterId);
        } else {
            Toast.makeText(this, "Invalid character ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showErrorLayout() {
        setContentView(R.layout.error);
        ImageButton btnReload = findViewById(R.id.btn_load);
        btnReload.setOnClickListener(v -> recreate());
    }

    private void setupViews() {
        ivCharacterDetail = findViewById(R.id.iv_character_detail);
        tvCharacterNameDetail = findViewById(R.id.tv_character_name_detail);
        tvStatus = findViewById(R.id.tv_status);
        tvSpeciesDetail = findViewById(R.id.tv_species_detail);
        tvGender = findViewById(R.id.tv_gender);
        tvOrigin = findViewById(R.id.tv_origin);
        tvLocation = findViewById(R.id.tv_location);
        tvBack = findViewById(R.id.btn_back);

        tvBack.setOnClickListener(view -> onBackPressed());

    }

    private void loadCharacterDetail(int characterId) {
        RetrofitClient.getClient().getCharacterDetail(characterId).enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayCharacterDetail(response.body());
                } else {
                    Toast.makeText(DetailActivity.this, "Gagal memuat detail karakter", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                showErrorLayout();
            }
        });
    }

    private void displayCharacterDetail(Character character) {
        Glide.with(this)
                .load(character.getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                .into(ivCharacterDetail);

        tvCharacterNameDetail.setText(character.getName());
        tvStatus.setText("Status: " + character.getStatus());
        tvSpeciesDetail.setText("Species: " + character.getSpecies());
        tvGender.setText("Gender: " + character.getGender());
        tvOrigin.setText(character.getOrigin().getName());
        tvLocation.setText(character.getLocation().getName());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
