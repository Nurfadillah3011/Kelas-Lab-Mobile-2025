package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesFragment extends Fragment implements FavoriteBookAdapter.OnFavoriteBookClickListener {

    private RecyclerView recyclerView;
    private FavoriteBookAdapter favoriteBookAdapter;
    private TextView tvNoFavorites;
    private BookManager bookManager;
    private ProgressBar progressBarFavorites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookManager = BookManager.getInstance();

        progressBarFavorites = view.findViewById(R.id.progressBarFavorites);
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize TextView for empty state
        tvNoFavorites = view.findViewById(R.id.tvNoFavorites);

        progressBarFavorites.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvNoFavorites.setVisibility(View.GONE);

        // Load favorite books
        loadFavoriteBooks();
    }

    private void loadFavoriteBooks() {
        progressBarFavorites.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvNoFavorites.setVisibility(View.GONE);

        // Simulasikan operasi latar belakang dengan Handler
        new android.os.Handler().postDelayed(() -> {
            List<Book> favoriteBooks = bookManager.getFavoriteBooks();

            if (favoriteBooks.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvNoFavorites.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvNoFavorites.setVisibility(View.GONE);

                // Gunakan FavoriteBookAdapter dengan this sebagai listener
                favoriteBookAdapter = new FavoriteBookAdapter(favoriteBooks, getContext(), this);
                recyclerView.setAdapter(favoriteBookAdapter);
            }

            progressBarFavorites.setVisibility(View.GONE);
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavoriteBooks();
    }

    // Implementasi interface OnFavoriteBookClickListener
    @Override
    public void onBookClick(Book book) {
        // Mengarahkan ke DetailActivity sama seperti di BookAdapter
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        startActivity(intent);
    }
}