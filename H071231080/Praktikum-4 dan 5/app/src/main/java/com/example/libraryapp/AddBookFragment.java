package com.example.libraryapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddBookFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText etTitle, etAuthor, etYear, etBlurb;
    private ImageView ivBookCover;
    private Button btnAddBook;
    private String selectedImageUri = null;
    private TextView tvSelectCover;
    private Spinner spinnerGenre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Pastikan return inflater dengan layout fragment_add_book
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        etTitle = view.findViewById(R.id.etTitle);
        etAuthor = view.findViewById(R.id.etAuthor);
        etYear = view.findViewById(R.id.etYear);
        etBlurb = view.findViewById(R.id.etBlurb);
        spinnerGenre = view.findViewById(R.id.spinnerGenre);
        ivBookCover = view.findViewById(R.id.ivBookCover);
        btnAddBook = view.findViewById(R.id.btnAddBook);
        tvSelectCover = view.findViewById(R.id.tvSelectCover);

        // Dapatkan daftar genre dari BookManager
        List<String> genres = BookManager.getInstance().getAllGenres();

        // Jika daftar genre kosong, tambahkan beberapa default
        if (genres.isEmpty()) {
            genres = Arrays.asList("Fantasy", "Science Fiction",
                    "Horror", "Romance", "Thriller", "Drama");
        }

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genres
        );
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(genreAdapter);

        MaterialCardView cardCover = view.findViewById(R.id.cardCover);
        cardCover.setOnClickListener(v -> openGallery());

        btnAddBook.setOnClickListener(v -> addNewBook());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            selectedImageUri = imageUri.toString();

            // Display selected image
            ivBookCover.setImageURI(imageUri);
            tvSelectCover.setVisibility(View.GONE);
        }
    }

    private void addNewBook() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Get input values
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        int year = Integer.parseInt(etYear.getText().toString().trim());
        String blurb = etBlurb.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        if (selectedImageUri == null) {
            selectedImageUri = "android.resource://com.example.libraryapp/drawable/book_default";
        }

        try {
            Book newBook = new Book(title, author, year, blurb, selectedImageUri, genre);
            BookManager.getInstance().addBook(newBook);
            Toast.makeText(getContext(), "Book added successfully", Toast.LENGTH_SHORT).show();
            clearForm();
        } catch (IllegalArgumentException e) {
            // Tangani exception jika genre tidak valid
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etTitle.getText())) {
            etTitle.setError("Title is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etAuthor.getText())) {
            etAuthor.setError("Author is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etYear.getText())) {
            etYear.setError("Year is required");
            isValid = false;
        } else {
            try {
                int year = Integer.parseInt(etYear.getText().toString().trim());
                if (year < 1000 || year > 2025) {
                    etYear.setError("Enter a valid year");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etYear.setError("Enter a valid year");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(etBlurb.getText())) {
            etBlurb.setError("Description is required");
            isValid = false;
        }


        return isValid;
    }

    private void clearForm() {
        etTitle.setText("");
        etAuthor.setText("");
        etYear.setText("");
        etBlurb.setText("");
        // Reset spinner ke pilihan pertama
        if (spinnerGenre.getAdapter().getCount() > 0) {
            spinnerGenre.setSelection(0);
        }
        ivBookCover.setImageResource(R.drawable.book_default);
        tvSelectCover.setVisibility(View.VISIBLE);
        selectedImageUri = null;
    }
}