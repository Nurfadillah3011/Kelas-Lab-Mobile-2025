package com.example.libraryapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthor, tvYear, tvBlurb, tvGenre;
    private ImageView ivCover;
    private Button btnLike;
    private Book book;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvYear = findViewById(R.id.tvYear);
        tvBlurb = findViewById(R.id.tvBlurb);
        tvGenre = findViewById(R.id.tvGenre);
        ivCover = findViewById(R.id.ivCover);
        btnLike = findViewById(R.id.btnLike);

        // Ambil ID buku dari intent
        int bookId = getIntent().getIntExtra("BOOK_ID", -1);
        book = BookManager.getInstance().getBookById(bookId);

        if (book != null) {
            // Set detail buku
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvYear.setText(String.valueOf(book.getPublishYear()));
            tvBlurb.setText(book.getBlurb());
            tvGenre.setText(book.getGenre());

            // Tampilkan gambar
            String coverUri = book.getCoverImageUri();
            if (coverUri != null) {
                if (coverUri.startsWith("content://") || coverUri.startsWith("file://")) {
                    // URI dari galeri
                    ivCover.setImageURI(Uri.parse(coverUri));
                } else {
                    // Nama resource drawable
                    int resId = getResources().getIdentifier(coverUri, "drawable", getPackageName());
                    if (resId != 0) {
                        ivCover.setImageResource(resId);
                    } else {
                        ivCover.setImageResource(R.drawable.book_default);
                    }
                }
            }
        }

        btnLike.setOnClickListener(v -> {
            toggleFavorite();
        });
    }

    private void toggleFavorite() {
        if (book != null) {
            book.toggleFavorite();
            if (book.isFavorite()) {
                Toast.makeText(this, "Book added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Book removed from favorites", Toast.LENGTH_SHORT).show();
            }
            btnLike.setText(book.isFavorite() ? "Unlike" : "Like");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Perbarui teks tombol saat activity dimulai
        if (book != null) {
            btnLike.setText(book.isFavorite() ? "Unlike" : "Like");
        }
    }
}
