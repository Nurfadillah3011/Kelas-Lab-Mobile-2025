package com.example.libraryapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private SearchView searchView;
    private Spinner genreSpinner;
    private TextView tvNoSearch;

    private ProgressBar progressBar;
    private BookManager bookManager;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookManager = BookManager.getInstance();

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookAdapter = new BookAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(bookAdapter);
        tvNoSearch = view.findViewById(R.id.tvNoSearch);


        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //Saat tekan enter
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return false;
            }
            // Saat mengetik
            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return false;
            }
        });

        setupGenreSpinner(view);
        loadAllBooks();
    }

    private void setupGenreSpinner(View view) {
        genreSpinner = view.findViewById(R.id.genreSpinner);

        // Ambil semua genre dari BookManager
        List<String> genres = bookManager.getAllGenres();
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("All Genres");
        spinnerItems.addAll(genres);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter <>(
                getContext(),
                android.R.layout.simple_spinner_item,
                spinnerItems);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(spinnerAdapter);

        // Listener saat item spinner dipilih
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = parent.getItemAtPosition(position).toString();
                filterByGenre(selectedGenre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterBooks(final String query) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Book> filteredList;
                if (query == null || query.isEmpty()) {
                    filteredList = bookManager.getAllBooks();
                } else {
                    filteredList = bookManager.searchBooks(query);
                }

                // Simulasi loading agar ProgressBar terlihat
                try { Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace(); }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bookAdapter.updateData(filteredList);
                        progressBar.setVisibility(View.GONE);

                        if (filteredList.isEmpty()) {
                            tvNoSearch.setText("Tidak ditemukan buku atau penulis dengan \"" + query + "\"");
                            recyclerView.setVisibility(View.GONE);
                            tvNoSearch.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNoSearch.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }

    private void filterByGenre(final String genre) {

        List<Book> filteredBooks;
        if (genre.equals("All Genres")) {
            filteredBooks = bookManager.getAllBooks();
        } else {
            filteredBooks = bookManager.getBooksByGenre(genre);
        }

        bookAdapter.updateData(filteredBooks);
    }


    private void loadAllBooks() {
        progressBar.setVisibility(View.VISIBLE);

        List<Book> allBooks = bookManager.getAllBooks();
        bookAdapter.updateData(allBooks);

        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Saat kembali ke fragment, muat ulang semua buku
        loadAllBooks();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
